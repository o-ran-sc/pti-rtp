FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "ntp-config"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

SRC_URI += " \
        git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
	"


do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/${DSTSUFX0}/files/LICENSE ${S}/stx-configfiles-LICENSE
}

do_install_append () {
        install -D -m644 ${WORKDIR}/${DSTSUFX0}/files/ntpd.sysconfig ${D}/${sysconfdir}/sysconfig/ntpd
        install -D -m644 ${WORKDIR}/${DSTSUFX0}/files/ntp.conf ${D}/${sysconfdir}/ntp.conf
}

SYSTEMD_AUTO_ENABLE = "disable"
RDEPENDS_${PN}_append = " bash"

FILES_${PN}_append = " ${sysconfdir}/sysconfig/ntpd"
