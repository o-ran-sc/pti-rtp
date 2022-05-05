DESCRIPTION = "Pacemaker High Availability resource agents for OpenStack"
SUMMARY = "Openstack Resource Agents from Madkiss"

PROTOCOL = "https"
BRANCH = "master"
SRCNAME = "stx-openstack-ras"
SRCREV = "cc6f677570b4f0e7ba44a91d5dff33164b8b76bc"
S = "${WORKDIR}/git"
PV = "1.0.0"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/starlingx-staging/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"


do_install() {
	make  DESTDIR=${D} install
	rm -rf ${D}/usr/lib/ocf/resource.d/openstack/ceilometer-agent-central
	rm -rf ${D}/usr/lib/ocf/resource.d/openstack/ceilometer-alarm-evaluator
	rm -rf ${D}/usr/lib/ocf/resource.d/openstack/ceilometer-alarm-notifier
}

FILES_${PN} += " ${libdir}"
