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

PACKAGES += " controllerconfig"

RDEPENDS_controllerconfig += " \
	bash \
	fm-api \
	systemd \
	tsconfig \
	python-iso8601 \
	python-keyring \
	python-netaddr \
	python-netifaces \
	python-pyudev \
	python-six \
	python-cryptography \
	python-oslo.utils \
	python-pysnmp \
	python-ruamel.yaml \
	"

do_configure_prepend() {
	cd ${S}/controllerconfig/controllerconfig
	distutils_do_configure
} 

do_compile_prepend() {
	cd ${S}/controllerconfig/controllerconfig
	distutils_do_compile
}

do_install_prepend () {
	cd ${S}/controllerconfig/controllerconfig
	distutils_do_install

        install -p -D -m 700 scripts/keyringstaging ${D}/${bindir}
        install -p -D -m 700 scripts/openstack_update_admin_password ${D}/${bindir}
        install -p -D -m 700 scripts/install_clone.py ${D}/${bindir}
        install -p -D -m 700 scripts/finish_install_clone.sh ${D}/${bindir}

	install -d -m 700 ${D}/${sysconfdir}/goenabled.d
	install -d -m 700 ${D}/${sysconfdir}/init.d
        install -p -D -m 700 scripts/config_goenabled_check.sh ${D}/${sysconfdir}/goenabled.d
        install -p -D -m 755 scripts/controller_config ${D}/${sysconfdir}/init.d/controller_config

        ## Install Upgrade scripts
	install -d -m 755 ${D}/${sysconfdir}/upgrade.d
        install -p -m 755 upgrade-scripts/16-neutron-move-bindings-off-controller-1.py ${D}/${sysconfdir}/upgrade.d
        install -p -m 755 upgrade-scripts/20-sysinv-retire-ceph-cache-tier-sp.py ${D}/${sysconfdir}/upgrade.d
	

        install -p -D -m 664 scripts/controllerconfig.service ${D}/${sysconfdir}/systemd/system/controllerconfig.service

	sed -i -e 's|/usr/local/bin|${bindir}|' \
		${D}${libdir}/python2.7/site-packages/controllerconfig/utils.py \
		${D}${libdir}/python2.7/site-packages/controllerconfig/upgrades/utils.py \
		${D}${sysconfdir}/init.d/controller_config
}


FILES_controllerconfig = " \
		${sysconfdir}/goenabled.d/config_goenabled_check.sh  \
		${sysconfdir}/upgrade.d/20-sysinv-retire-ceph-cache-tier-sp.py \
		${sysconfdir}/upgrade.d/16-neutron-move-bindings-off-controller-1.py \
		${sysconfdir}/init.d/controller_config \
		${bindir}/keysringstaging \
        	${bindir}/openstack_update_admin_password \
        	${bindir}/install_clone.py \
        	${bindir}/finish_install_clone.sh \
        	${bindir}/finish_install_clone.sh \
		${bindir}/upgrade_controller \
		${bindir}/config_region \
		${bindir}/config_subcloud \
		${bindir}/config_management \
		${bindir}/keyringstaging \
		${bindir}/tidy_storage_post_restore \
		${bindir}/config_controller \
		${bindir}/upgrade_controller_simplex \
		${sysconfdir}/systemd/system/controllerconfig.service \
		${libdir}/python2.7/site-packages/controllerconfig*.egg-info/ \
		${libdir}/python2.7/site-packages/controllerconfig/ \
		"

SYSTEMD_PACKAGES += "controllerconfig"
SYSTEMD_SERVICE_controllerconfig = "controllerconfig.service"
SYSTEMD_AUTO_ENABLE_controllerconfig = "enable"
