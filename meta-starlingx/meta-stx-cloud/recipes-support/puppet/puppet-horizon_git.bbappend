
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/0001-Update-memcached-dependency.patch \
	file://${BPN}/0002-puppet-horizon-changes-for-poky-stx.patch \
	"
inherit openssl10
