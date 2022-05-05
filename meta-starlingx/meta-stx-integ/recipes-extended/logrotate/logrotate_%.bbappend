SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "logrotate-config"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

SRC_URI += " \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
"

RDEPENDS_${PN}_append = " cronie"

do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/${DSTSUFX0}/files/LICENSE ${S}/stx-configfiles-LICENSE
}

do_install_append() {
    install -d -m0755 ${D}/${sysconfdir}/cron.d/
    install -m 644 ${WORKDIR}/${DSTSUFX0}/files/logrotate-cron.d ${D}/${sysconfdir}/cron.d/logrotate
    install -m 644 ${WORKDIR}/${DSTSUFX0}/files/logrotate.conf ${D}/${sysconfdir}/logrotate.conf
    #mv ${D}/${sysconfdir}/cron.daily/logrotate ${D}/${sysconfdir}/logrotate.cron
    #chmod 700 ${D}/${sysconfdir}/logrotate.cron
}

