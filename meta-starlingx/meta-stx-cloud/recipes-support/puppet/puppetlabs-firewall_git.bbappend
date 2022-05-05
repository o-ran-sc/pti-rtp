
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://${BPN}/puppet-firewall-updates-for-poky-stx.patch \
	file://${BPN}/puppet-firewall-random-fully-support.patch \
	"

inherit openssl10
