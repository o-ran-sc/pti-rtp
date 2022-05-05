
DESCRIPTION = "YAQL (Yet Another Query Language) is an embeddable and extensible query language, that allows performing complex queries against arbitrary objects. It has a vast and comprehensive standard library of frequently used querying functions and can be extend even further with user-specified functions. YAQL is written in python and is distributed via PyPI."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

SRCREV = "7385a31b9dbfc777a514ba28fe507fbf904cc779"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "1.1.3+git${SRCPV}"

SRC_URI = "git://github.com/openstack/yaql.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python python-pbr-native"
inherit setuptools distutils pkgconfig
