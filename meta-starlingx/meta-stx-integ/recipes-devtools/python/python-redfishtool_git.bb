
DESCRIPTION = "Client side API implementation of the Redfish RESTful API for Data Center Hardware Management."
HOMEPAGE = "https://github.com/DMTF/Redfishtool"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=cee7a7694b5bf14bc9d3e0fbe78a64af"


SRCREV = "2bdcd905e1ad227f40809ec298804d5401047612"
SRCNAME = "Redfishtool"
BRANCH = "master"
PROTOCOL = "https"
PV = "1.1.0+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = " \
	git://github.com/DMTF/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://${PN}/0001-Adapt-redfishtool-to-python2.patch \
	"

inherit setuptools

RDEPENDS_${PN} += " \
	python-requests \
"

do_install_append() {
	:
}

FILES_${PN} += " \
	"
