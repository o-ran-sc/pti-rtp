require update-common.inc

SUBPATH0 = "enable-dev-patch"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_unpack_append() {
    bb.build.exec_func('do_restore_license_file', d)
}

do_restore_license_file () {
	cd ${S}
	git reset HEAD LICENSE
	git checkout LICENSE
}

do_install () {
	install -m 755 -d ${D}/${sysconfdir}/pki/wrs
	install -m 444 enable-dev-patch/dev_certificate_enable.bin ${D}/${sysconfdir}/pki/wrs
}
