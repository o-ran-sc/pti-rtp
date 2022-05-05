
# This class is intended to include workarounds and finetuning of the rootfs
# Most of the stuff in here needs to go else where.

ROOTFS_POSTPROCESS_COMMAND_append = " stx_postprocess_rootfs;"
# ETHDEV = "enp0s5"

stx_postprocess_rootfs() {

	# Issue: #83 /dev/root does not exist
	# This workaround is to be removed once initramfs is added

	cat > ${IMAGE_ROOTFS}/etc/udev/rules.d/99-dev-root-symlink.rules << \EOF
KERNEL=="sda3", SYMLINK+="root"
EOF
}
