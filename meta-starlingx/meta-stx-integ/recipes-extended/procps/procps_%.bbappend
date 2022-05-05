FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH = "initscripts-config"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += " \
	file://stx-configfiles-LICENSE;beginline=1;endline=10;md5=5c43895c2c3756125227c74209b8b791 \
	"

SRC_URI += " \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH};name=opendev \
	"

do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/${DSTSUFX0}/centos/initscripts-config.spec ${S}/stx-configfiles-LICENSE
}

do_install_append () {
    install -d  -m 755 ${D}/${sysconfdir}

    install -m  644 ${WORKDIR}/${DSTSUFX0}/files/sysctl.conf ${D}/${sysconfdir}/sysctl.conf
}
