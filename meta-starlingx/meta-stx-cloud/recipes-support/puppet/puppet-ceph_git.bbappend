
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${BPN}/0001-Roll-up-TIS-patches.patch \
	file://${BPN}/0002-Newton-rebase-fixes.patch \
	file://${BPN}/0003-Ceph-Jewel-rebase.patch \
	file://${BPN}/0004-US92424-Add-OSD-support-for-persistent-naming.patch \
	file://${BPN}/0005-Remove-puppetlabs-apt-as-ceph-requirement.patch \
	file://${BPN}/0006-ceph-disk-prepare-invalid-data-disk-value.patch \
	file://${BPN}/0007-Add-StarlingX-specific-restart-command-for-Ceph-moni.patch \
	file://${BPN}/0008-ceph-mimic-prepare-activate-osd.patch \
	file://${BPN}/0009-fix-ceph-osd-disk-partition-for-nvme-disks.patch \
	file://${BPN}/0010-wipe-unprepared-disks.patch \
	file://${BPN}/0011-puppet-ceph-changes-for-poky-stx.patch \
	"
inherit openssl10
