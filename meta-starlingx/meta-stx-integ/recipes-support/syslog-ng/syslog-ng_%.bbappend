FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "syslog-ng-config"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

SRC_URI += " \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
	file://syslog-ng-config-parse-err.patch;striplevel=3 \
	file://syslog-ng-config-systemd-service.patch;striplevel=3 \
	file://syslog-ng-conf-replace-match-with-message.patch;striplevel=3 \ 
	"


do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/${DSTSUFX0}/files/LICENSE ${S}/stx-configfiles-LICENSE
    cp -pf ${WORKDIR}/${DSTSUFX0}/files/syslog-ng.conf ${S}/syslog-ng.conf
    cp -pf ${WORKDIR}/${DSTSUFX0}/files/syslog-ng.service ${S}/syslog-ng.service
    cp -pf ${WORKDIR}/${DSTSUFX0}/files/syslog-ng.logrotate ${S}/syslog-ng.logortate
    cp -pf ${WORKDIR}/${DSTSUFX0}/files/remotelogging.conf ${S}/remotelogging.conf
    cp -pf ${WORKDIR}/${DSTSUFX0}/files//fm_event_syslogger ${S}/fm_event_syslogger
}

do_install_append () {
    rm -rf ${D}${systemd_unitdir}/system/multi-user.target.wants

        chmod 644 ${D}/${sysconfdir}/syslog-ng/syslog-ng.conf

    install -D -m644 ${S}/remotelogging.conf ${D}/${sysconfdir}/syslog-ng/remotelogging.conf
    install -D -m700 ${S}/fm_event_syslogger ${D}/${sbindir}/fm_event_syslogger


    install -D -m700 ${S}/fm_event_syslogger ${D}/${sbindir}/fm_event_syslogger
    install -D -m644 ${S}/syslog-ng.logrotate ${D}/${sysconfdir}/logrotate.d/syslog
    install -D -m644 ${S}/remotelogging.conf ${D}/${sysconfdir}/syslog-ng/remotelogging.conf
    install -D -m644 ${S}/syslog-ng.conf ${D}/${sysconfdir}/syslog-ng/syslog-ng.conf

    # install -D -m644 ${S}/syslog-ng.service ${D}/${sysconfdir}/systemd/system/syslog-ng.service
    install -D -m644 ${S}/syslog-ng.service  ${D}/${systemd_system_unitdir}/syslog-ng.service
	# Fix the config version to avoid warning
    sed -i -e 's/\(@version: \).*/\1 3.19/' ${D}${sysconfdir}/syslog-ng/syslog-ng.conf
    # Workaround: comment out the udp source to aviod the service fail to start at boot time
    sed -i -e 's/\(.*s_udp.*\)/#\1/' ${D}/${sysconfdir}/syslog-ng/syslog-ng.conf
	# And replace default unit file with stx specific service file
	rm -f ${D}/${systemd_system_unitdir}/syslog-ng@.service

}

# SYSTEMD_PACKAGES_append = "${PN}"
SYSTEMD_SERVICE_${PN} = "syslog-ng.service"
SYSTEMD_AUTO_ENABLE = "enable"
RDEPENDS_${PN}_append = " bash"
