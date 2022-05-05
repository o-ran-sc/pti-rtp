require monitoring-common.inc

SUBPATH0 = "vm-topology/vm-topology"


LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN}_append = " \
	python \
	libvirt \
	python-keyring \
	"

DEPENDS += " \
	python-keyring \
	libvirt \
	"

inherit setuptools python-dir
