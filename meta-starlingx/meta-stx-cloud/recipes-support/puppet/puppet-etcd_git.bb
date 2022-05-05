
SUMMARY = "Installs and configures etcd"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"

PV = "1.11.0"
SRCREV = "f43e1292a9554766f799cd5a14b67cc19ce5b00e"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/cristifalcas/puppet-etcd;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://puppet-etcd/Add-gemspec.patch \
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

RUBY_INSTALL_GEMS = "puppet-etcd-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/etcd
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/etcd
}

FILES_${PN} += " ${datadir}"
