
DESCRIPTION = "This project provides first-class OAuth library support for Requests."

LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://LICENSE;md5=22d117a849df10d047ed9b792838e863"

SRCREV = "e19ac082682ecf3ed06cdcc5538e3c4bb2aa7762"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "1.2.0"

SRC_URI = "git://github.com/requests/requests-oauthlib;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
