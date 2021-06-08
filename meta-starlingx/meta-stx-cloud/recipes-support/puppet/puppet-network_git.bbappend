inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "config/puppet-modules/${BPN}/centos/files"

SRC_URI_STX += " \
	file://puppet-network-Kilo-quilt-changes.patch;striplevel=5 \
	file://puppet-network-support-ipv6.patch;striplevel=5 \
	file://Don-t-write-absent-to-redhat-route-files-and-test-fo.patch;striplevel=5 \
	file://fix-absent-options.patch;striplevel=5 \
	file://permit-inservice-update-of-static-routes.patch;striplevel=5 \
	file://ipv6-static-route-support.patch;striplevel=5 \
	file://route-options-support.patch;striplevel=5 \
	"

SRC_URI += " \
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
