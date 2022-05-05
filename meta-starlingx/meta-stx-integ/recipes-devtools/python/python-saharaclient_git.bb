
DESCRIPTION = "CLI and python client library for OpenStack Sahara"
HOMEPAGE = "https://launchpad.net/sahara"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

DEPENDS += " \
        python-pip \
        python-pbr \
        "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
        python-pbr-native \
"

RDEPENDS_${PN} += " \
	python-pbr \
	"

SRCNAME = "saharaclient"

SRC_URI = "git://github.com/openstack/python-saharaclient.git;branch=master"

PV = "2.3.0+git${SRCPV}"
SRCREV = "3107b452467537f4eef3d9ecfb5e35d110d19662"
S = "${WORKDIR}/git"

inherit setuptools

