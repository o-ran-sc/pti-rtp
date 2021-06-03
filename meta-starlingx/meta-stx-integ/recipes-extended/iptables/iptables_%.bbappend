inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "iptables-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

inherit systemd
SYSTEMD_PACKAGES += "${PN}"
SYSETMD_SERVICE_${PN}_append = "iptables.service ip6tables.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

do_install_append() {
    install -d -m0755 ${D}/${sysconfdir}/sysconfig
    install -m 600 ${STX_METADATA_PATH}/files/iptables.rules ${D}/${sysconfdir}/sysconfig/iptables
    install -m 600 ${STX_METADATA_PATH}/files/ip6tables.rules ${D}/${sysconfdir}/sysconfig/ip6tables
}
