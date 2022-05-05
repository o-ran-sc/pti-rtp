
DESCRIPTION = "requests-toolbelt"

STABLE = "master"
PROTOCOL = "https"
BRANCH = "master"
SRCREV = "1e384626476f7afbff0f649fe41886d0f27473d6"
S = "${WORKDIR}/git"
PV = "0.9.1+${SRCPV}"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=71760e0f1dda8cff91b0bc9246caf571"

SRC_URI = "git://github.com/requests/toolbelt.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

DEPENDS = " \
	python \
	python-pbr-native \
	"

inherit setuptools

RDEPENDS_${PN} += " bash"
