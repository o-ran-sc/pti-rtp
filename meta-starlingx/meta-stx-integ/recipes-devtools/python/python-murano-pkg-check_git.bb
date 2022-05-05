
DESCRIPTION = "Murano package validator tool"


LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"


SRCREV = "18119f5e9d5a9d706c13188e057cb5b242c51f89"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "0.3.0+git${SRCPV}"

SRC_URI = "git://github.com/openstack/murano-pkg-check.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python python-pbr-native"
inherit setuptools distutils pkgconfig
