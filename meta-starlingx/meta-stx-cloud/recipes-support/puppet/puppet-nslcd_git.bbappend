
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/puppet-nslcd-updates-for-poky-stx.patch \
	"

RDEPENDS_${PN} += "nss-pam-ldapd"

inherit openssl10
