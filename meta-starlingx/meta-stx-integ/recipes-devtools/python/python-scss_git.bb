
DESCRIPTION = "A simple language agnostic database migration tool"

LICENSE = "LGPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e6a600fd5e1d9cbde2d983680233ad02 "


SRCREV = "34fe985e6b43caa9f9b9bcd0dc433be4b2a1fdec"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "0.8.73"

SRC_URI = "git://github.com/klen/python-scss;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
