PACKAGES += " config-gate-worker"

require config-common.inc

SUBPATH0 = "config-gate/files"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_config-gate-worker_append = " bash"
RDEPENDS_${PN}_append = " bash"


inherit systemd
SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_PACKAGES += "${PN}-worker"
SYSTEMD_SERVICE_${PN} = "config.service"
SYSTEMD_SERVICE_config-gate-worker = "worker-config-gate.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"
SYSTEMD_AUTO_ENABLE_${PN}-worker = "enable"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	oe_runmake -e \
                 SBINDIR=${D}/${sbindir} SYSTEMDDIR=${D}/${systemd_system_unitdir} \
		 install
}
