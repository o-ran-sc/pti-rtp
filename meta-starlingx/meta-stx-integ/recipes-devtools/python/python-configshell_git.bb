
DESCRIPTION = " \
ConfigShell is a Python library that provides a framework for building simple but nice CLI-based applications running both as single-command tools and interactive shells providing a UNIX filesystem-like navigation interface, as well as full autocompletion support and interactive inline help. It is part of LIO. \
	"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRCREV = "020d540850ca36f31af68e9b545a520f1122ea69"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "1.6+git${SRCPV}"

SRC_URI = "git://github.com/Datera/configshell.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python python-pyparsing-native"
inherit setuptools distutils pkgconfig
