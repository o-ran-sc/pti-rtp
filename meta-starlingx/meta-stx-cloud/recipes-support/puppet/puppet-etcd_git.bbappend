
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/puppet-etcd-changes-for-poky-stx.patch \
	"
inherit openssl10
