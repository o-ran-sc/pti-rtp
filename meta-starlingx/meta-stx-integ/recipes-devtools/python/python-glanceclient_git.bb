
DESCRIPTION = "Client library for Glance built on the OpenStack Images API."
HOMEPAGE = "https://opendev.org/openstack/python-glanceclient"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

SRCREV = "44a4dbd6ce2642daeaca9f45ac99e2d1b39e805a"
SRCNAME = "python-glanceclient"
BRANCH = "stable/train"
PROTOCOL = "https"
PV = "2.16.0+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"
	
inherit setuptools monitor rmargparse

DEPENDS += " \
        python-pip \
        python-pbr-native\
        "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
	bash \
        python-pbr \
        python-prettytable \
        python-keystoneauth1 \
	python-warlock \
        python-six \
        python-oslo.utils \
        python-oslo.i18n \
	python-wrapt \
	python-pyopenssl \
	"


do_install_append() {
	install -d -m 755 ${D}/${sysconfdir}/bash_completion.d
	install -p -D -m 664 tools/glance.bash_completion ${D}/${sysconfdir}/bash_completion.d/glance
}
