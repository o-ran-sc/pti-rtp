
DESCRIPTION = "oslo.concurrency library"
HOMEPAGE = "https://github.com/openstack/oslo.concurrency"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

SRCREV = "5b42d276350666410a7d010a5152467ad509d3f9"
SRCNAME = "oslo.concurrency"
PROTOCOL = "https"
BRANCH = "stable/train"
S = "${WORKDIR}/git"
PV = "3.26.0+git${SRCPV}"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools rmargparse

DEPENDS += " \
        python-pbr \
        python-pip \
        "

DEPENDS += " \
        python-pbr-native \
        "

# RDEPENDS_default:
RDEPENDS_${PN} += " \
        python-pbr \
        python-fasteners \
        python-oslo.config \
        python-oslo.i18n \
        python-oslo.utils \
        python-six \
        python-enum34 \
        "
