
DESCRIPTION = "Construct is a powerful declarative and symmetrical parser and builder for binary data"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3fd0f2c25089e629957285e6bc402a20"

SRCREV = "a6a45f09da003b278cc5955486ad17d9144c136b"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "v2.9.46+git${SRCPV}"

SRC_URI = "git://github.com/construct/construct.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python python-pbr-native"
inherit setuptools distutils pkgconfig
