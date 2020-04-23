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

PACKAGES += " platform-util"

RDEPENDS_platform-util  += " bash"
DESCRIPTION_platform-util  = "platform-util"
SUMMARY_platform-util  = "StarlingX Platform utilities installed only on controllers"

inherit setuptools distutils

do_configure_append() {
	cd ${S}/utilities/platform-util/platform-util
	distutils_do_configure
}
do_compile_append() {
	cd ${S}/utilities/platform-util/platform-util
	distutils_do_configure
}

do_install_append() {
	cd ${S}/utilities/platform-util/platform-util
	distutils_do_install

	cd ${S}/utilities/platform-util/scripts

	install -d -m0755 ${D}/${bindir}
	install -m0755 tc_setup.sh ${D}/${bindir}/tc_setup.sh
	install -m0755 remotelogging_tc_setup.sh ${D}/${bindir}/remotelogging_tc_setup.sh
	install -m0755 connectivity_test  ${D}/${bindir}/connectivity_test
	install -m0755 update-iso.sh ${D}/${bindir}/update-iso.sh

	install -p -d -m0755 ${D}/${sysconfdir}/init.d
	install -m0755 log_functions.sh ${D}/${sysconfdir}/init.d/log_functions.sh

	install -p -d -m0755 ${D}/${sbindir}
	install -m0755 patch-restart-mtce  ${D}/${sbindir}/patch-restart-mtce
	install -m0755 patch-restart-processes ${D}/${sbindir}/patch-restart-processes
	install -m0755 patch-restart-haproxy ${D}/${sbindir}/patch-restart-haproxy



	install -p -d -m0755 ${D}/${systemd_system_unitdir}

	install -m0644 opt-platform.mount  ${D}/${systemd_system_unitdir}/opt-platform.mount
	install -m0644 opt-platform.service ${D}/${systemd_system_unitdir}/opt-platform.service

}

FILES_platform-util = "  \
	${bindir}/tc_setup.sh \
	${bindir}/verify-license \
	${bindir}/remotelogging_tc_setup.sh \
	${bindir}/connectivity_test \
	${bindir}/update-iso.sh \
	${sysconfdir}/init.d/log_functions.sh \
	${sbindir}/patch-restart-mtce \
	${sbindir}/patch-restart-processes \
	${sbindir}/patch-restart-haproxy \
	${systemd_system_unitdir}/opt-platform.mount \
	${systemd_system_unitdir}/opt-platform.service \
	${PYTHON_SITEPACKAGES_DIR}/platform_util/ \
	${PYTHON_SITEPACKAGES_DIR}/platform_util-${PV}-py${PYTHON_BASEVERSION}.egg-info/ \
	"
