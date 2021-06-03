
DESCRIPTION = "python-ironicclient"
SECTION = "devel/python"

SRCREV = "04ef2d7b04caad162e299c52542b2cb581552ea3"
SRCNAME = "python-ironicclient"
PROTOCOL = "https"
BRANCH = "stable/train"
PV = "3.1.0+git${SRCPV}"
S = "${WORKDIR}/git"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

DEPENDS += " \
	python \
	python-pbr-native \
	"

inherit setuptools

RDEPENDS_${PN}_append = " \
	bash \
	python-dogpile.cache \
	python-oslo.config \
	"
