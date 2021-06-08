inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "config/puppet-modules/${BPN}/centos/files"

SRC_URI_STX += " \
	file://0001-puppet-dnsmasq-Kilo-quilt-patches.patch;striplevel=5 \
	file://0002-Fixing-mismatched-permission-on-dnsmasq-conf.patch;striplevel=5 \
	file://0003-Support-management-of-tftp_max-option.patch;striplevel=5 \
	file://0004-Enable-clear-DNS-cache-on-reload.patch;striplevel=5 \
	"

SRC_URI += " \
	file://${BPN}/0005-puppet-dnsmasq-updates-for-poky-stx.patch;striplevel=5 \
	"

inherit openssl10
