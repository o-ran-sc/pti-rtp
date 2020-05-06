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

PACKAGES += " cgcs-patch"
PACKAGES += " cgcs-patch-agent"
PACKAGES += " cgcs-patch-controller"

inherit setuptools systemd

RDEPENDS_cgcs-patch_append = " \
	bash \
	"
RDEPENDS_cgcs-patch-agent_append = " \
	bash \
	python \
	"

RDEPENDS_cgcs-patch-controller_append = " \
	bash \
	python-requests-toolbelt \
	createrepo-c \
	"

do_configure_append () {
	cd ${S}/cgcs-patch/cgcs-patch
	distutils_do_configure
} 

do_compile_append () {
	cd ${S}/cgcs-patch/cgcs-patch
	distutils_do_compile
}

do_install_append () {
	cd ${S}/cgcs-patch/cgcs-patch
	distutils_do_install

	cd ${S}/cgcs-patch/bin

	install -m 755 -d ${D}/${sbindir}
	install -m 755 -d ${D}/${sysconfdir}/bash_completion.d
	install -m 755 -d ${D}/${sysconfdir}/goenabled.d
	install -m 755 -d ${D}/${sysconfdir}/init.d
	install -m 755 -d ${D}/${sysconfdir}/logrotate.d
	install -m 755 -d ${D}/${sysconfdir}/patching
	install -m 700 -d ${D}/${sysconfdir}/patching/patch-scripts
	install -m 755 -d ${D}/${sysconfdir}/pmon.d
	install -m 755 -d ${D}/${systemd_system_unitdir}

	install -m 500 sw-patch-agent  ${D}/${sbindir}/sw-patch-agent
	install -m 500 sw-patch-controller-daemon ${D}/${sbindir}/sw-patch-controller-daemon
	install -m 555 sw-patch ${D}/${sbindir}/sw-patch
	install -m 555 rpm-audit ${D}/${sbindir}/rpm-audit
	
	install -m 500 sw-patch-controller-daemon-init.sh ${D}/${sysconfdir}/init.d/sw-patch-controller-daemon
	install -m 500 sw-patch-agent-init.sh ${D}/${sysconfdir}/init.d/sw-patch-agent
	
	install -m 600 patching.conf ${D}/${sysconfdir}/patching/patching.conf
	
	install -m 644 policy.json ${D}/${sysconfdir}/patching/policy.json 
	
	install -m 444 pmon-sw-patch-controller-daemon.conf ${D}/${sysconfdir}/pmon.d/sw-patch-controller-daemon.conf
	install -m 444 pmon-sw-patch-agent.conf ${D}/${sysconfdir}/pmon.d/sw-patch-agent.conf 
	install -m 444 *.service ${D}/${systemd_system_unitdir} 
	install -m 444 sw-patch.completion ${D}/${sysconfdir}/bash_completion.d/sw-patch 
	install -m 400 patch-functions ${D}/${sysconfdir}/patching/patch-functions 

	install -D -m 444 patch-tmpdirs.conf ${D}/${sysconfdir}/tempfiles.d/patch-tmpdirs.conf

	install -m 500 run-patch-scripts ${D}/${sbindir}/run-patch-scripts 
	install -m 500 sw-patch-controller-daemon-restart ${D}/${sbindir}/sw-patch-controller-daemon-restart
	install -m 500 sw-patch-agent-restart ${D}/${sbindir}/sw-patch-agent-restart


	install -m 500 run-patch-scripts ${D}/${sbindir}/run-patch-scripts
	install -m 500 sw-patch-controller-daemon-restart ${D}/${sbindir}/sw-patch-controller-daemon-restart
	install -m 500 sw-patch-agent-restart ${D}/${sbindir}/sw-patch-agent-restart 
	install -m 500 sw-patch-init.sh ${D}/${sysconfdir}/init.d/sw-patch
	install -m 500 sw-patch-controller-init.sh ${D}/${sysconfdir}/init.d/sw-patch-controller 

	sed -i -e 's/createrepo/createrepo_c/' ${D}/${sysconfdir}/init.d/sw-patch-controller

	install -m 555 patch_check_goenabled.sh ${D}/${sysconfdir}/goenabled.d/patch_check_goenabled.sh 
	install -m 444 patching.logrotate ${D}/${sysconfdir}/logrotate.d/patching 
	
	install -m 500 upgrade-start-pkg-extract ${D}/${sbindir}/upgrade-start-pkg-extract

}

FILES_cgcs-patch = " \
	${libdir}/python2.7/site-packages/cgcs_patch \
	${libdir}/python2.7/site-packages/cgcs_patch-1.0-py2.7.egg-info \
	${libdir}/python2.7/site-packages/cgcs_make_patch \
	${libdir}/python2.7/site-packages/cgcs_patch-1.0-py2.7.egg-info/top_level.txt \
	${sbindir}/rpm-audit \
	${sysconfdir}/patching/policy.json \
	${sysconfdir}/patching/patching.conf \
	${sysconfdir}/patching/patch-scripts \
	${sysconfdir}/init.d/sw-patch \
	${systemd_system_unitdir}/sw-patch.service \
	${sysconfdir}/goenabled.d/patch_check_goenabled.sh \
	${sysconfdir}/logrotate.d/patching \
	${sysconfdir}/tempfiles.d/patch-tmpdirs.conf \
	${sysconfdir}/patching/patch-functions \
"

FILES_cgcs-patch-agent = " \
	${sbindir}/sw-patch-agent \
	${sbindir}/sw-patch-agent-restart \
	${sysconfdir}/pmon.d/sw-patch-agent.conf \
	${sbindir}/run-patch-scripts \
	${sysconfdir}/init.d/sw-patch-agent \
	${systemd_system_unitdir}/sw-patch-agent.service \
	${sysconfdir}/bash_completion.d/sw-patch \
	"

FILES_cgcs-patch-controller = " \
	${sbindir}/sw-patch-controller-daemon-restart \
	${sysconfdir}/init.d/sw-patch-controller-daemon \
	${sbindir}/sw-patch-controller-daemon \
	${sbindir}/upgrade-start-pkg-extract \
	${sysconfdir}/pmon.d/sw-patch-controller-daemon.conf \
	${systemd_system_unitdir}/sw-patch-controller-daemon.service \
	${sbindir}/sw-patch \
	${sysconfdir}/init.d/sw-patch-controller \
	${systemd_system_unitdir}/sw-patch-controller.service \
	"

SYSTEMD_PACKAGES += "cgcs-patch"
SYSTEMD_SERVICE_cgcs-patch = "sw-patch.service"
SYSTEMD_AUTO_ENABLE_cgcs-patch = "enable"

SYSTEMD_PACKAGES += "cgcs-patch-agent"
SYSTEMD_SERVICE_cgcs-patch-agent = "sw-patch-agent.service"
SYSTEMD_AUTO_ENABLE_cgcs-patch-agent = "enable"

SYSTEMD_PACKAGES += "cgcs-patch-controller"
SYSTEMD_SERVICE_cgcs-patch-controller = " \
	sw-patch-controller.service \
	sw-patch-controller-daemon.service \
	"
SYSTEMD_AUTO_ENABLE_cgcs-patch-controller = "enable"
