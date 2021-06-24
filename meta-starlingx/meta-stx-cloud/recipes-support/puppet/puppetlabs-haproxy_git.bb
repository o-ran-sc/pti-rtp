
SUMMARY = "Configures HAProxy servers and manages the configuration of backend member servers."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

PV = "1.5.0"
SRCREV = "3ac513c0ceb3bcfe35dd2936875189ccfc991a34"
PROTOCOL = "https"
BRANCH = "main"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/puppetlabs/puppetlabs-haproxy;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://puppetlabs-haproxy/Add-gemspec.patch \
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

RUBY_INSTALL_GEMS = "puppetlabs-haproxy-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/haproxy
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/haproxy
}

FILES_${PN} += " ${datadir}"
