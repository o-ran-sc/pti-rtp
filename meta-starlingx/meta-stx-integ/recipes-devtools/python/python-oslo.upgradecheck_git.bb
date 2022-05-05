
SUMMARY = "Common code for writing OpenStack upgrade checks"
DESCRIPTION = "\
  This project contains the common code necessary for writing upgrade checks \
  in OpenStack projects. It includes a module (oslo_upgradecheck.upgradecheck) \
  for the common code as well as an example (oslo_upgradecheck.__main__) of \
  integrating that code into a project. \
"
HOMEPAGE = "https://github.com/openstack/oslo.upgradecheck"
SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

PV = "0.3.1+git${SRCPV}"
SRCREV = "5f182fe19cdfe0bcf0d51bcf7be05e7a74f0a068"

SRCNAME = "oslo.upgradecheck"
SRC_URI = "git://github.com/openstack/${SRCNAME}.git;branch=master"

S = "${WORKDIR}/git"

inherit setuptools

DEPENDS += " \
    python-pip \
    python-pbr-native \
"

RDEPENDS_${PN} += " \
    python-oslo.config \
    python-oslo.i18n \
"
