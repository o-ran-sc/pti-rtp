
SUMMARY = "Puppet module for OpenStack Barbican"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fce88ac1cd1315adf28a52502c9f7f6b"

RDEPENDS_${PN} += " \
	puppetlabs-inifile \
	"

PV = "11.3.0"
SRCREV = "8241a1d13be6c3ee6344fa46dcfc045439044e76"
PROTOCOL = "https"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/openstack/puppet-barbican.git;protocol=${PROTOCOL};rev=${SRCREV};nobranch=1 \
	file://puppet-barbican/Add-gemspec.patch \
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

RUBY_INSTALL_GEMS = "puppet-barbican-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/barbican
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/barbican
}

FILES_${PN} += " ${datadir}"
