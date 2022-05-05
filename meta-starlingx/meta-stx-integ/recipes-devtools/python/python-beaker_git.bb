
DESCRIPTION = "Beaker is a web session and general caching library that includes WSGI middleware for use in web applications."

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5297e0e46f5be6c86a87e35afe958cc7"

SRCREV = "4a2cb747c16dc58af39b1ae56e1cf14dfeb9c9a7"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "1.10.1+git${SRCPV}"

SRC_URI = "git://github.com/bbangert/beaker;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
