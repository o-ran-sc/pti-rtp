require monitoring-common.inc

SUBPATH0 = "collectd-extensions/src"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN}_append += " \
	systemd \
	collectd \
	fm-api \
	ntpq \
	python-influxdb \
	python-oslo.concurrency \
	python-httplib2 \
	tsconfig \
	"


local_unit_dir = "${sysconfdir}/systemd/system"
local_plugin_dir = "${sysconfdir}/collectd.d"
local_python_extensions_dir = "/opt/collectd/extensions/python"
local_config_extensions_dir = "/opt/collectd/extensions/config"


do_install_append() {

	install -m 755 -d ${D}/${sysconfdir}
	install -m 755 -d ${D}/${local_unit_dir}
	install -m 755 -d ${D}/${local_plugin_dir}
	install -m 755 -d ${D}/${local_config_extensions_dir}
	install -m 755 -d ${D}/${local_python_extensions_dir}

	# support files ; service and pmon conf
	install -m 644 collectd.service  ${D}/${local_unit_dir}
	install -m 600 collectd.conf.pmon  ${D}/${local_config_extensions_dir}

	# collectd python plugin files - notifiers
	install -m 700  fm_notifier.py ${D}/${local_python_extensions_dir}
	install -m 700 plugin_common.py ${D}/${local_python_extensions_dir}

	# collectd python plugin files - resource plugins
	install -m 700 cpu.py  ${D}/${local_python_extensions_dir}
	install -m 700 memory.py  ${D}/${local_python_extensions_dir}
	install -m 700 example.py  ${D}/${local_python_extensions_dir}
	install -m 700 ntpq.py  ${D}/${local_python_extensions_dir}
	install -m 700 interface.py ${D}/${local_python_extensions_dir}
	install -m 700 remotels.py  ${D}/${local_python_extensions_dir}
	install -m 700 ptp.py  ${D}/${local_python_extensions_dir}
	install -m 700 ovs_interface.py  ${D}/${local_python_extensions_dir}


	# collectd plugin conf files into /etc/collectd.d
	install -m 600 python_plugins.conf  ${D}/${local_plugin_dir}
	install -m 600 cpu.conf  ${D}/${local_plugin_dir}
	install -m 600 memory.conf  ${D}/${local_plugin_dir}
	install -m 600 df.conf  ${D}/${local_plugin_dir}
	install -m 600 example.conf  ${D}/${local_plugin_dir}
	install -m 600 ntpq.conf  ${D}/${local_plugin_dir}
	install -m 600 interface.conf  ${D}/${local_plugin_dir}
	install -m 600 remotels.conf  ${D}/${local_plugin_dir}
	install -m 600 ptp.conf  ${D}/${local_plugin_dir}
	install -m 600 ovs_interface.conf  ${D}/${local_plugin_dir}

}

FILES_${PN}_append = " \
	${local_python_extensions_dir} \
	${local_config_extensions_dir} \
	"
