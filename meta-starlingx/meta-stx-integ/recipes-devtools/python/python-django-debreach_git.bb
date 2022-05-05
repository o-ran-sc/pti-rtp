
DESCRIPTION = "Simple database sharding (horizontal partitioning) library for Django applications."

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=04c6b8f6ee56b21d30ecb172b066902c"


SRCREV = "b425bb719ea5de583fae7db5b7419e5fed569cb0"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "1.5.2+git${SRCPV}"

SRC_URI = "git://github.com/lpomfrey/django-debreach.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
