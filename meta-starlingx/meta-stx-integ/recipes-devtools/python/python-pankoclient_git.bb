
DESCRIPTION = "python-pankoclient"
PROTOCOL = "https"
BRANCH = "stable/train"
SRCREV = "28b55860a2e71fe1fd015d868d64500d3b36470c"
S = "${WORKDIR}/git"
PV = "0.7.0+git${SRCPV}"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRC_URI = "git://github.com/openstack/python-pankoclient.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

DEPENDS += " \
	python \
	python-pbr-native \
	"

inherit setuptools
