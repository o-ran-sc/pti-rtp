inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "logrotate-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

RDEPENDS_${PN}_append = " cronie"

do_install_append() {
    install -d -m0755 ${D}/${sysconfdir}/cron.d/
    install -m 644 ${STX_METADATA_PATH}/files/logrotate-cron.d ${D}/${sysconfdir}/cron.d/logrotate
    install -m 644 ${STX_METADATA_PATH}/files/logrotate.conf ${D}/${sysconfdir}/logrotate.conf
    #mv ${D}/${sysconfdir}/cron.daily/logrotate ${D}/${sysconfdir}/logrotate.cron
    #chmod 700 ${D}/${sysconfdir}/logrotate.cron
}

