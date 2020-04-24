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

DESCRIPTION = "stx-utilities"

PROTOCOL = "https"
SRCNAME = "utilities"
BRANCH = "r/stx.3.0"
SRCREV = "cbad6b792157d066dd601f0f9ce62dc177d4c848"
S = "${WORKDIR}/git"
PV = "1.0.0"

LICENSE = "Apache-2.0"

#e7b3e2a120f5d4c0f6f562a52b6badf4  ./security/tpm2-openssl-engine/tpm2-openssl-engine/LICENSE
#3b83ef96387f14655fc854ddc3c6bd57  ./utilities/build-info/build-info-1.0/LICENSE
#3b83ef96387f14655fc854ddc3c6bd57  ./utilities/namespace-utils/LICENSE
#3b83ef96387f14655fc854ddc3c6bd57  ./utilities/namespace-utils/namespace-utils/LICENSE
#3b83ef96387f14655fc854ddc3c6bd57  ./utilities/nfscheck/LICENSE
#3b83ef96387f14655fc854ddc3c6bd57  ./utilities/nfscheck/files/LICENSE
#3b83ef96387f14655fc854ddc3c6bd57  ./utilities/pci-irq-affinity-agent/files/LICENSE
#3b83ef96387f14655fc854ddc3c6bd57  ./utilities/platform-util/platform-util/LICENSE
#3b83ef96387f14655fc854ddc3c6bd57  ./utilities/platform-util/scripts/LICENSE
#3b83ef96387f14655fc854ddc3c6bd57  ./utilities/stx-extensions/files/LICENSE
#3b83ef96387f14655fc854ddc3c6bd57  ./utilities/update-motd/LICENSE
#3b83ef96387f14655fc854ddc3c6bd57  ./utilities/update-motd/files/LICENSE
#3b83ef96387f14655fc854ddc3c6bd57  ./utilities/worker-utils/worker-utils/LICENSE

LIC_FILES_CHKSUM = " \
	file://ceph/ceph-manager/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://ceph/python-cephclient/python-cephclient/LICENSE;md5=41687b590435621fc0676ac02c51154f \
	file://security/stx-ssl/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://tools/collector/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://tools/collector/scripts/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://tools/engtools/hostdata-collectors/scripts/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://utilities/logmgmt/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://utilities/logmgmt/logmgmt/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"


SRC_URI = " \
	git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	"

inherit setuptools distutils python-dir systemd
DEPENDS = " \
	python-pip \
	python-pbr-native \
	systemd \
"

require utilities/build-info.inc
require utilities/logmgmt.inc
require utilities/namespace-utils.inc
require utilities/nfscheck.inc
require utilities/pci-irq-affinity.inc
require utilities/platform-util.inc
require utilities/stx-extensions.inc
require utilities/update-motd.inc
require utilities/worker-utils.inc
require ceph/ceph-manager.inc
require ceph/python-cephclient.inc
require security/stx-ssl.inc
# Skip tpm2-openssl-engine2
require tools/collector.inc
require tools/collect-engtools.inc

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
	:
}

FILES_${PN} = " "

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"
