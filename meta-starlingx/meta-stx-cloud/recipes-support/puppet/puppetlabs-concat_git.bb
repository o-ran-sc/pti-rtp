
DESCRIPTION = "Construct files from multiple fragments."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

PV = "2.2.0"
SRCREV = "fdf4a84534ccb1b9ae8ffb654c7e160a31e294ee"
PROTOCOL = "https"
BRANCH = "main"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/puppetlabs/puppetlabs-concat.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
    file://puppetlabs-concat/Add-gemspec.patch \
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

RUBY_INSTALL_GEMS = "puppetlabs-concat-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/concat
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/concat
}

FILES_${PN} += " ${datadir}"
