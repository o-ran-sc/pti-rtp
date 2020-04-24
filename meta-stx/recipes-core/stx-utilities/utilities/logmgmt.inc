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

PACKAGES += " logmgmt"

RDEPENDS_logmgmt += " \
	systemd \
	python-daemon \
	"
DESCRIPTION_logmgmt = "Management of /var/log filesystem"

do_configure_append() {
	cd ${S}/utilities/logmgmt/logmgmt/
	distutils_do_configure
}
do_compile_append() {
	cd ${S}/utilities/logmgmt/logmgmt/
	distutils_do_compile
}

do_install_append() {
	cd ${S}/utilities/logmgmt/logmgmt/
	distutils_do_install

	cd ${S}/utilities/logmgmt/scripts
	install -d -m0755 ${D}/${bindir}
	install -m0700 bin/logmgmt ${D}/${bindir}
	install -m0700 bin/logmgmt_postrotate ${D}/${bindir}
	install -m0700 bin/logmgmt_prerotate ${D}/${bindir}

	install -d -m0755 ${D}/${sysconfdir}/init.d
	install -m0700 init.d/logmgmt ${D}/${sysconfdir}/init.d

	install -d -m0755 ${D}/${sysconfdir}/pmon.d
	install -m0700 pmon.d/logmgmt ${D}/${sysconfdir}/pmon.d

	install -d -m0755 ${D}/${systemd_system_unitdir}
	install -m0664 etc/systemd/system/logmgmt.service ${D}/${systemd_system_unitdir}
}

FILES_logmgmt = "  \
	${bindir}/logmgmt \
	${bindir}/logmgmt_postrotate \
	${bindir}/logmgmt_prerotate \
	${sysconfdir}/init.d/logmgmt \
	${sysconfdir}/pmon.d/logmgmt \
	${systemd_system_unitdir}/logmgmt.service \
	${PYTHON_SITEPACKAGES_DIR}/logmgmt/ \
	${PYTHON_SITEPACKAGES_DIR}/logmgmt-${PV}-py${PYTHON_BASEVERSION}.egg-info/ \
	"

SYSTEMD_PACKAGES += "logmgmt"
SYSTEMD_SERVICE_logmgmt = "logmgmt.service"
SYSTEMD_AUTO_ENABLE_logmgmt = "enable"
