
DESCRIPTION = "OpenStackClient Library"
HOMEPAGE = "http://opensource.perlig.de/rcssmin/"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRC_URI[md5sum] = "240d3debc1b6eaadf5e8838f5f2d06fb"
SRC_URI[sha256sum] = "3467a1edf62946f1b67724fa7f9c699b5e31d80b111ed9e4c7aec21633a3e30d"

inherit setuptools pypi

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
        python-pbr \
        python-six \
        python-babel \
        python-cliff \
        python-keystoneauth1 \
        python-os-client-config \
        python-oslo.i18n \
        python-oslo.utils \
        python-simplejson \
        python-stevedore \
        "

CLEANBROKEN = "1"
