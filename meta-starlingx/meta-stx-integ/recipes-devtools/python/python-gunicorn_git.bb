DESCRIPTION = "Gunicorn 'Green Unicorn' is a Python WSGI HTTP Server for UNIX. \
It's a pre-fork worker model ported from Ruby's Unicorn project. \
The Gunicorn server is broadly compatible with various web frameworks, \
simply implemented, light on server resource usage, and fairly speedy."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=03ccc2b0215ca2a4b1e5f3c5483807f9"


SRCREV = "328e509260ae70de6c04c5ba885ee17960b3ced5"
PROTOCOL = "https"
BRANCH = "19.x"
S = "${WORKDIR}/git"
PV = "19.7.1+git${SRCPV}"

SRC_URI = "git://github.com/benoitc/gunicorn.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
