inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "config/puppet-modules/openstack/${BP}/centos"

SRC_URI_STX += " \
	file://patches/0001-Roll-up-TIS-patches.patch \
	file://patches/0002-update-for-openstackclient-Train.patch \
	"

SRC_URI += " \
	file://${BPN}/puppet-openstacklib-updates-for-poky-stx.patch \
	"

inherit openssl10
