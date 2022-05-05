
DESCRIPTION = "A messaging framework built on the QPID Proton engine. It provides a callback-based API for message passing. See the User Guide in the docs directory for more detail."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=54830c88a42666c6835a0f834f93a521"

SRCREV = "39f0ee6fd6d71ee335b1229b91ab0bb47bdd71f0"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "0.6.1+git${SRCPV}"

SRC_URI = "git://github.com/GehirnInc/python-jwt;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
