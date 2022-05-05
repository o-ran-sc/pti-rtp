
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/puppetlabs-apache-updates-for-poky-stx.patch \
	"
#SRC_URI += " \
#	file://${BPN}/0001-maint-Fix-conditional-in-vhost-ssl-template.patch \
#	file://${BPN}/0002-maint-Fix-the-vhost-ssl-template-correctly-this-time.patch \
#	"

RDEPENDS_${PN} += " \
	apache2 \
	mod-wsgi \
	"

inherit openssl10
