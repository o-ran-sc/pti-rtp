
SUMMARY = "The oslo module is a part of OpenStack intended to provide continuous integration testing and code review."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86e96afa80a52eedda5dccc1af36b4f2"

PV = "11.3.0"
SRCREV = "5ad200e9d8af200a8f50f86a2db4cf3f36ab2a4c"
PROTOCOL = "https"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/openstack/puppet-oslo.git;protocol=${PROTOCOL};rev=${SRCREV};nobranch=1 \
	file://puppet-oslo/Add-gemspec.patch \
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

RUBY_INSTALL_GEMS = "puppet-oslo-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/oslo
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/oslo
}

FILES_${PN} += " ${datadir}"
