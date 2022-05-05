
DESCRIPTION = " \
	This module analyzes JPEG/JPEG 2000/PNG/GIF/TIFF/SVG image headers and returns image size. \
	"
HOMEPAGE = "https://github.com/shibukawa/imagesize_py"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=0c128f0f7e8a02e1b83884c0b5a41cda"

SRC_URI[md5sum] = "3a1e124594183778a8f87e4bcdb6dca9"
SRC_URI[sha256sum] = "b1f6b5a4eab1f73479a50fb79fcf729514a900c341d8503d62a62dbc4127a2b1"

PYPI_PACKAGE = "imagesize"
inherit setuptools pypi
