
SUMMARY = "This modules allows you to configure sysctl."

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://README.md;md5=b5335702ab6b120493cf88faaf9be346"

PV = "0.0.11"
SRCREV = "65ffe839a4ce785bc3901452488197a0ef158cd8"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/duritong/puppet-sysctl;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
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

RUBY_BUILD_GEMS = "duritong-sysctl.gemspec"
RUBY_INSTALL_GEMS = "duritong-sysctl-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/sysctl
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/sysctl
}

FILES_${PN} += " ${datadir}"
