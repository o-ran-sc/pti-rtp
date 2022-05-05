
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/0001-puppet-ldap-add-os-poky-stx.patch \
	file://${BPN}/0002-puppet-ldap-poky-stx-fix-pkg-name.patch \
	"

inherit openssl10
