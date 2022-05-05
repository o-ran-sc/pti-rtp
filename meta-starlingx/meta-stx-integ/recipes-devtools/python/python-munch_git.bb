
DESCRIPTION = "Munch is a dictionary that supports attribute-style access, a la JavaScript."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=f0291cd32b2d6a91d9b19970c94d0a46"

SRCREV = "d0fbbce7b8205a243337faed0b61472dfe09706d"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "2.3.2+git${SRCPV}"

SRC_URI = "git://github.com/Infinidat/munch.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

DEPENDS += " python"
inherit setuptools distutils pkgconfig
