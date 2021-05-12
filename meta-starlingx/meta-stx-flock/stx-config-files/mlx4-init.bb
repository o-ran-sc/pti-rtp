FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "mlx4-config"

DSTSUFX0 = "stx-configfiles"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/centos/mlx4-config.spec;beginline=1;endline=10;md5=b791daf2e53077e3acb71428524a356d \
	file://${STX_METADATA_PATH}/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

RDEPENDS_${PN}_append = " bash"

inherit systemd
SYSTEMD_PACKAGES += "${PN}"
SYSETMD_SERVICE_${PN}_append = "mlx4-config.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"


do_configure[noexec] = "1"
do_patch[noexec] = "1"

do_install() {
	install -d -m 0755 ${D}/${sysconfdir}/init.d/
	install -d -m 0755 ${D}/${systemd_system_unitdir}/
	install -d -m 0755 ${D}/${sysconfdir}/goenabled.d/
	install -d -m 0755 ${D}/${bindir}/

	install -m 755 ${STX_METADATA_PATH}/files/mlx4-configure.sh ${D}/${sysconfdir}/init.d/
	install -m 644 ${STX_METADATA_PATH}/files/mlx4-config.service ${D}/${systemd_system_unitdir}/
	install -m 555 ${STX_METADATA_PATH}/files/mlx4_core_goenabled.sh ${D}/${sysconfdir}/goenabled.d/
	install -m 755 ${STX_METADATA_PATH}/files/mlx4_core_config.sh ${D}/${bindir}/
}

FILES_${PN}_append = " ${systemd_system_unitdir}"

