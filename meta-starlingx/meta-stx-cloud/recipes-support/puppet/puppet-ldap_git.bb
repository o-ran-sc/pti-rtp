
SUMMARY = "OpenLDAP module for Puppet."

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://metadata.json;md5=4244fe391bee02e9ee7259aa7f8dda8b"

PV = "0.2.4"
SRCREV = "480f13af6d17d1d3fcf0dc7b4bd04b49fa4099e1"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = " \
	git://github.com/torian/puppet-ldap;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://puppet-ldap/Add-gemspec.patch \
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
	install -d -m 0755 ${D}/${datadir}/puppet/modules/ldap
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/ldap
}

FILES_${PN} += " ${datadir}"
