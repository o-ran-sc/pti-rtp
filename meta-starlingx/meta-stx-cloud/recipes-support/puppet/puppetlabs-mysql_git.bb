
SUMMARY = "The mysql module installs, configures, and manages the MySQL service."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

PV = "3.11.0"
SRCREV = "920dd76214d87d9b26f0db105886ee89ac266c4e"
PROTOCOL = "https"
BRANCH = "main"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/puppetlabs/puppetlabs-mysql.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
    file://puppetlabs-mysql/Add-gemspec.patch \
    "
#   file://puppetlabs-mysql/0001-Fix-ruby-path.patch

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

RUBY_INSTALL_GEMS = "puppetlabs-mysql-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/mysql
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/mysql
}

FILES_${PN} += " ${datadir}"
