
SUMMARY = " Lang - Python Language Constraints"
DESCRIPTION = "\
	Lang is a Python module that allows enforcing programming language constraints. Lang was \
	built using a Java like mindset, so many of the constraints that are supported are mirrors \
	of constraints in the Java programming language. \
	"


LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fa818a259cbed7ce8bc2a22d35a464fc"

SRCREV = "feb4c638ebc581d9913f440965e83558fd10018c"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git/src"
PV = "1.0.4+git${SRCPV}"

SRC_URI = "git://github.com/amitassaraf/lang.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python3 python3-setuptools-scm-native"
inherit setuptools3 distutils3 pkgconfig
