HOMEPAGE = "http://github.com/docker/distribution"
SUMMARY = "The Docker toolset to pack, ship, store, and deliver content"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d2794c0df5b907fdace235a619d80314"

GO_PKG_PATH = "github.com/docker/distribution"
GO_IMPORT = "import"

SRCREV_distribution="48294d928ced5dd9b378f7fd7c6f5da3ff3f2c89"
SRC_URI = " \
	git://github.com/docker/distribution.git;branch=release/2.6;name=distribution;destsuffix=git/src/${GO_PKG_PATH} \
	file://${BPN}.service \
	file://config.yml \
	"

PV = "v2.6.2"
S = "${WORKDIR}/git/src/${GO_PKG_PATH}"

inherit goarch
inherit go

# This disables seccomp and apparmor, which are on by default in the
# go package. 
EXTRA_OEMAKE="BUILDTAGS=''"

do_compile() {
	export GOARCH="${TARGET_GOARCH}"
	export GOPATH="${WORKDIR}/git/"
	export GOROOT="${STAGING_LIBDIR_NATIVE}/${TARGET_SYS}/go"
	# Pass the needed cflags/ldflags so that cgo
	# can find the needed headers files and libraries
	export CGO_ENABLED="1"
	export CFLAGS=""
	export LDFLAGS=""
	export CGO_CFLAGS="${BUILDSDK_CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
	export GO_GCFLAGS=""
	export CGO_LDFLAGS="${BUILDSDK_LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"

	cd ${S}

	oe_runmake binaries
}

do_install() {
	install -d ${D}/${bindir}
	install ${S}/bin/registry ${D}/${bindir}

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
	    install -d ${D}${systemd_system_unitdir}
	    install -m 644 ${WORKDIR}/${BPN}.service ${D}/${systemd_system_unitdir}
	fi

	install -d ${D}/${sysconfdir}/${BPN}/registry/
	install ${WORKDIR}/config.yml ${D}/${sysconfdir}/${BPN}/registry/config.yml

	# storage for the registry containers
	install -d ${D}/${localstatedir}/lib/registry/
}

FILES_${PN} = "\
	${bindir}/* \
	${systemd_system_unitdir}/${BPN}.service \
	${sysconfdir}/${BPN}/* \
	${localstatedir}/lib/registry/ \
	"

SYSTEMD_SERVICE_${BPN} = "${@bb.utils.contains('DISTRO_FEATURES','systemd','${BPN}.service','',d)}"
SYSTEMD_AUTO_ENABLE_${BPN} = "disable"


SYSROOT_PREPROCESS_FUNCS += "docker_distribution_sysroot_preprocess"

docker_distribution_sysroot_preprocess () {
    install -d ${SYSROOT_DESTDIR}${prefix}/local/go/src/${GO_PKG_PATH}
    cp -r ${S} ${SYSROOT_DESTDIR}${prefix}/local/go/src/$(dirname ${GO_PKG_PATH})
}
