
DESCRIPTION = "API supporting parsing command line arguments and .ini style configuration files."
HOMEPAGE = "https://pypi.python.org/pypi/oslo.config/5.2.0"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c46f31914956e4579f9b488e71415ac8"

SRCREV = "31c11ab4289efa1a91835f3daa928fe927ac4276"
SRCNAME = "oslo.config"
PROTOCOL = "https"
BRANCH = "stable/queens"
S = "${WORKDIR}/git"
PV = "5.2.0+git${SRCPV}"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools rmargparse

DEPENDS += " \
        python-pbr \
        python-pip \
        "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
    python-pbr \
    python-netaddr \
    python-six \
    python-stevedore \
    python-debtcollector \
    python-oslo.i18n \
    python-rfc3986 \
    python-pyyaml \
    python-importlib-metadata \
    "
	
