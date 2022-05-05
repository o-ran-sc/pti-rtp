
DESCRIPTION = "Middleware for Openstack identity API"
HOMEPAGE = "https://launchpad.net/keystonemiddleware"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4a4d0e932ffae1c0131528d30d419c55"

SRCREV = "83d0612e03471f56be3be2b521cc21974118cebe"
SRCNAME = "keystonemiddleware"
BRANCH = "stable/train"
PROTOCOL = "https"
PV = "5.1.0"
S = "${WORKDIR}/git"

SRC_URI = "git://git.openstack.org/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


inherit setuptools

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
        python-keystoneauth1 \
        python-oslo.cache \
        python-oslo.config \
        python-oslo.context \
        python-oslo.i18n \
        python-oslo.log \
        python-oslo.serialization \
        python-oslo.utils \
        python-pbr \
        python-positional \
        python-pycadf \
        python-keystoneclient \
        python-requests \
        python-six \
        python-webob \
        "
