
DESCRIPTION = "Iniparse is a INI parser for Python"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=52f28065af11d69382693b45b5a8eb54"

SRCREV = "0305d08121461776222e515ae21a8405ee68eef4"
PROTOCOL = "https"
BRANCH = "main"
S = "${WORKDIR}/git"
PV = "0.4+git${SRCPV}"

SRC_URI = "git://github.com/candlepin/python-iniparse.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"



inherit setuptools distutils
