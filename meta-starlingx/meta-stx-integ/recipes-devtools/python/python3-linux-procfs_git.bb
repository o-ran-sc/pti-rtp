
DESCRIPTION = "Python classes to extract information from the Linux kernel /proc files"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b"


SRCREV = "d170839360edf3fbac4a31c7771c4ec5bb0b6121"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "0.6.1"

SRC_URI = "git://git.kernel.org/pub/scm/libs/python/python-linux-procfs/python-linux-procfs.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
RDEPENDS_${PN} = "python3-core"

inherit setuptools3 distutils3 pkgconfig
