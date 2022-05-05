
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${PN}/0001-TIS-Patches.patch \
	file://${PN}/0002-Disable-timeout-for-mkfs-command.patch \
	file://${PN}/0003-drbd-parallel-to-serial-synchronization.patch \
	file://${PN}/0004-US-96914-reuse-existing-drbd-cinder-resource.patch \
	file://${PN}/0005-Add-PausedSync-states-to-acceptable-cstate.patch \
	file://${PN}/0006-CGTS-7164-Add-resource-options-cpu-mask-to-affine-drbd-kernel-threads.patch \
	file://${PN}/0007-Add-disk-by-path-test.patch \
	file://${PN}/0008-CGTS-7953-support-for-new-drbd-resources.patch \
	file://${PN}/0009-drbd-slow-before-swact.patch \
	"

inherit openssl10
