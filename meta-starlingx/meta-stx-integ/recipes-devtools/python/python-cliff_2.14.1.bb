
DESCRIPTION = "Command Line Interface Formulation Framework"
HOMEPAGE = "https://github.com/dreamhost/cliff"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI[md5sum] = "4c5d43d98970c79b091e25676cce2b10"
SRC_URI[sha256sum] = "b47387a344898ccb28ca7f386f017ade9eb66dc1713e5c642a0bc09ec606cc67"

inherit setuptools pypi

DEPENDS += "\
    python-pbr \
    "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
    python-pbr-native \
    "

RDEPENDS_${PN} += "python-prettytable \
            python-cmd2 \
            python-pbr \
            python-pyparsing \
            python-prettytable \
            python-six \
            python-stevedore \
            python-unicodecsv \
            python-pyyaml \
"

CLEANBROKEN = "1"
