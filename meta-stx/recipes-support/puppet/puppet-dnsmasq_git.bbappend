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
	file://${BPN}/0001-puppet-dnsmasq-Kilo-quilt-patches.patch;striplevel=5 \
	file://${BPN}/0002-Fixing-mismatched-permission-on-dnsmasq-conf.patch;striplevel=5 \
	file://${BPN}/0003-Support-management-of-tftp_max-option.patch;striplevel=5 \
	file://${BPN}/0004-Enable-clear-DNS-cache-on-reload.patch;striplevel=5 \
	file://${BPN}/0005-puppet-dnsmasq-updates-for-poky-stx.patch;striplevel=5 \
	"

inherit openssl10
