
DESCRIPTION = "puppet-drbd"

PV = "0.3.1"
SRCREV = "496b3ba9cd74a2d12636f9e90a718739a5451169"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=6089b6bd1f0d807edb8bdfd76da0b038 "

SRC_URI = " \
	git://github.com/voxpupuli/puppet-drbd;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
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
	install -d -m 0755 ${D}/${datadir}/puppet/modules/drbd
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/drbd
}

FILES_${PN} += "${datadir}"
