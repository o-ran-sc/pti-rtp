SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "iptables-config"
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

inherit systemd
SYSTEMD_PACKAGES += "${PN}"
SYSETMD_SERVICE_${PN}_append = "iptables.service ip6tables.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

do_install_append() {
    install -d -m0755 ${D}/${sysconfdir}/sysconfig
    install -m 600 ${WORKDIR}/${DSTSUFX0}/files/iptables.rules ${D}/${sysconfdir}/sysconfig/iptables
    install -m 600 ${WORKDIR}/${DSTSUFX0}/files/ip6tables.rules ${D}/${sysconfdir}/sysconfig/ip6tables
}
