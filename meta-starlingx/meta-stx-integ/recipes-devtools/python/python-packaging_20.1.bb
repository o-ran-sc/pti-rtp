
DESCRIPTION = " \
	Core utilities for Python packages. \
	The packaging project includes the following: version handling, specifiers, markers, requirements, tags, utilities. \
	"
HOMEPAGE = "https://github.com/pypa/packaging"
SECTION = "devel/python"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=faadaedca9251a90b205c9167578ce91"

SRC_URI[md5sum] = "a02ce566f10c701b4c42e39a4ce59c93"
SRC_URI[sha256sum] = "e665345f9eef0c621aa0bf2f8d78cf6d21904eef16a93f020240b704a57f1334"

PYPI_PACKAGE = "packaging"
inherit setuptools pypi
