FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "iscsi-initiator-utils-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/centos/iscsi-initiator-utils-config.spec;beginline=1;endline=10;md5=4f3e541126551bf6458a8a6557b1e171 \
	"

inherit systemd
SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_SERVICE_${PN}_append = "iscsi-shutdown.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

do_install_append() {
   install -d  ${D}/${libdir}/tmpfiles.d
   install -d  ${D}/${sysconfdir}/systemd/system

   install -m 0644 ${STX_METADATA_PATH}/files/iscsi-cache.volatiles   ${D}/${libdir}/tmpfiles.d/iscsi-cache.conf
   install -m 0644 ${STX_METADATA_PATH}/files/iscsi-shutdown.service  ${D}/${sysconfdir}/systemd/system
   install -m 0644 ${STX_METADATA_PATH}/files/iscsid.conf             ${D}/${sysconfdir}/iscsi/iscsid.conf

   rm -rf ${D}/${nonarch_base_libdir}/
}

FILES_${PN}_append = " \
	${libdir}/tmpfiles.d \
	"
