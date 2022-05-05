require nfv-common.inc
SUBPATH0 = "nova-api-proxy/nova-api-proxy"

inherit setuptools systemd
SYSTEMD_PACKAGES += "nova-api-proxy"
SYSTEMD_SERVICE_${PN} = "api-proxy.service"

do_install_append () {

	install -d -m 755 ${D}/${systemd_system_unitdir}
	install -p -D -m 644 nova_api_proxy/scripts/api-proxy.service ${D}/${systemd_system_unitdir}/api-proxy.service
	install -d -m 755 ${D}/${sysconfdir}/rc.d/init.d
	install -p -D -m 755 nova_api_proxy/scripts/api-proxy ${D}/${sysconfdir}/rc.d/init.d/api-proxy

	install -d -m 755 ${D}/${sysconfdir}/proxy
	install -p -D -m 700 nova_api_proxy/nova-api-proxy.conf ${D}${sysconfdir}/proxy/nova-api-proxy.conf
	install -p -D -m 700 nova_api_proxy/api-proxy-paste.ini ${D}${sysconfdir}/proxy/api-proxy-paste.ini
	

}
