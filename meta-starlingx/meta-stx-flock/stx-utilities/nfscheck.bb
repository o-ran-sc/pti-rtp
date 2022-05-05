DESCRIPTION = "NFS Audit"
SUMMARY = "NFS Audit"

require utilities-common.inc
SUBPATH0 = "utilities/nfscheck/files/"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN}_append = " bash systemd"

inherit systemd
SYSTEMD_PACKAGES += " nfscheck"
SYSTEMD_SERVICE_${PN} = "nfscheck.service"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {

	install -d -m0755 ${D}/${bindir}
	install -m0755 nfscheck.sh ${D}/${bindir}

	install -d -m0755 ${D}/${systemd_system_unitdir}
	install -m0644 nfscheck.service ${D}/${systemd_system_unitdir}
}
