DESCRIPTION = "Library to implement a well-behaved Unix daemon process"
HOMEPAGE = "https://pagure.io/python-daemon/"
SECTION = "devel/python"

DEPENDS += "${PYTHON_PN}-docutils-native"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit pypi setuptools3

SRC_URI[md5sum] = "3ab10a93472201214cd95c05f1923af6"
SRC_URI[sha256sum] = "affeca9e5adfce2666a63890af9d6aff79f670f7511899edaddca7f96593cc25"

PYPI_PACKAGE = "python-daemon"

RDEPENDS_${PN} = "\
    ${PYTHON_PN}-docutils \
    ${PYTHON_PN}-lockfile (>= 0.10) \
    ${PYTHON_PN}-resource \
"
