
DESCRIPTION = "Python bindings for the ethtool kernel interface"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRCREV = "b8b09b69ef6d4f8a6f12a6c441305790060dd829"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "0.14+git${SRCPV}"

SRC_URI = "git://github.com/fedora-python/python-ethtool.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " libnl python"
inherit setuptools distutils pkgconfig
