DESCRIPTION = "Management of /var/log filesystem"

require utilities-common.inc
SUBPATH0 = "utilities/logmgmt/logmgmt/"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN}_append = " \
	systemd \
	python-daemon \
	"

inherit setuptools systemd
SYSTEMD_PACKAGES += "logmgmt"
SYSTEMD_SERVICE_${PN} = "logmgmt.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

do_unpack_append() {
    bb.build.exec_func('do_restore_files', d)
}

do_restore_files() {
	cd ${S}
	git reset ${SRCREV} utilities/logmgmt/scripts
	git checkout utilities/logmgmt/scripts
}

do_install_append() {

	cd ${S}/utilities/logmgmt/scripts
	install -d -m0755 ${D}/${bindir}
	install -m0700 bin/logmgmt ${D}/${bindir}
	install -m0700 bin/logmgmt_postrotate ${D}/${bindir}
	install -m0700 bin/logmgmt_prerotate ${D}/${bindir}

	install -d -m0755 ${D}/${sysconfdir}/init.d
	install -m0700 init.d/logmgmt ${D}/${sysconfdir}/init.d

	install -d -m0755 ${D}/${sysconfdir}/pmon.d
	install -m0700 pmon.d/logmgmt ${D}/${sysconfdir}/pmon.d

	install -d -m0755 ${D}/${systemd_system_unitdir}
	install -m0664 etc/systemd/system/logmgmt.service ${D}/${systemd_system_unitdir}
}

