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
	file://${BPN}/0001-Roll-up-TIS-patches.patch \
	file://${BPN}/0002-Changed-cipher-specification-to-openssl-format.patch \
	file://${BPN}/0004-Partially-revert-upstream-commit-f7c3a4a637d59f3065d.patch \
	file://${BPN}/0005-Remove-the-rabbitmq_nodename-fact.patch \
	file://${BPN}/0007-init.pp-do-not-check-the-apt-resource.patch \
	file://${BPN}/0008-puppet-rabbitmq-poky.patch \
	file://${BPN}/0009-remove-apt-requirement.patch \
	"

inherit openssl10

DEPENDS_append = " puppet-staging"
