
DESCRIPTION = "A messaging framework built on the QPID Proton engine. It provides a callback-based API for message passing. See the User Guide in the docs directory for more detail."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d2794c0df5b907fdace235a619d80314"

SRCREV = "5392392046989f1bb84ba938c30e4d48311075f1"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

PV = "2.3.0+git${SRCPV}"

SRC_URI = "git://github.com/kgiusti/pyngus;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
