
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/0001-Stx-uses-nanliu-staging-module.patch \
	file://${BPN}/0002-puppet-mysql-changes-for-poky-stx.patch \
	file://${BPN}/0003-puppet-mysqltuner-adjust-path.patch \
	"

inherit openssl10
