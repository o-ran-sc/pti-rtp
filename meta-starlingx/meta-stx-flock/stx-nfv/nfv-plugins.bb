require nfv-common.inc

SUBPATH0 = "nfv/nfv-plugins"

inherit setuptools

do_install_append () {

	install -d -m 755 ${D}/${sysconfdir}/nfv/
	install -d -m 755 ${D}/${sysconfdir}/nfv/nfv_plugins/
	install -d -m 755 ${D}/${sysconfdir}/nfv/nfv_plugins/alarm_handlers/

	install -p -D -m 600 nfv_plugins/alarm_handlers/config.ini \
			${D}/${sysconfdir}/nfv/nfv_plugins/alarm_handlers/config.ini

	install -d -m 755 ${D}/${sysconfdir}/nfv/nfv_plugins/event_log_handlers/

	install -p -D -m 600 nfv_plugins/event_log_handlers/config.ini \
			${D}/${sysconfdir}/nfv/nfv_plugins/event_log_handlers/config.ini
			\
	install -d -m 755 ${D}/${sysconfdir}/nfv/nfv_plugins/nfvi_plugins/

	install -p -D -m 600 nfv_plugins/nfvi_plugins/config.ini \
			${D}/${sysconfdir}/nfv/nfv_plugins/nfvi_plugins/config.ini
					
	install -d -m 755 ${D}/
	install -p -D -m 644 scripts/nfvi-plugins.logrotate \
			${D}/${sysconfdir}/logrotate.d/nfvi-plugins.logrotate
	
}
