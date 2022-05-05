require metal-common.inc

SUBPATH0 = "mtce-storage/src/"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI += "file://0001-mtce-storage-dont-install-empty-directory-unless-nee.patch;striplevel=3"

RDEPENDS_${PN}_append = " \
	bash \
	systemd \
	"

inherit systemd
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "goenabled-storage.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	cd ${S}
	oe_runmake buildroot=${D} \
		_sysconfdir=${sysconfdir} _unitdir=${systemd_system_unitdir} _datarootdir=${datadir} \
		install
}


FILES_${PN}_append = " ${datadir}/licenses/mtce-storage-1.0/LICENSE"

