require config-common.inc

SUBPATH0 = "tsconfig/tsconfig"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit setuptools

RDEPENDS_${PN} = " bash"

do_install_append () {

	install -m 755 -d ${D}/${bindir}
	install -m 500 scripts/tsconfig ${D}/${bindir}/

}
