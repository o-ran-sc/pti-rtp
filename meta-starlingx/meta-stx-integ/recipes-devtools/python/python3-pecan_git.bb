
DESCRIPTION = "WSGI object-dispatching web framework"

HOMEPAGE = "https://pypi.python.org/pypi/pecan/"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d846877d24bbb3d7a00a985c90378e8c"

SRCREV = "da15e06d783e2cf569b39ba506e68e4e1e85568d"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "1.3.2+git${SRCPV}"

SRC_URI = "git://github.com/pecan/pecan.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


inherit setuptools3 distutils3

RDEPENDS_${PN} = "python3-mako \
		  python3-six \
		  python3-logutils \
		  python3-webtest  \
		  "

do_install_append() {
        mv ${D}/${bindir}/gunicorn_pecan ${D}/${bindir}/gunicorn_pecan3
        mv ${D}/${bindir}/pecan ${D}/${bindir}/pecan3
}
