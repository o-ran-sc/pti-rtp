
SUMMARY = " Web Services Made Easy (WSME)"
DESCRIPTION = "\
	Web Services Made Easy (WSME) simplifies the writing of REST web services by providing simple yet \
	powerful typing, removing the need to directly manipulate the request and the response objects. \
	"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5a9126e7f56a0cf3247050de7f10d0f4"

SRCREV = "f36a607124355007d3a830ebe7e53efda777c58a"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git/"
PV = "0.9.3+git${SRCPV}"

SRC_URI = "git://opendev.org/x/wsme.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

DEPENDS += " python3 python3-pbr-native"
inherit distutils3 
