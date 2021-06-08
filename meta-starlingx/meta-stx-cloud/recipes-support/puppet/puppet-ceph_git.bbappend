inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "config/puppet-modules/openstack/puppet-ceph-2.2.0/centos/patches"

SRC_URI_STX += " \
	file://0001-Roll-up-TIS-patches.patch \
	file://0002-Newton-rebase-fixes.patch \
	file://0003-Ceph-Jewel-rebase.patch \
	file://0004-US92424-Add-OSD-support-for-persistent-naming.patch \
	file://0006-ceph-disk-prepare-invalid-data-disk-value.patch \
	file://0007-Add-StarlingX-specific-restart-command-for-Ceph-moni.patch \
	file://0008-ceph-mimic-prepare-activate-osd.patch \
	file://0009-fix-ceph-osd-disk-partition-for-nvme-disks.patch \
	file://0010-wipe-unprepared-disks.patch \
	"

SRC_URI += " \
	file://${BPN}/0005-Remove-puppetlabs-apt-as-ceph-requirement.patch \
	file://${BPN}/0011-puppet-ceph-changes-for-poky-stx.patch \
	"

inherit openssl10
