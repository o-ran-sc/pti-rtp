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
