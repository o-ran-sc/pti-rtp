
DESCRIPTION = "Python client for containers service"
HOMEPAGE = "https://github.com/openstack/python-magnumclient"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d2794c0df5b907fdace235a619d80314"

SRCREV = "37e602d160632a386c2960ec8777bfc65642a9a9"
SRCNAME = "python-magnumclient"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "2.12.0+git${SRCPV}"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

RDEPENDS_${PN} +=" \
	python-pbr \
	python-babel \
	python-stevedore \
	python-requests \
	python-oslo.i18n \
	python-oslo.serialization \
	python-oslo.utils \
	python-os-client-config \
	python-osc-lib \
	python-prettytable \
	python-cryptography \
	python-decorator \
	"

inherit setuptools
