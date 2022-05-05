
DESCRIPTION = "Oslo policy library"
HOMEPAGE = "https://github.com/openstack/oslo.policy"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRCREV = "b9fd10e2612f26c93d49c168a0408aba6d20e5bf"
SRCNAME = "oslo.policy"
PROTOCOL = "https"
BRANCH = "stable/train"
S = "${WORKDIR}/git"
PV = "1.43.1+git${SRCPV}"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
        python-oslo.config \
	python-oslo.i18n \
	python-oslo.serialization \
	python-oslo.utils \
	python-six \
	python-pyyaml \
	python-requests \
	"
