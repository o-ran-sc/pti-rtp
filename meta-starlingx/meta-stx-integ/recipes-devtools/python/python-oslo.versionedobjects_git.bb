
DESCRIPTION = "Oslo versionedobjects library"
HOMEPAGE = "https://wiki.openstack.org/wiki/Oslo"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRCREV = "c95f0c876840e36f37acb14d5eec5238d85e7dce"
SRCNAME = "oslo.versionedobjects"
PROTOCOL = "https"
BRANCH = "stable/queens"
S = "${WORKDIR}/git"
PV = "1.31.2+git${SRCPV}"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
        python-six \
        python-oslo.concurrency \
        python-oslo.config \
        python-oslo.context \
        python-oslo.messaging \
        python-oslo.serialization \
        python-oslo.utils \
        python-oslo.log \
        python-oslo.i18n \
        python-webob \
        python-iso8601 \
        python-netaddr \
       "
