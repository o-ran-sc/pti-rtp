
DESCRIPTION = "python-pankoclient"
STABLE = "master"
PROTOCOL = "https"
BRANCH = "master"
SRCREV = "572aee9cf6ac618eb5d1ea325f9e59414d387dbf"
S = "${WORKDIR}/git"
PV = "0.5.0"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRC_URI = "git://github.com/openstack/python-pankoclient.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

DEPENDS += " \
	python \
	python-pbr-native \
	"

inherit setuptools
