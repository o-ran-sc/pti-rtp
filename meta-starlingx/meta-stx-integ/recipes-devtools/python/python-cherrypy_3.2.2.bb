SUMMARY = " CherryPy is a pythonic, object-oriented HTTP framework"
DESCRIPTION = "\
It allows building web applications in much the same way one would build any \
other object-oriented program. This design results in less and more readable \
code being developed faster. It's all just properties and methods. \
It is now more than ten years old and has proven fast and very stable. \
It is being used in production by many sites, from the simplest to the most \
demanding. \
"

HOMEPAGE = "https://www.cherrypy.org/"
AUTHOR = "CherryPy Team"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://cherrypy/LICENSE.txt;md5=c187ff3653a0878075713adef2c545c3"

SRC_URI = "https://pypi.python.org/packages/source/C/CherryPy/CherryPy-${PV}.tar.gz"
SRC_URI[md5sum] = "c1b1e9577f65f9bb88bfd1b15b93b911"
SRC_URI[sha256sum] = "dc5a88562795c2ee462dac5b37aba1cf4f34f3e27281ec11049227039308b691"

S = "${WORKDIR}/CherryPy-${PV}"

inherit setuptools

FILES_${PN} += "\
    ${datadir}/cherrypy \
"
