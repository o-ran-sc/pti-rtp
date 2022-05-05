
SUMMARY = "Intended managing the entirety of horizon."
DESCRIPTION = " \
	The horizon module is a thorough attempt to make Puppet capable of \
	managing the entirety of horizon. Horizon is a fairly classic django \
	application, which results in a fairly simply Puppet module.\
	"

HOMEPAGE = "https://github.com/saz/puppet-memcached.git"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1322039bbc8e3ff4e74252ec65303861"

PV = "3.0.2"
SRCREV = "c2a0b543dc28f34ab68e905ede3173b00246ddca"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = " \
	git://github.com/saz/puppet-memcached.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://puppet-memcached/Add-gemspec.patch \
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

RUBY_INSTALL_GEMS = "puppet-memcached-${PV}.gem"

do_install_append() { 
	install -d -m 0755 ${D}/${datadir}/puppet/modules/memcached
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/memcached
}

FILES_${PN} += " ${datadir}"
