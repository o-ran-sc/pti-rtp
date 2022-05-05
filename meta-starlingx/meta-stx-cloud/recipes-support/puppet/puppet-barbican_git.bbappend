
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/puppet-barbican-fix-the-pkg-and-service-names-for-poky-stx.patch \
	file://${BPN}/puppet-barbican-do-not-fail-for-poky-stx.patch \
	"
inherit openssl10
