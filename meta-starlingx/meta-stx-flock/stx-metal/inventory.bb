require metal-common.inc

SUBPATH0 = "inventory/inventory/"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRC_URI += "file://0001-inventory-Remove-argparse-requirement.patch"

RDEPENDS_${PN} += " \
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

DEPENDS += " \
	python-pbr-native \
	"

inherit systemd
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "inventory-api.service inventory-conductor.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

inherit setuptools python-dir


do_install_append () {
	
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
	install -m 644 inventory/db/sqlalchemy/migrate_repo/migrate.cfg \
		${D}/${PYTHON_SITEPACKAGES_DIR}/inventory/db/sqlalchemy/migrate_repo/migrate.cfg

}

FILES_${PN}_append = " \
	${libdir}/ocf/resource.d/platform/inventory-api \
	${libdir}/ocf/resource.d/platform/inventory-conductor \
	"
