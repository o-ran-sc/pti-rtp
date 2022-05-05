
DESCRIPTION = "Kubernetes python client"
HOMEPAGE = "https://pypi.org/project/kubernetes/8.0.0"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ad09685d909e7a9f763d2bb62d4bd6fb"

SRC_URI[md5sum] = "c1d6d0ac57a8a49c5fd383a39ee9ab4b"
SRC_URI[sha256sum] = "54f8e7bb1dd9a55cf416dff76a63c4ae441764280942d9913f2243676f29d02c"

inherit setuptools pypi

RDEPENDS_${PN}_append = " \
	${PYTHON_PN}-adal \
	${PYTHON_PN}-google-auth \
	${PYTHON_PN}-requests-oauthlib \
	"
