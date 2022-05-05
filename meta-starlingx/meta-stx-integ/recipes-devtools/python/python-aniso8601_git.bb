
DESCRIPTION = "Another ISO 8601 parser for Python"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=bf2bdb115b4d685026985cc189ca5375"

SRCREV = "b5aad0fbef5664e624986a10bd1362e0ac05214c"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "7.0.0+git${SRCPV}"

SRC_URI = "git://bitbucket.org/nielsenb/aniso8601.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
