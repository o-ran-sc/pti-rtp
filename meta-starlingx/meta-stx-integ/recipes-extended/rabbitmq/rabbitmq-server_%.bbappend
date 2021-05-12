inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "rabbitmq-server-config"

LICENSE_append = " & Apache-2.0"

LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/centos/rabbitmq-server-config.spec;beginline=1;endline=10;md5=47a43f492f496b985b830ce47b8c5cec \
	"

do_install_append () {

    # Libdir here is hardcoded in other scripts.
    install -d ${D}/usr/lib/ocf/resource.d/rabbitmq
    install -d ${D}/${sysconfdir}/systemd/system
    install -d ${D}/${sysconfdir}/logrotate.d

    install -m 0755 ${STX_METADATA_PATH}/files/rabbitmq-server.ocf  \
        ${D}/usr/lib/ocf/resource.d/rabbitmq/stx.rabbitmq-server
		
    install -m 0644 ${STX_METADATA_PATH}/files/rabbitmq-server.service.example  \
         ${D}/${sysconfdir}/systemd/system/rabbitmq-server.service
    sed -i -e 's/notify/simple/' ${D}/${sysconfdir}/systemd/system/rabbitmq-server.service 
    # Remove lib/systemd/ 
    rm -rf ${D}/${nonarch_base_libdir}
	 
    install -m 0644 ${STX_METADATA_PATH}/files/rabbitmq-server.logrotate  \
         ${D}/${sysconfdir}/logrotate.d/rabbitmq-server

}
