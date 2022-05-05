
DESCRIPTION = "Set of handlers for the Python standard library's logging package"
HOMEPAGE = "https://pypi.python.org/pypi/logutils"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=44c35f0b8e2a27a2f33a4e4a5c65d014"

SRCREV = "66d55fae0cb3a94bc6a25af8e7c3dff2535a1b02"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "0.3.5"

# SRC_URI = "hg://bitbucket.org/vinay.sajip/logutils;module=${PN}-${PV};proto=https;rev=8dcaf2a86257"
SRC_URI = "git://bitbucket.org/vinay.sajip/logutils.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools3 distutils3
