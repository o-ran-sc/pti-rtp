SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "rsync-config"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = " & Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;beginline=1;endline=10;md5=0b819b48e21c87ba7f5d0502e304af61 \
	"
SRC_URI += " \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
	"

inherit systemd
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "rsync.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/${DSTSUFX0}/centos/rsync-config.spec ${S}/stx-configfiles-LICENSE
}


do_install_append_class-target() {
    install -p -D -m 644 ${S}/packaging/systemd/rsync.service ${D}/${systemd_system_unitdir}/rsync.service
    install -m 644 ${WORKDIR}/${DSTSUFX0}/files/rsyncd.conf  ${D}/${sysconfdir}/rsyncd.conf
}

FILES_${PN}_append = " ${systemd_system_unitdir}"

	
