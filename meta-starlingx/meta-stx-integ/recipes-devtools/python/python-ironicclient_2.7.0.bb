
DESCRIPTION = "python-ironicclient"
STABLE = "master"
PROTOCOL = "https"
BRANCH = "master"
PV = "2.7.0"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRC_URI = "https://tarballs.openstack.org/python-ironicclient/python-ironicclient-${PV}.tar.gz"
SRC_URI[md5sum] = "6b13e133eb0c521a09c377f28fef139e"

DEPENDS += " \
	python \
	python-pbr-native \
	"

inherit setuptools

RDEPENDS_${PN}_append = " \
	bash \
	python-dogpile.cache \
	"
