
DESCRIPTION = "The Python perf module is a toolkit to write, run and analyze benchmarks."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=78bc2e6e87c8c61272937b879e6dc2f8"

SRCREV = "7febee0242ce8fd01f56bce2ec72ec536a41caed"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "1.6.0+git${SRCPV}"

SRC_URI = "git://github.com/vstinner/pyperf.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


# DEPENDS += " python python-pbr-native"
DEPENDS += " python"
inherit setuptools distutils pkgconfig
