
DESCRIPTION = "Oslo db library"
HOMEPAGE = "http://launchpad.net/oslo"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

SRCREV = "4de33ebd504a2c3dbddc2492bdb96ae7bca77d66"
SRCNAME = "oslo.db"
PROTOCOL = "https"
BRANCH = "stable/stein"
S = "${WORKDIR}/git"
PV = "4.27.0+git${SRCPV}"

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
        python-alembic \
	python-sqlalchemy \
	python-sqlalchemy-migrate \
	python-stevedore \
	python-pbr \
	python-debtcollector \
	"
