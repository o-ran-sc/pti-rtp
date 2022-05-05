DESCRIPTION  = "platform-util"
SUMMARY  = "StarlingX Platform utilities installed only on controllers"

require utilities-common.inc

SUBPATH0 = "utilities/platform-util/platform-util"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN}_append  = " bash"

inherit setuptools systemd
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"
SYSTEMD_PACKAGES += " ${PN}"
SYSTEMD_SERVICE_${PN} = "opt-platform.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

do_unpack_append() {
    bb.build.exec_func('do_restore_files', d)
}

do_restore_files() {
	cd ${S}
	git reset ${SRCREV} utilities/platform-util/scripts
	git checkout utilities/platform-util/scripts
}

do_install_append() {

	cd ${S}/utilities/platform-util/scripts

	install -d -m0755 ${D}/${bindir}
	install -m0755 tc_setup.sh ${D}/${bindir}/tc_setup.sh
	install -m0755 remotelogging_tc_setup.sh ${D}/${bindir}/remotelogging_tc_setup.sh
	install -m0755 connectivity_test  ${D}/${bindir}/connectivity_test
	install -m0755 update-iso.sh ${D}/${bindir}/update-iso.sh

	install -p -d -m0755 ${D}/${sysconfdir}/init.d
	install -m0755 log_functions.sh ${D}/${sysconfdir}/init.d/log_functions.sh

	install -p -d -m0755 ${D}/${sbindir}
	install -m0755 patch-restart-mtce  ${D}/${sbindir}/patch-restart-mtce
	install -m0755 patch-restart-processes ${D}/${sbindir}/patch-restart-processes
	install -m0755 patch-restart-haproxy ${D}/${sbindir}/patch-restart-haproxy



	install -p -d -m0755 ${D}/${systemd_system_unitdir}

	install -m0644 opt-platform.mount  ${D}/${systemd_system_unitdir}/opt-platform.mount
	install -m0644 opt-platform.service ${D}/${systemd_system_unitdir}/opt-platform.service

}

FILES_${PN}_append  = " ${systemd_system_unitdir}/opt-platform.mount" 
