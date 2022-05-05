DESCRIPTION_ceph-manager = " \
Handle Ceph API calls and provide status updates via alarms. \
Handle sysinv RPC calls for long running Ceph API operations: \
	- cache tiering enable \
	- cache tiering disable \
"


require utilities-common.inc

SUBPATH0 = "ceph/ceph-manager/ceph-manager"


LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN}_append = " sysinv"

inherit setuptools systemd
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"
SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_SERVICE_${PN} = " ceph-manager.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

do_unpack_append() {
    bb.build.exec_func('do_restore_files', d)
}

do_restore_files() {
	cd ${S}
	git reset ${SRCREV} ceph/ceph-manager/scripts
	git reset ${SRCREV} ceph/ceph-manager/files
	git checkout ceph/ceph-manager/scripts
	git checkout ceph/ceph-manager/files
}

do_install_append() {

	install -d -m0755 ${D}/${bindir}
	install -d -m0755 ${D}/${sysconfdir}/init.d
	install -d -m0755 ${D}/${sysconfdir}/logrotate.d
	install -d -m0755 ${D}/${systemd_system_unitdir}

	install -p -m0700 ${S}/ceph/ceph-manager/scripts/bin/ceph-manager ${D}/${bindir}
	install -p -m0700 ${S}/ceph/ceph-manager/scripts/init.d/ceph-manager ${D}/${sysconfdir}/init.d
	install -p -m0700 ${S}/ceph/ceph-manager/files/ceph-manager.logrotate ${D}/${sysconfdir}/logrotate.d
	install -p -m0700 ${S}/ceph/ceph-manager/files/ceph-manager.service ${D}/${systemd_system_unitdir}
}
