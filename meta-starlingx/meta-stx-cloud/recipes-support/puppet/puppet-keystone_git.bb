
SUMMARY = "Puppet module for OpenStack Keystone"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=12a15a9ebddda7d856c783f745e5ee47"

PV = "11.3.0"
SRCREV = "305c91cac00f720ad6461b442e71b52b12f9ae57"
PROTOCOL = "https"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/openstack/puppet-keystone.git;protocol=${PROTOCOL};rev=${SRCREV};nobranch=1 \
	file://puppet-keystone/Add-gemspec.patch \
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

RUBY_INSTALL_GEMS = "puppet-keystone-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/keystone
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/keystone
}

FILES_${PN} += " ${datadir}"
