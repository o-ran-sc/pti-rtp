SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
LICENSE_append = " & Apache-2.0"
SUBPATH0 = "rabbitmq-server-config"
DSTSUFX0 = "stx-configfiles"

LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;beginline=1;endline=10;md5=47a43f492f496b985b830ce47b8c5cec \
	"
SRC_URI += " \
    git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
    "

do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/stx-configfiles/centos/rabbitmq-server-config.spec ${S}/stx-configfiles-LICENSE
}

do_install_append () {

    # Libdir here is hardcoded in other scripts.
    install -d ${D}/usr/lib/ocf/resource.d/rabbitmq
    install -d ${D}/${sysconfdir}/systemd/system
    install -d ${D}/${sysconfdir}/logrotate.d

    install -m 0755 ${WORKDIR}/stx-configfiles/files/rabbitmq-server.ocf  \
        ${D}/usr/lib/ocf/resource.d/rabbitmq/stx.rabbitmq-server
		
    install -m 0644 ${WORKDIR}/stx-configfiles/files/rabbitmq-server.service.example  \
         ${D}/${sysconfdir}/systemd/system/rabbitmq-server.service
    sed -i -e 's/notify/simple/' ${D}/${sysconfdir}/systemd/system/rabbitmq-server.service 
    # Remove lib/systemd/ 
    rm -rf ${D}/${nonarch_base_libdir}
	 
    install -m 0644 ${WORKDIR}/stx-configfiles/files/rabbitmq-server.logrotate  \
         ${D}/${sysconfdir}/logrotate.d/rabbitmq-server

}
