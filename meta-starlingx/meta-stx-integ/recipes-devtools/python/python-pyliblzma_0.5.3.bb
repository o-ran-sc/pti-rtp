SUMMARY = "Python bindings for liblzma"
DESCRIPTION = "\
PylibLZMA provides a python interface for the liblzma library to read and write data \
that has been compressed or can be decompressed by Lasse Collin’s xz / lzma utils. \
"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=58c39b26c0549f8e1bb4122173f474cd"

SRC_URI[md5sum] = "500f61116ee1ab4063b49c121786863a"
SRC_URI[sha256sum] = "08d762f36d5e59fb9bb0e22e000c300b21f97e35b713321ee504cfb442667957"

DEPENDS = "xz"

inherit pypi setuptools

PYPI_PACKAGE = "pyliblzma"
PYPI_PACKAGE_EXT = "tar.bz2"

BBCLASSEXTEND = " native"
