FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "mlx4-config"
DSTSUFX0 = "stx-configfiles"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;beginline=1;endline=10;md5=b791daf2e53077e3acb71428524a356d \
	file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
	"

SRC_URI += " \
        git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
		file://APACHE-2.0.LICENSE \
	"

RDEPENDS_${PN}_append = " bash"

inherit systemd
SYSTEMD_PACKAGES += "${PN}"
SYSETMD_SERVICE_${PN}_append = "mlx4-config.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"


do_configure[noexec] = "1"
do_patch[noexec] = "1"

do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/stx-configfiles/centos/mlx4-config.spec ${S}/stx-configfiles-LICENSE
    cp -pf ${WORKDIR}/APACHE-2.0.LICENSE  ${S}/LICENSE
}

do_install() {
	install -d -m 0755 ${D}/${sysconfdir}/init.d/
	install -d -m 0755 ${D}/${systemd_system_unitdir}/
	install -d -m 0755 ${D}/${sysconfdir}/goenabled.d/
	install -d -m 0755 ${D}/${bindir}/

	install -m 755 ${WORKDIR}/${DSTSUFX0}/files/mlx4-configure.sh ${D}/${sysconfdir}/init.d/
	install -m 644 ${WORKDIR}/${DSTSUFX0}/files/mlx4-config.service ${D}/${systemd_system_unitdir}/
	install -m 555 ${WORKDIR}/${DSTSUFX0}/files/mlx4_core_goenabled.sh ${D}/${sysconfdir}/goenabled.d/
	install -m 755 ${WORKDIR}/${DSTSUFX0}/files/mlx4_core_config.sh ${D}/${bindir}/
}

FILES_${PN}_append = " ${systemd_system_unitdir}"

