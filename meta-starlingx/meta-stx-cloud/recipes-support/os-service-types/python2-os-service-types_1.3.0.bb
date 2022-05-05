
DESCRIPTION = "A library to handle official service types for OpenStack and their historical aliases."
HOMEPAGE = "https://opendev.org/openstack/os-service-types"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

STABLE = "master"
PROTOCOL = "https"
BRANCH = "master"
SRCREV = "2e5b38088a43539621de82aa2d0c1b366c2638a9"
S = "${WORKDIR}/git"

SRC_URI = "git://opendev.org/openstack/os-service-types.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools 

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
        python-lxml \
        python-requests \
        python-cython \
        "
