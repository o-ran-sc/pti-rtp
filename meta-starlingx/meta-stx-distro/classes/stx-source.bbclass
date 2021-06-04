# This class is usd for creating source recipe for StarlingX repos,
# which will be used as a shared work directory with other recipes

STX_REPO ?= "integ"
OVERRIDES .= ":${STX_REPO}"

PROTOCOL = "https"
STX_URI = "git://opendev.org/starlingx/${STX_REPO}.git"

S = "${WORKDIR}/git"
PV = "1.0.0+git${SRCPV}"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "${STX_URI};protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

deltask do_configure
deltask do_compile
deltask do_install
deltask do_populate_sysroot
deltask do_populate_lic

inherit nopackages

WORKDIR = "${TMPDIR}/work-shared/${PN}"

STAMP = "${STAMPS_DIR}/work-shared/${PN}"
STAMPCLEAN = "${STAMPS_DIR}/work-shared/${PN}-*"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = ""
PACKAGES = ""

EXCLUDE_FROM_WORLD = "1"
RM_WORK_EXCLUDE += "${PN}"
