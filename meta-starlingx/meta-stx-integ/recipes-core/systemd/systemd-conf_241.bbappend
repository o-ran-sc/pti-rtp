FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "systemd-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

do_install_append () {
	install -d -m 0755 ${D}/${sysconfdir}/systemd
	install -m644 ${STX_METADATA_PATH}/files/journald.conf ${D}/${sysconfdir}/systemd/journald.conf
	chmod 644 ${D}/${sysconfdir}/systemd/journald.conf
}
