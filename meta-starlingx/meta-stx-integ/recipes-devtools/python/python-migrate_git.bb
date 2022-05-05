
DESCRIPTION = "A simple language agnostic database migration tool"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=123c58ff9658c9062dbe401889464492"

SRCREV = "b53b7168f8ac27e4c557de6e62ad85fe00d99566"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "0.3.8+git${SRCPV}"

SRC_URI = "git://github.com/kofrasa/migrate;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
