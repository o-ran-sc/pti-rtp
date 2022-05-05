
DESCRIPTION = " \
	Snowball is a small string processing language designed for creating stemming algorithms \
	for use in Information Retrieval. This site describes Snowball, and presents several useful \
	stemmers which have been implemented using it. \
	"
HOMEPAGE = "https://github.com/snowballstem/snowball"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=2750797da77c1d784e7626b3f7d7ff3e"

SRC_URI[md5sum] = "c05ec4a897be3c953c8b8b844c4241d4"
SRC_URI[sha256sum] = "df3bac3df4c2c01363f3dd2cfa78cce2840a79b9f1c2d2de9ce8d31683992f52"

PYPI_PACKAGE = "snowballstemmer"
inherit setuptools pypi
