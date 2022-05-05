
DESCRIPTION = " \
	sphinxcontrib-websupport provides a Python API to easily integrate Sphinx\
	documentation into your Web application. \
	"
HOMEPAGE = "https://www.sphinx-doc.org/en/master/"
SECTION = "devel/python"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=819a10ab58e77e03e61a584de6339f7c"

SRC_URI[md5sum] = "ca6435e7b4eb9408df4f54972361e9d3"
SRC_URI[sha256sum] = "9de47f375baf1ea07cdb3436ff39d7a9c76042c10a769c52353ec46e4e8fc3b9"

PYPI_PACKAGE = "sphinxcontrib-websupport"
inherit setuptools pypi
