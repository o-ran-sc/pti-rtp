FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "ntp-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

do_install_append () {
        install -D -m644 ${STX_METADATA_PATH}/files/ntpd.sysconfig ${D}/${sysconfdir}/sysconfig/ntpd
        install -D -m644 ${STX_METADATA_PATH}/files/ntp.conf ${D}/${sysconfdir}/ntp.conf
}

SYSTEMD_AUTO_ENABLE = "disable"
RDEPENDS_${PN}_append = " bash"

FILES_${PN}_append = " ${sysconfdir}/sysconfig/ntpd"
