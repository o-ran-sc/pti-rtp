require config-common.inc

SUBPATH0 = "controllerconfig/controllerconfig"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN}_append = " \
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

inherit setuptools python-dir systemd
SYSTEMD_PACKAGES += "controllerconfig"
SYSTEMD_SERVICE_controllerconfig = "controllerconfig.service"
SYSTEMD_AUTO_ENABLE_controllerconfig = "enable"

do_install_append() {

    install -p -D -m 700 scripts/keyringstaging ${D}/${bindir}
    install -p -D -m 700 scripts/openstack_update_admin_password ${D}/${bindir}
    install -p -D -m 700 scripts/install_clone.py ${D}/${bindir}
    install -p -D -m 700 scripts/finish_install_clone.sh ${D}/${bindir}

    install -d -m 755 ${D}/${sysconfdir}/goenabled.d
    install -d -m 755 ${D}/${sysconfdir}/init.d
    install -p -D -m 700 scripts/config_goenabled_check.sh ${D}/${sysconfdir}/goenabled.d
    install -p -D -m 755 scripts/controller_config ${D}/${sysconfdir}/init.d/controller_config

    ## Install Upgrade scripts
    install -d -m 755 ${D}/${sysconfdir}/upgrade.d
    install -p -m 755 upgrade-scripts/16-neutron-move-bindings-off-controller-1.py ${D}/${sysconfdir}/upgrade.d
    install -p -m 755 upgrade-scripts/20-sysinv-retire-ceph-cache-tier-sp.py ${D}/${sysconfdir}/upgrade.d


    install -p -D -m 664 scripts/controllerconfig.service ${D}/${sysconfdir}/systemd/system/controllerconfig.service
    sed -i -e 's/network.target/networking.target/g'  \
		${D}/${sysconfdir}/systemd/system/controllerconfig.service

    sed -i -e 's|/usr/local/bin|${bindir}|' \
	${D}${libdir}/python2.7/site-packages/controllerconfig/utils.py \
	${D}${libdir}/python2.7/site-packages/controllerconfig/upgrades/utils.py \
	${D}${sysconfdir}/init.d/controller_config
}
