
DESCRIPTION = " Token server for use with Docker registry with Openstack Keystone back end"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d2794c0df5b907fdace235a619d80314"

GO_IMPORT = "registry-token-server"

SRC_URI = "file://registry-token-server-1.0.0.tar.gz \
           file://registry-token-server.service \
           file://token_server.conf \
          "

RDEPENDS_${PN}-dev_append = "systemd"

DEPENDS += "\
        go-logrus \
        docker-distribution \
        go-libtrust \
        go-patricia \
        go-mux \
        go-context \
        go-phercloud \
        "

inherit go goarch ${@bb.utils.contains('VIRTUAL-RUNTIME_init_manager','systemd','systemd','', d)}

do_compile() {
        mkdir -p _build/src
        ln -sfn ${WORKDIR}/${PN}-${PV} ./_build/src/registry-token-server
  
        # Pass the needed cflags/ldflags so that cgo
        # can find the needed headers files and libraries
        export GOARCH=${TARGET_GOARCH}
        export CGO_ENABLED="1"
        export CGO_CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
        export CGO_LDFLAGS="${LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"

        export GOPATH="${WORKDIR}/build/_build:${STAGING_DIR_TARGET}/${prefix}/local/go"
        cd _build/src/${GO_IMPORT}
        #oe_runmake registry-token-server
        export GOROOT=${STAGING_DIR_TARGET}/${prefix}/local/go
        go build -o ${WORKDIR}/build/bin/registry-token-server registry-token-server
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "registry-token-server.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
        install -d ${D}/${sbindir}
        install -m 0755 bin/registry-token-server ${D}/${sbindir}
        install -d ${D}/${sysconfdir}/registry-token-server/registry
        install -m 0644 ${WORKDIR}/token_server.conf ${D}/${sysconfdir}/registry-token-server/registry

        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
            install -d ${D}${systemd_unitdir}/system
            install -m 0644 ${WORKDIR}/registry-token-server.service ${D}${systemd_unitdir}/system/
        fi
}

