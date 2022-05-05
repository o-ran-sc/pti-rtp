
DESCRIPTION = "Oslo Middleware Library"
HOMEPAGE = "https://launchpad.net/oslo"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

SRCREV = "8812bc3fc490f0db4977418eaedf58190a0df394"
SRCNAME = "oslo.middleware"
PROTOCOL = "https"
BRANCH = "stable/train"
S = "${WORKDIR}/git"
PV = "3.31.0+git${SRCPV}"

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
        python-pbr \
        python-jinja2 \
        python-oslo.config \
        python-oslo.context \
        python-oslo.i18n \
        python-oslo.utils \
        python-six \
        python-stevedore \
        python-webob \
        python-debtcollector \
        python-statsd \
        "
