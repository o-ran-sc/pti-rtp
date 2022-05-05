
DESCRIPTION = "puppet module for dnsmasq"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://README.md;md5=71e3bfa9ffc5e93324727bbffae917f5"

PV = "1.1.0"
STABLE = "master"
PROTOCOL = "https"
BRANCH = "master"
SRCREV = "cff07e90890662972c97684a2baee964f68ff3ed"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/netmanagers/puppet-dnsmasq;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://${PN}/Add-gemspec.patch \
	file://${PN}/metadata.json.patch \
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

do_install_append () {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/dnsmasq
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/dnsmasq/
}

FILES_${PN} += " ${datadir}"
