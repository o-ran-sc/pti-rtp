DESCRIPTION = "Titanium Cloud namespace utilities"
SUMMARY = "namespace utils"

require utilities-common.inc
SUBPATH0 = "utilities/namespace-utils/namespace-utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN}_append = " bash"

do_configure[noexec] = "1"

do_compile() {
	$CC ${LDFLAGS} ${CFLAGS} -o bashns bashns.c
}

do_install() {

	install -d -m0755 ${D}/${sbindir}
	install -m0500 bashns ${D}/${sbindir}
	install -m0500 umount-in-namespace  ${D}/${sbindir}
}
