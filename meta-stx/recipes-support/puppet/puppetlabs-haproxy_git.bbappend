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
	file://puppetlabs-haproxy/0001-Roll-up-TIS-patches.patch \
	file://puppetlabs-haproxy/0002-disable-config-validation-prechecks.patch \
	file://puppetlabs-haproxy/0003-Fix-global_options-log-default-value.patch \
	file://puppetlabs-haproxy/0004-Stop-invalid-warning-message \
	"

inherit openssl10
