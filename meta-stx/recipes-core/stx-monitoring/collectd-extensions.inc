#
## Copyright (C) 2019 Wind River Systems, Inc.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

PACKAGES += " collectd-extensions"

RDEPENDS_collectd-extensions += " \
	systemd \
	collectd \
	fm-api \
	python-influxdb \
	python-oslo.concurrency \
	python-httplib2 \
	tsconfig \
	"

do_configure_append () {
	:
} 

do_compile_append() {
	:
}

local_unit_dir = "${sysconfdir}/systemd/system"
local_plugin_dir = "${sysconfdir}/collectd.d"
local_python_extensions_dir = "/opt/collectd/extensions/python"
local_config_extensions_dir = "/opt/collectd/extensions/config"


do_install_append() {

	cd ${S}/collectd-extensions/src
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

FILES_collectd-extensions = " \
	${local_unit_dir}/collectd.service \
	${local_config_extensions_dir}/collectd.conf.pmon \
	${local_python_extensions_dir}/fm_notifier.py \
	${local_python_extensions_dir}/plugin_common.py \
	${local_python_extensions_dir}/cpu.py \
	${local_python_extensions_dir}/memory.py \
	${local_python_extensions_dir}/example.py \
	${local_python_extensions_dir}/ntpq.py \
	${local_python_extensions_dir}/interface.py \
	${local_python_extensions_dir}/remotels.py \
	${local_python_extensions_dir}/ptp.py \
	${local_python_extensions_dir}/ovs_interface.py \
	${local_plugin_dir}/python_plugins.conf \
	${local_plugin_dir}/cpu.conf \
	${local_plugin_dir}/memory.conf \
	${local_plugin_dir}/df.conf \
	${local_plugin_dir}/example.conf \
	${local_plugin_dir}/ntpq.conf \
	${local_plugin_dir}/interface.conf \
	${local_plugin_dir}/remotels.conf \
	${local_plugin_dir}/ptp.conf \
	${local_plugin_dir}/ovs_interface.conf \
	"
