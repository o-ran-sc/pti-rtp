
DESCRIPTION = "A high-level cross-protocol url-grabber."
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=68ad62c64cc6c620126241fd429e68fe"
HOMEPAGE = "http://urlgrabber.baseurl.org/"

SRC_URI[sha256sum] = "79c5a01c5dd31906a7f38ef1f500030e137704804d585644693d3e474ed15f39"

DEPENDS += "python-six python-six-native"
PYPI_PACKAGE = "urlgrabber"
inherit pypi setuptools

FILES_${PN}_append = " ${datadir}"
