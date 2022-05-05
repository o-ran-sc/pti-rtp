
DESCRIPTION = "Oslo Serialization API"
HOMEPAGE = "https://launchpad.net/oslo"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

SRCREV = "576b13ec26baa671da05df56a8d14aba6fa3e826"
SRCNAME = "oslo.serialization"
PROTOCOL = "https"
BRANCH = "stable/train"
S = "${WORKDIR}/git"
PV = "2.23.0+git${SRCPV}"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


inherit setuptools

# DEPENDS_default: python-pip

DEPENDS += " \
        python-pip \
	python-pbr-native\
	"

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
	python-pbr-native \
	"

# RDEPENDS_default:
RDEPENDS_${PN} += " \
	python-pbr \
	python-six \
	python-oslo.utils \
	python-pytz \
	python-msgpack \
	"
