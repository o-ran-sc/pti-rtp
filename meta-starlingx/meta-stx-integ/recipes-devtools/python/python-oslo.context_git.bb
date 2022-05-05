
DESCRIPTION = "Oslo Context Library"
HOMEPAGE = "https://launchpad.net/oslo"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRCREV = "76a07f9022f0fa967707c9f6cb5a4a24aac6b3ef"
SRCNAME = "oslo.context"
PROTOCOL = "https"
BRANCH = "stable/stein"
S = "${WORKDIR}/git"
PV = "2.22.1+git${SRCPV}"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
        python-pbr-native \
        "

# RDEPENDS_default: 
RDEPENDS_${PN} += " \
        bash \
        python-pbr \
        python-debtcollector \
        python-positional \
        "
