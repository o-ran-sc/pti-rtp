
DESCRIPTION = "YAML 1.2 loader/dumper package for Python"
HOMEPAGE = "https://pypi.org/project/ruamel.yaml/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=84e9d7d823d2abac052e70de2051ca1c"

SRC_URI[md5sum] = "d53315f8ccb93748d00ccca39486ac78"
SRC_URI[sha256sum] = "350496f6fdd8c2bb17a0fa3fd2ec98431280cf12d72dae498b19ac0119c2bbad"

inherit setuptools pypi python-dir

DEPENDS += " \
	${PYTHON_PN}-native \
	${PYTHON_PN}-cryptography-native \
	"
RDEPENDS_${PN}_append  = " \
	${PYTHON_PN}-ruamel.ordereddict \
	"

do_install_prepend() {
	export RUAMEL_NO_PIP_INSTALL_CHECK=1
}

