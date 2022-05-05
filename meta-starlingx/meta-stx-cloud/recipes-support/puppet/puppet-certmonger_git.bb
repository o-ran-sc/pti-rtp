
SUMMARY = "Deprecated: Use saltedsignal/puppet-certmonger instead."
DESCRIPTION = " \
	This puppet module allows you to request and manage certificates using certmonger. \
	"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5e8f921d6b978d3605270db1f39e199f"

PV = "1.1.1"
SRCREV = "1157a7e552d87696e80ed4ab54bf0608a1c5ffff"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/earsdown/puppet-certmonger.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://puppet-certmonger/Add-gemspec.patch \
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

RUBY_INSTALL_GEMS = "puppet-certmonger-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/certmonger
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/certmonger
}

FILES_${PN} += " ${datadir}"
