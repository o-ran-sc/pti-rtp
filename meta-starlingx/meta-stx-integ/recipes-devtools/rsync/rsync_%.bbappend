inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "rsync-config"

LICENSE_append = " & Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/centos/rsync-config.spec;beginline=1;endline=10;md5=0b819b48e21c87ba7f5d0502e304af61 \
	"

inherit systemd
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "rsync.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install_append_class-target() {
    install -p -D -m 644 ${S}/packaging/systemd/rsync.service ${D}/${systemd_system_unitdir}/rsync.service
    install -m 644 ${STX_METADATA_PATH}/files/rsyncd.conf  ${D}/${sysconfdir}/rsyncd.conf
}

FILES_${PN}_append = " ${systemd_system_unitdir}"
