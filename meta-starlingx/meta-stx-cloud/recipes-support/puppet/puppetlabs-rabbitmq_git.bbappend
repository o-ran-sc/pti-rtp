
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://${BPN}/0001-Roll-up-TIS-patches.patch \
	file://${BPN}/0002-Changed-cipher-specification-to-openssl-format.patch \
	file://${BPN}/0004-Partially-revert-upstream-commit-f7c3a4a637d59f3065d.patch \
	file://${BPN}/0005-Remove-the-rabbitmq_nodename-fact.patch \
	file://${BPN}/0007-init.pp-do-not-check-the-apt-resource.patch \
	file://${BPN}/0008-puppet-rabbitmq-poky.patch \
	file://${BPN}/0009-remove-apt-requirement.patch \
	"

inherit openssl10

DEPENDS_append = " puppet-staging"
