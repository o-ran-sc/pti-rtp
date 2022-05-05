
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${PN}/0001-puppet-lvm-kilo-quilt-changes.patch;striplevel=5 \
	file://${PN}/0002-UEFI-pvcreate-fix.patch;striplevel=5 \
	file://${PN}/0003-US94222-Persistent-Dev-Naming.patch;striplevel=5 \
	file://${PN}/0004-extendind-nuke_fs_on_resize_failure-functionality.patch;striplevel=5 \
	file://${PN}/Fix-the-logical-statement-for-nuke_fs_on_resize.patch;striplevel=5 \
	"
RDEPENDS_${PN} += " \
	lvm2 \
	lvm2-scripts \
	lvm2-udevrules \
	"

inherit openssl10
