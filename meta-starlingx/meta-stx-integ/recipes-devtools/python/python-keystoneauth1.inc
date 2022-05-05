
DESCRIPTION = "Authentication Library for OpenStack Identity"
HOMEPAGE = "https://pypi.python.org/pypi/keystoneauth1"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=96f840d59b245a1c817fbcb901afc574"

SRC_URI[md5sum] = "0864f3485db8709d1dec0c8fa6073a31"
SRC_URI[sha256sum] = "db91ccab5cc43dac4a25dc2c090642d7c31f9ceb28df2c685620d7c12335a8cd"

inherit pypi

DEPENDS += " \
        ${PYTHON_PN}-pbr \
        "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
        ${PYTHON_PN}-pbr-native \
        "

RDEPENDS_${PN} += " \
        ${PYTHON_PN}-pbr \
        ${PYTHON_PN}-iso8601 \
        ${PYTHON_PN}-requests \
        ${PYTHON_PN}-six \
        ${PYTHON_PN}-stevedore \
	python2-os-service-types \
        "
