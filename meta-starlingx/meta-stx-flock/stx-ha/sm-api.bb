require ha-common.inc
SUBPATH0 = "service-mgmt-api/sm-api"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN}_append = " \
	bash \
	python-six \
	chkconfig \
	mtce-pmon \
	"
inherit setuptools
SYSTEMD_PACKAGES += "sm-api"
SYSTEMD_SERVICE_sm-api = "sm-api.service"
SYSTEMD_AUTO_ENABLE_sm-api = "enable"

do_install_append () {
        install -d -m 0755 ${D}/${sysconfdir}/sm
        install -d -m 0755 ${D}/${sysconfdir}/init.d
        install -d -m 0755 ${D}/${sysconfdir}/pmon.d
        install -d -m 0755 ${D}/${sysconfdir}/sm-api
        install -d -m 0755 ${D}/${systemd_system_unitdir}
        install -m 644 scripts/sm_api.ini ${D}/${sysconfdir}/sm
        install -m 755 scripts/sm-api ${D}/${sysconfdir}/init.d
        install -m 644 scripts/sm-api.service ${D}/${systemd_system_unitdir}
        install -m 644 scripts/sm-api.conf ${D}/${sysconfdir}/pmon.d
        install -m 644 etc/sm-api/policy.json ${D}/${sysconfdir}/sm-api
}
