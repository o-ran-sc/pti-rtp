require ha-common.inc
SUBPATH0 = "service-mgmt-client/sm-client"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
	file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2 \
	"

RDEPENDS_${PN}_append = " \
	python \
	python-six \
	"

inherit setuptools
