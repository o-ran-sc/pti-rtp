
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/0001-puppet-dnsmasq-Kilo-quilt-patches.patch;striplevel=5 \
	file://${BPN}/0002-Fixing-mismatched-permission-on-dnsmasq-conf.patch;striplevel=5 \
	file://${BPN}/0003-Support-management-of-tftp_max-option.patch;striplevel=5 \
	file://${BPN}/0004-Enable-clear-DNS-cache-on-reload.patch;striplevel=5 \
	file://${BPN}/0005-puppet-dnsmasq-updates-for-poky-stx.patch;striplevel=5 \
	"

inherit openssl10
