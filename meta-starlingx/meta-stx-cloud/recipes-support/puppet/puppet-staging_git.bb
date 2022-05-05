
SUMMARY = "Manages staging directory, along with download/extraction of compressed files."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=cdbf8d74b765504fbdf8e154bb4458a1"

PV = "1.0.4"
SRCREV = "bc434a71e19aae54223d57c274e2e1a7f9546d5e"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = " \
	git://github.com/nanliu/puppet-staging;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://puppet-staging/Add-gemspec.patch \
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

RUBY_BUILD_GEMS = "nanliu-staging.gemspec"
RUBY_INSTALL_GEMS = "nanliu-staging-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/staging
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/staging
}

FILES_${PN} += " ${datadir}"
