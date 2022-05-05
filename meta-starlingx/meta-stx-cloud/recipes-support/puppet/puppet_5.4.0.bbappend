
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/puppet-updates-for-poky-stx.patch \
	file://${BPN}/puppet-poky-yum.patch \
	"
