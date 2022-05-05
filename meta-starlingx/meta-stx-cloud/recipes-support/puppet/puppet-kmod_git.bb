
SUMMARY = "Manage Linux kernel modules with Puppet"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0e5ccf641e613489e66aa98271dbe798"

PV = "2.1.0"
SRCREV = "0d69a96e8d0d3a08da0d5f476c733134df4fb9ee"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/camptocamp/puppet-kmod;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://${PN}/Add-gemspec.patch \
	file://${PN}/persistent-module-load-poky.patch \
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

RUBY_BUILD_GEMS = "camptocamp-kmod.gemspec"
RUBY_INSTALL_GEMS = "camptocamp-kmod-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/kmod
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/kmod
}

FILES_${PN} += " ${datadir}"
