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

PACKAGES += " inventory"

#DEPENDS = " \
#	python \
#	python-pbr-native \
#	"

#python-futurist >= 0.11.0
#python-keystoneauth1 >= 3.1.0
#python-keystonemiddleware >= 4.12.0
#python-neutronclient >= 6.3.0
#python-oslo-concurrency >= 3.8.0
#python-oslo-config >= 2:4.0.0
#python-oslo-context >= 2.14.0
#python-oslo-db >= 4.24.0
#python-oslo-i18n >= 2.1.0
#python-oslo-log >= 3.22.0
#python-oslo-messaging >= 5.24.2
#python-oslo-middleware >= 3.27.0
#python-oslo-policy >= 1.23.0
#python-oslo-rootwrap >= 5.0.0
#python-oslo-serialization >= 1.10.0
#python-oslo-service >= 1.10.0
#python-oslo-utils >= 3.20.0
#python-oslo-versionedobjects >= 1.17.0
#python-osprofiler >= 1.4.0
#python-stevedore >= 1.20.0
#python-webob >= 1.7.1

RDEPENDS_inventory += " \
		bash \
		python-anyjson \
		python-amqplib \
		python-pyudev \
		python-pyparted \
		python-ipaddr \
		python-paste \
		python-eventlet \
		python-futurist \
		python-jsonpatch \
		python-keystoneauth1 \
		python-keystonemiddleware \
		python-neutronclient \
		python-oslo.concurrency \
		python-oslo.config \
		python-oslo.context \
		python-oslo.db \
		python-oslo.i18n \
		python-oslo.log \
		python-oslo.messaging \
		python-oslo.middleware \
		python-oslo.policy \
		python-oslo.rootwrap \
		python-oslo.serialization \
		python-oslo.service \
		python-oslo.utils \
		python-oslo.versionedobjects \
		python-osprofiler \
		python-pbr \
		python-pecan \
		python-psutil \
		python-requests \
		python-retrying \
		python-six \
		python-sqlalchemy \
		python-stevedore \
		python-webob \
		python-wsme \
		"



do_configure_prepend () {
	cd ${S}/inventory/inventory
	distutils_do_configure
} 

do_compile_prepend () {
	cd ${S}/inventory/inventory
	distutils_do_compile
}

do_install_prepend () {
	cd ${S}/inventory/inventory
	distutils_do_install
	
	install -d -m 755 ${D}/${sysconfdir}/goenabled.d
	install -p -D -m 755 etc/inventory/inventory_goenabled_check.sh ${D}/${sysconfdir}/goenabled.d/inventory_goenabled_check.sh

	install -d -m 755 ${D}/${sysconfdir}/inventory
	install -p -D -m 755 etc/inventory/policy.json ${D}/${sysconfdir}/inventory/policy.json

	install -d -m 755 ${D}/${sysconfdir}/motd.d
	install -p -D -m 755 etc/inventory/motd-system ${D}/${sysconfdir}/motd.d/10-system-config

	install -m 755 -p -D scripts/inventory-api ${D}/${libdir}/ocf/resource.d/platform/inventory-api
	install -m 755 -p -D scripts/inventory-conductor ${D}/${libdir}/ocf/resource.d/platform/inventory-conductor

	install -d -m 0755 ${D}/${systemd_system_unitdir}/
	install -m 644 -p -D scripts/inventory-api.service ${D}/${systemd_system_unitdir}/
	install -m 644 -p -D scripts/inventory-conductor.service ${D}/${systemd_system_unitdir}/

	# Install sql migration
	# install -m 644 inventory/db/sqlalchemy/migrate_repo/migrate.cfg ${D}/${libdir}/inventory/db/sqlalchemy/migrate_repo/migrate.cfg

}

#pkg_postinst_ontarget-inventory () {
# install default config files
#cd ${_builddir}/${name}-${version} && oslo-config-generator --config-file inventory/config-generator.conf --output-file ${_builddir}/${name}-${version}/inventory.conf.sample
#}

FILES_inventory = " \
	${systemd_system_unitdir}/inventory-api.service \
	${systemd_system_unitdir}/inventory-conductor.service \
	${bindir}/inventory-api \
	${bindir}/inventory-conductor \
	${bindir}/inventory-dnsmasq-lease-update \
	${bindir}/inventory-agent \
	${bindir}/inventory-dbsync \
	${libdir}/ocf/resource.d/platform/inventory-api \
	${libdir}/ocf/resource.d/platform/inventory-conductor \
	${libdir}/python2.7/site-packages/inventory*.egg-info/ \
	${libdir}/python2.7/site-packages/inventory/ \
	${sysconfdir}/goenabled.d/inventory_goenabled_check.sh \
	${sysconfdir}/motd.d/10-system-config \
	${sysconfdir}/inventory/policy.json \
	"
