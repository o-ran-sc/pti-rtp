FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "initscripts-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += " \
	file://${STX_METADATA_PATH}/centos/initscripts-config.spec;beginline=1;endline=10;md5=5c43895c2c3756125227c74209b8b791 \
	"

do_install_append () {
    install -d  -m 755 ${D}/${sysconfdir}
    install -m  644 ${STX_METADATA_PATH}/files/sysctl.conf ${D}/${sysconfdir}/sysctl.conf
}
