
SUMMARY = "Resource types for managing settings in INI files"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

PV = "1.6.0"
SRCREV = "88bf9868b532ddf556bdb617f67eda9de0b8dc0f"
PROTOCOL = "https"
BRANCH = "main"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/puppetlabs/puppetlabs-inifile;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://puppetlabs-inifile/Add-gemspec.patch \
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

RUBY_INSTALL_GEMS = "puppetlabs-inifile-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/inifile
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/inifile
}

FILES_${PN} += " ${datadir}"
