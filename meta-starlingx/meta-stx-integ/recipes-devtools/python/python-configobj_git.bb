
DESCRIPTION = "ConfigObj is a simple but powerful config file reader and writer: an ini file round tripper. Its main feature is that it is very easy to use, with a straightforward programmer’s interface and a simple syntax for config files."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9a851af36881461de16b8fecf59a9e17"

SRCREV = "45fbf1b85b181853caea4d251e6d4c0232735e85"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "5.0.6+git${SRCPV}"

SRC_URI = "git://github.com/DiffSK/configobj;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
