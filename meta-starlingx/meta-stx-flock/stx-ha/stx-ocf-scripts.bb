require ha-common.inc
SUBPATH0 = "stx-ocf-scripts/src/ocf"


LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN} = " \
	bash \
	openstack-ras \
	"

do_unpack_append() {
    bb.build.exec_func('do_restore_license_file', d)
}

do_restore_license_file () {
    cd ${S}
    git reset HEAD LICENSE
	git checkout LICENSE
}

do_install_append () {
	install -d -m 755 ${D}/usr/lib/ocf/resource.d/openstack
	install -p -D -m 755 $(find . -type f -not -path './.git/*') ${D}/usr/lib/ocf/resource.d/openstack/ 
}

FILES_${PN}_append = " \
	${libdir}/ocf/resource.d/openstack/ \
	"
