require monitoring-common.inc

SUBPATH0 = "monitor-tools/scripts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

do_install_append() {

	install -m 755 -d ${D}/${bindir}
	# support files ; service and pmon conf
	install -m 644 memtop  ${D}/${bindir}
	install -m 600 schedtop ${D}/${bindir}
	install -m 600 occtop  ${D}/${bindir}
}
