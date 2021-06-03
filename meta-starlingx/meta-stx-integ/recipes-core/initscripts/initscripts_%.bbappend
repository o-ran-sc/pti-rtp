FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "initscripts-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += " \
	file://${STX_METADATA_PATH}/centos/initscripts-config.spec;beginline=1;endline=10;md5=5c43895c2c3756125227c74209b8b791 \
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

    install -m  644 ${STX_METADATA_PATH}/files/sysconfig-network.conf ${D}/${sysconfdir}/sysconfig/network
    install -m  755 ${STX_METADATA_PATH}/files/mountnfs.sh ${D}/${sysconfdir}/init.d/mountnfs
    install -m  644 ${STX_METADATA_PATH}/files/mountnfs.service ${D}/${systemd_system_unitdir}/mountnfs.service
}
