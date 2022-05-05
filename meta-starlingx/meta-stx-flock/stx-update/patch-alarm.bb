DESCRIPTION = "TIS Platform Patching"
SUMMARY = "Patch alarm management"

require update-common.inc

SUBPATH0 = "patch-alarm/patch-alarm"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN}_append = " \
	bash \
	python \
	python-requests-toolbelt \
	"

inherit setuptools

do_unpack_append() {
    bb.build.exec_func('do_restore_files', d)
}

do_restore_files() {
	cd ${S}
	git reset ${SRCREV} patch-alarm/scripts
	git checkout patch-alarm/scripts
}


do_install_append () {

	cd ${S}/patch-alarm/

	install -m 755 -d ${D}/${bindir}
	install -m 755 -d ${D}/${sysconfdir}/init.d

	install -m 700 scripts/bin/patch-alarm-manager ${D}/${bindir}/
	install -m 700 scripts/init.d/patch-alarm-manager ${D}/${sysconfdir}/init.d/
}
