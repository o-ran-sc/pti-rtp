
DESCRIPTION = "CLI and python client library for OpenStack Neutron"
HOMEPAGE = "https://launchpad.net/neutron"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRCREV = "115f60f0056899d536d5dd3e6491cd0f999cfd90"
SRCNAME = "python-neutronclient"
BRANCH = "stable/train"
PROTOCOL = "https"
PV = "6.14.0+git${SRCPV}"
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
        python-pbr \
        python-cliff \
        python-debtcollector \
        python-iso8601 \
        python-netaddr \
        python-osc-lib \
        python-oslo.i18n \
        python-oslo.log \
        python-oslo.serialization \
        python-oslo.utils \
        python-os-client-config \
        python-keystoneauth1 \
        python-keystoneclient \
        python-requests \
        python-simplejson \
        python-six \
        python-babel \
        "


PACKAGECONFIG ?= "bash-completion"
PACKAGECONFIG[bash-completion] = ",,bash-completion,bash-completion ${BPN}-bash-completion"

do_install_append() {
	install -d ${D}/${sysconfdir}/bash_completion.d
	install -m 664 ${S}/tools/neutron.bash_completion ${D}/${sysconfdir}/bash_completion.d
}

PACKAGES =+ "${BPN}-bash-completion"
FILES_${BPN}-bash-completion = "${sysconfdir}/bash_completion.d/*"

MONITOR_CHECKS_${PN} += "\
	neutron-api-check.sh \
"
