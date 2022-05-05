DESCRIPTION = "Highly concurrent networking library"
HOMEPAGE = "http://pypi.python.org/pypi/eventlet"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=56472ad6de4caf50e05332a34b66e778"

SRC_URI[md5sum] = "8d1c646d8cc10a4958c92fe8a30f3676"
SRC_URI[sha256sum] = "74ef11d67ee5e85e009b0fced733c907620bca1ab8e6b0489d9f247405ab2685"

inherit setuptools pypi

RDEPENDS_${PN} += " \
    python-enum34 \
    python-enum-compat \
    python-greenlet \
    "
