
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/0001-Roll-up-TIS-patches.patch \
	file://${BPN}/0002-puppet-openstacklib-updates-for-poky-stx.patch \
	"

inherit openssl10
