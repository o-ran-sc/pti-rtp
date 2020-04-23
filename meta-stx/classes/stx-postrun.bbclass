#
## Copyright (C) 2019 Wind River Systems, Inc.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

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
