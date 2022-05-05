
SUMMARY = "Puppet module for configuring collectd and plugins."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=03ff50a0bc4a15eb462d4129e89f9656"

PV = "5.1.0"
SRCREV = "f5a44db033c7cea9990c405519f70d18fc2b05c0"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/voxpupuli/puppet-collectd;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
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
	install -d -m 0755 ${D}/${datadir}/puppet/modules/collectd
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/collectd
}

FILES_${PN} += " ${datadir}"
