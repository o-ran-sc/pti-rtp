
FILESEXTRAPATHS_prepend := "${THISDIR}/files/python-pynacl:"
DESCRIPTION = "Python binding to libsodiom"

HOMEPAGE = "https://pypi.org/project/PyNaCl/"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8cc789b082b3d97e1ccc5261f8594d3f"

SRCREV = "4881c878c9a33f4684337f650355bdf7f031d77d"
PROTOCOL = "https"
BRANCH = "main"
S = "${WORKDIR}/git"

PV = "1.3.0+git${SRCPV}"

SRC_URI = " \
	git://github.com/pyca/pynacl.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://${PN}/0001-Enable-cross-compile.patch \
	"


inherit setuptools distutils

DEPENDS += " libsodium python-cffi-native libsodium-native"
RDEPENDS_${PN} = " libsodium"

do_compile_prepend() {
	export PYNACL_CROSS_BUILD="--build=${BUILD_SYS}"
	export PYNACL_CROSS_HOST="--host=${HOST_SYS}"
	export PYNACL_CROSS_TARGET="--target=${TARGET_SYS}"
	export SODIUM_INSTALL="system"
}

do_install_prepend() {
	export SODIUM_INSTALL="system"
}
