
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/puppet-puppi-updates-for-poky-stx.patch \
	file://${BPN}/puppet-puppi-adjust-path.patch \
	"

inherit openssl10
