
SUMMARY = "Manages Firewalls such as iptables"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

PV = "1.8.2"
SRCREV = "23016934d23c5c2f3f3edbc2ec8279f8faac2457"
PROTOCOL = "https"
BRANCH = "main"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/puppetlabs/puppetlabs-firewall;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://puppetlabs-firewall/Add-gemspec.patch \
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

RUBY_INSTALL_GEMS = "puppetlabs-firewall-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/firewall
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/firewall
}

FILES_${PN} += " ${datadir}"
