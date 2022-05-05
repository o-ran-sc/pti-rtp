
SUMMARY = "Installs and configures Puppi."
DESCRIPTION = " \
	This module provides the Puppi libraries required by Example42 modules and, if explicitely included, the \
	puppi command, its working environment, the defines and procedures to deploy applications.\
	"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a300b604c66de62cf6e923cca89c9d83"

PV = "2.2.3"
SRCREV = "c1c47f4edfd761d1bbde32a75da0c3fa7cc93a81"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = " \
	git://github.com/example42/puppi;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://${PN}/Add-gemspec.patch \
	" 

inherit ruby 

DEPENDS += " \
	ruby \
	facter \
	"

RDEPENDS_${PN} += " \
	ruby \
	facter \
	puppet \
	perl \
	"

RUBY_INSTALL_GEMS = "${PN}-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/puppi
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/puppi
}

FILES_${PN} += " ${datadir}"
