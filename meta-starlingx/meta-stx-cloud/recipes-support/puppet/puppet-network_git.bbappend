
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/puppet-network-Kilo-quilt-changes.patch;striplevel=5 \
	file://${BPN}/puppet-network-support-ipv6.patch;striplevel=5 \
	file://${BPN}/Don-t-write-absent-to-redhat-route-files-and-test-fo.patch;striplevel=5 \
	file://${BPN}/fix-absent-options.patch;striplevel=5 \
	file://${BPN}/permit-inservice-update-of-static-routes.patch;striplevel=5 \
	file://${BPN}/ipv6-static-route-support.patch;striplevel=5 \
	file://${BPN}/route-options-support.patch;striplevel=5 \
	file://${BPN}/0001-Stx-uses-puppet-boolean-instead-of-adrien-boolean.patch \
	file://${BPN}/puppet-network-updates-for-poky-stx.patch \
	file://${BPN}/puppet-network-config-poky-provider.patch \
	file://${BPN}/puppet-network-poky-stx.rb-add-vlan-support.patch \
	" 

inherit openssl10

do_configure_append() {
	rm -f spec/fixtures/modules/network/files
}

RDEPENDS_${PN} += "\
	vlan \
"
