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

FILESEXTRAPATHS_append := ":${THISDIR}/files:"

inherit openssl10
DEPENDS_append = " openssl"

SRC_URI += " \
	file://0001-ps.patch \
	file://0002-personality.patch \
	file://0003-centos_remove-net-commands-that-can-timeout.patch;striplevel=2 \
	file://0004-centos_fix-ipv6-regex.patch \
	file://0005-Hardcode-ipaddress-fact-to-localhost.patch \
	file://0006-facter-updates-for-poky-stx.patch \
	"
