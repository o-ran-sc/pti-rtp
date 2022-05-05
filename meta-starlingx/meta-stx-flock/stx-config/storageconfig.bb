require config-common.inc

SUBPATH0 = "storageconfig/storageconfig/"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN}_append = " bash"

inherit systemd 
SYSTEMD_PACKAGES += "storageconfig"
SYSTEMD_SERVICE_storageconfig = "storageconfig.service"
SYSTEMD_AUTO_ENABLE_storageconfig = "enable"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install_append() {
        oe_runmake GOENABLEDDIR=${D}/${sysconfdir}/goenabled.d  INITDDIR=${D}/${sysconfdir}/init.d \
	                        SYSTEMDDIR=${D}/${systemd_system_unitdir} install
	sed -i -e 's:/usr/local/bin/:/usr/bin/:g' ${D}/${sysconfdir}/init.d/storage_config
}

