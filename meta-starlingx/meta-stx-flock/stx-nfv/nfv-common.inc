DESCRIPTION = "stx-nfv"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCNAME = "nfv"
SRCREV = "aaa932c00e028dcbaf0eed6843c4d3e51f09b2c1"
SRCREV_opendev = "aaa932c00e028dcbaf0eed6843c4d3e51f09b2c1"
DESTSUFFIX = "${PN}-${PV}"
PV = "1.0.0"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = " \
	git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0};name=opendev \
	"

DEPENDS += " \
	python \
	python-pbr-native \
	mtce \
	fm-common \
	json-c \
	openssl \
	libevent \
	libgcc \
	"
