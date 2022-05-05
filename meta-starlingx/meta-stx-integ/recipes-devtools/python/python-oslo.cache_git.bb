
DESCRIPTION = "oslo.config enabled dogpile cache"
HOMEPAGE = "https://github.com/openstack/oslo.cache"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRCREV = "3b8d9c3f6c87d62e5502cf4a9ae89e4067180c1f"
SRCNAME = "oslo.cache"
PROTOCOL = "https"
BRANCH = "stable/train"
S = "${WORKDIR}/git"
PV = "1.26.0+git${SRCPV}"

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
        python-dogpile.cache \
        python-six \
        python-oslo.config \
        python-oslo.i18n \
        python-oslo.log \
        python-oslo.utils \
        "
