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
	file://${BPN}/puppet-network-Kilo-quilt-changes.patch;striplevel=5 \
	file://${BPN}/puppet-network-support-ipv6.patch;striplevel=5 \
	file://${BPN}/Don-t-write-absent-to-redhat-route-files-and-test-fo.patch;striplevel=5 \
	file://${BPN}/fix-absent-options.patch;striplevel=5 \
	file://${BPN}/permit-inservice-update-of-static-routes.patch;striplevel=5 \
	file://${BPN}/ipv6-static-route-support.patch;striplevel=5 \
	file://${BPN}/route-options-support.patch;striplevel=5 \
	file://${BPN}/0001-Stx-uses-puppet-boolean-instead-of-adrien-boolean.patch \
	file://${BPN}/puppet-network-updates-for-poky-stx.patch \
	file://${BPN}/puppet-network-config-poky-provider.patch \
	" 

inherit openssl10

do_configure_append() {
	rm -f spec/fixtures/modules/network/files
}

RDEPENDS_${PN} += "\
	vlan \
"
