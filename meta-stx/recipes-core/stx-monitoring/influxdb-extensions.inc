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

PACKAGES += " influxdb-extensions"

RDEPENDS_influxdb-extensions += " \
	influxdb \
	systemd \
	python-influxdb \
	"

do_configure_append () {
	:
} 

do_compile_append() {
	:
}

local_unit_dir = "${sysconfdir}/systemd/system"

do_install_append() {

	cd ${S}/influxdb-extensions/src
	install -m 755 -d ${D}/${sysconfdir}
	install -m 755 -d ${D}/${local_unit_dir}
	install -m 755 -d ${D}/${sysconfdir}/influxdb

	# support files ; service and pmon conf
	install -m 644 influxdb.service  ${D}/${local_unit_dir}
	install -m 600 influxdb.conf.pmon  ${D}/${sysconfdir}/influxdb
}

FILES_influxdb-extensions = " \
	${local_unit_dir}/influxdb.service \
	${sysconfdir}/influxdb \
	"
