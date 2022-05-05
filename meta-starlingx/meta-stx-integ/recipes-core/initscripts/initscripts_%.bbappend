FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "initscripts-config"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += " \
	file://stx-configfiles/centos/initscripts-config.spec;beginline=1;endline=10;md5=5c43895c2c3756125227c74209b8b791 \
	"

SRC_URI += " \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
	"

inherit systemd
SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_SERVICE_${PN}_append = "mountnfs.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

do_install_append () {
    install -d  -m 755 ${D}/${sysconfdir}/sysconfig
    install -d  -m 755 ${D}/${sysconfdir}/init.d
    install -d  -m 755 ${D}/${systemd_system_unitdir}

    install -m  644 ${WORKDIR}/${DSTSUFX0}/files/sysconfig-network.conf ${D}/${sysconfdir}/sysconfig/network
    install -m  755 ${WORKDIR}/${DSTSUFX0}/files/mountnfs.sh ${D}/${sysconfdir}/init.d/mountnfs
    install -m  644 ${WORKDIR}/${DSTSUFX0}/files/mountnfs.service ${D}/${systemd_system_unitdir}/mountnfs.service
}
