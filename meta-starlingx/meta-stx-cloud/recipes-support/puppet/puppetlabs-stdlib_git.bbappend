FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRCREV = "b89d5f388ca701e38a0e0337408f5ccb7e68565f"
PROTOCOL = "https"
BRANCH = "master"
PV = "4.18.0"

SRC_URI = " \
	git://github.com/puppetlabs/puppetlabs-stdlib.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://puppetlabs-stdlib/Add-gemspec.patch \
	file://puppetlabs-stdlib/0001-Filter-password-in-logs.patch \
	"

S = "${WORKDIR}/git"

RUBY_INSTALL_GEMS = "puppetlabs-stdlib-${PV}.gem"

do_install_append () {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/stdlib
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/stdlib
}

FILES_${PN} += " ${datadir}"

RDEPENDS_${PN}_append = " perl"
