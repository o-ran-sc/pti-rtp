
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://${PN}/0001-puppet-certmonger-adjust-path-to-poky-rootfs.patch"

inherit openssl10
