FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "iscsi-initiator-utils-config"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;beginline=1;endline=10;md5=4f3e541126551bf6458a8a6557b1e171 \
	"

SRC_URI += " \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
	"

inherit systemd
SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_SERVICE_${PN}_append = "iscsi-shutdown.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/stx-configfiles/centos/iscsi-initiator-utils-config.spec ${S}/stx-configfiles-LICENSE
}

do_install_append() {
   install -d  ${D}/${libdir}/tmpfiles.d
   install -d  ${D}/${sysconfdir}/systemd/system

   install -m 0644 ${WORKDIR}/stx-configfiles/files/iscsi-cache.volatiles   ${D}/${libdir}/tmpfiles.d/iscsi-cache.conf
   install -m 0644 ${WORKDIR}/stx-configfiles/files/iscsi-shutdown.service  ${D}/${sysconfdir}/systemd/system
   install -m 0644 ${WORKDIR}/stx-configfiles/files/iscsid.conf             ${D}/${sysconfdir}/iscsi/iscsid.conf

   rm -rf ${D}/${nonarch_base_libdir}/
}

FILES_${PN}_append = " \
	${libdir}/tmpfiles.d \
	"
