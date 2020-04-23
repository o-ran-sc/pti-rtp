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

SUMMARY = " StarlingX Single Server"

LICENSE = "MIT"

CORE_IMAGE_EXTRA_INSTALL = " \
	packagegroup-basic \
	packagegroup-core-base-utils  \
	"

IMAGE_INSTALL_append = " \
	${CORE_IMAGE_BASE_INSTALL} \
	packagegroup-core-full-cmdline \
	packagegroup-core-lsb \
	packagegroup-stx-upstream \
	packagegroup-stx-puppet \
	packagegroup-stx-fault \
	packagegroup-stx-metal \
	packagegroup-stx-nfv \
	packagegroup-stx-monitoring \
	packagegroup-stx-ha \
	packagegroup-stx-config-files \
	packagegroup-stx-update \
	packagegroup-stx-integ \
	packagegroup-stx-config \
	packagegroup-stx-distributedcloud \
	packagegroup-stx-utilities \
	packagegroup-stx-armada-app \
	starlingx-dashboard \
	playbookconfig \
	distributedcloud-client-dcmanager \
	registry-token-server \
	"

IMAGE_FEATURES += " \
	package-management \
	ssh-server-openssh \
	"

inherit stx-postrun
inherit core-image
inherit distro_features_check
inherit openstack-base
inherit identity
inherit monitor

# We need docker-ce
PACKAGE_EXCLUDE += " docker"
