
DESCRIPTION = "A simple connector pool for python-ldap."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://setup.py;md5=9c24605289b49ad77a51ba7986425158"


SRCREV = "f75c1a8a34d4fbe6df7889205b5c6a4db6886c33"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "2.4.1+git${SRCPV}"

SRC_URI = "git://opendev.org/openstack/ldappool.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python python-pbr-native"
inherit setuptools distutils pkgconfig
