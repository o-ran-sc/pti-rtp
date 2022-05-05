
DESCRIPTION = "JMESPath (pronounced “james path”) allows you to declaratively specify how to extract elements from a JSON document."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2683790f5fabb41a3f75b70558799eb4"

PV = "0.9.4"

SRCREV = "4a4f6dbd98549b2b8fc71bbc19860d317a6abfdb"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/jmespath/jmespath.py;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
