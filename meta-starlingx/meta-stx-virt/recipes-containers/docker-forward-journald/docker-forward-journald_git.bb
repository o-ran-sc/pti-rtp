
SUMMARY = "Forward stdin to journald"
HOMEPAGE = "https://github.com/docker/docker"
SECTION = "devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/forward-journald/LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"

PROTOCOL = "https"
SRCNAME = "forward-journald"
SRCREV = "77e02a9774a6ca054e41c27f6f319d701f1cbaea"
PV = "1.10.3+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/projectatomic/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};"

GO_IMPORT = "forward-journald"
inherit go goarch

do_compile() {
        mkdir -p _build/src
        ln -sfn ${S}/src/forward-journald ./_build/src/${SRCNAME}
        export GOARCH=${TARGET_GOARCH}
        export CGO_ENABLED="1"
        export CGO_CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
        export CGO_LDFLAGS="${LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"

        export GOPATH="${WORKDIR}/build/_build:${STAGING_DIR_TARGET}/${prefix}/local/go"
        cd _build/src/${SRCNAME}
        export GOROOT=${STAGING_DIR_TARGET}/${prefix}/local/go
        go build -ldflags "-B 0x$(head -c20 /dev/urandom|od -An -tx1|tr -d ' \n')" -o ${WORKDIR}/build/bin/forward-journald forward-journald
}

do_install() {
	install -m 0755 -d ${D}/${bindir}/

	install -m 0755 bin/forward-journald ${D}/${bindir}/
}
