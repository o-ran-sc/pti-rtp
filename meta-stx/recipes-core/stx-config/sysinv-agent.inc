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

PACKAGES += " sysinv-agent"

RDEPENDS_sysinv-agent += " python"


do_configure_append() {
	:
} 

do_compile_append() {
		:
}

do_install_append() {

	echo "Installing sysinv..."
	cd ${S}/sysinv/sysinv-agent

	install -d -m 755 ${D}${sysconfdir}/init.d
	install -p -D -m 755 ${S}/sysinv/sysinv-agent/sysinv-agent ${D}/${sysconfdir}/init.d/sysinv-agent

	install -d -m 755 ${D}${sysconfdir}/pmon.d
	install -p -D -m 644 ${S}/sysinv/sysinv-agent/sysinv-agent.conf ${D}/${sysconfdir}/pmon.d/sysinv-agent.conf
	install -p -D -m 644 ${S}/sysinv/sysinv-agent/sysinv-agent.service ${D}/${systemd_system_unitdir}/sysinv-agent.service

}

FILES_sysinv-agent = " \
	${systemd_system_unitdir}/sysinv-agent.service  \
	${sysconfdir}/pmon.d/sysinv-agent.conf \
	${sysconfdir}/init.d/sysinv-agent \
	"

SYSTEMD_PACKAGES += "sysinv-agent"
SYSTEMD_SERVICE_sysinv-agent = "sysinv-agent.service"
SYSTEMD_AUTO_ENABLE_sysinv-agent = "disable"
