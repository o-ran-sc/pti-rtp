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

RDEPENDS_platform-util_append = " bash"

do_configure_append() {
	distutils_do_configure
}

do_compile_append() {
	cd ${S}/utilities/platform-util/platform-util
	distutils_do_compile
}

do_install_append() {
	cd ${S}/utilities/platform-util/platform-util
	distutils_do_install

	install -d ${D}/${bindir}
	install ${S}/utilities/platform-util/scripts/cgcs_tc_setup.sh ${D}/${bindir}
	install ${S}/utilities/platform-util/scripts/remotelogging_tc_setup.sh ${D}/${bindir}
	install ${S}/utilities/platform-util/scripts/connectivity_test ${D}/${bindir}

	install -d ${D}/${sysconfdir}/init.d
	install ${S}/utilities/platform-util/scripts/log_functions.sh ${D}/${sysconfdir}/init.d

	install -d ${D}/${sbindir}
	install -m 700 -p -D ${S}/utilities/platform-util/scripts/patch-restart-mtce ${D}/${sbindir}
	install -m 700 -p -D ${S}/utilities/platform-util/scripts/patch-restart-processes ${D}/${sbindir}
	install -m 700 -p -D ${S}/utilities/platform-util/scripts/patch-restart-haproxy ${D}/${sbindir}

	install -d ${D}/${systemd_system_unitdir}
	install -m 644 -p -D ${S}/utilities/platform-util/scripts/opt-platform.mount ${D}/${systemd_system_unitdir} 
	install -m 644 -p -D ${S}/utilities/platform-util/scripts/opt-platform.service  ${D}/${systemd_system_unitdir}
}

FILES_platform-util = "\
	${libdir}/python2.7/site-packages/platform_util \
	${libdir}/python2.7/site-packages/platform_util-1.0.0-py2.7.egg-info \
	${bindir}/cgcs_tc_setup.sh  \
	${bindir}/remotelogging_tc_setup.sh  \
	${bindir}/connectivity_test  \
	${bindir}/verify-license \
	${sysconfdir}/init.d/log_functions.sh  \
	${sbindir}/patch-restart-mtce \
	${sbindir}/patch-restart-processes \
	${sbindir}/patch-restart-haproxy \
	${systemd_system_unitdir}/opt-platform.mount \
	${systemd_system_unitdir}/opt-platform.service  \
	"
