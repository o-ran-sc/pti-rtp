
DESCRIPTION = "d2to1 (the ‘d’ is for ‘distutils’) allows using distutils2-like setup.cfg files for a package’s metadata with a distribute/setuptools setup.py script. It works by providing a distutils2-formatted setup.cfg file containing all of a package’s metadata, and a very minimal setup.py which will slurp its arguments from the setup.cfg."

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ec5c9882880dafb7f5a0b8d2642ea581"

SRCREV = "d16d8fdcec77a6d3fcbd10ce23aa8810d8dd3bf2"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "0.2.12.post1+git${SRCREV}"

SRC_URI = "git://github.com/embray/d2to1;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
