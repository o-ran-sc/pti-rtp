
DESCRIPTION = "This library simplifies using Google’s various server-to-server authentication mechanisms to access Google APIs."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRCREV = "1322d896ba725b8d73fd7ac4793601d9f574a839"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "1.6.3+git${SRCPV}"

SRC_URI = "git://github.com/googleapis/google-auth-library-python.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
