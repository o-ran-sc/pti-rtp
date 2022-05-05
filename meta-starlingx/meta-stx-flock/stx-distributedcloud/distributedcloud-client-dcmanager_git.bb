DESCRIPTION = " \
	Client library for Distributed Cloud built on the Distributed Cloud API. \
	It provides a command-line tool (dcmanager).  \
	Distributed Cloud provides configuration and management of distributed clouds \
	"

HOMEPAGE = "https://opendev.org/starlingx"
SECTION = "network"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://distributedcloud-client/LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCNAME = "distcloud-client"
SRCREV = "8a8f01dd3701d4793dd8cbc0147e4dca49cd7c03"
PV = "1.0.0+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = " \
	git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	"

inherit distutils python-dir

DEPENDS += " \
	python-pbr-native \
	"

do_configure() {
	cd ${S}/distributedcloud-client
	distutils_do_configure
}

do_compile() {
	cd ${S}/distributedcloud-client
	distutils_do_compile
}

do_install() {
	cd ${S}/distributedcloud-client
	distutils_do_install
}
