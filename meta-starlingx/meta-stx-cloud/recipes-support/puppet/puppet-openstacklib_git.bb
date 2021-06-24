
SUMMARY = "Puppet OpenStack Libraries."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=12a15a9ebddda7d856c783f745e5ee47"

PV = "11.3.0"
SRCREV = "79a799f5d78667b5eee81e71782e8591f2e62ecc"
PROTOCOL = "https"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/openstack/puppet-openstacklib.git;protocol=${PROTOCOL};rev=${SRCREV};nobranch=1 \
	file://puppet-openstacklib/Add-gemspec.patch \
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

RUBY_INSTALL_GEMS = "puppet-openstacklib-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/openstacklib
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/openstacklib
}

FILES_${PN} += " ${datadir}"
