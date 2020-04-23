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

DESCRIPTION = "stx-metal"

# STABLE = "starlingx/master"
PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV = "be3cf4eeb50eef55910cf9c73ea47c168005ad64"
S = "${WORKDIR}/git"
PV = "1.0.0"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
	file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://installer/pxe-network-installer/pxe-network-installer/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://kickstart/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://mtce-common/src/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://mtce-compute/src/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://mtce-control/src/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://mtce-storage/src/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://mtce/src/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://inventory/inventory/LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2 \
	file://python-inventoryclient/inventoryclient/LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2 \
	"


SRC_URI = "git://opendev.org/starlingx/metal.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
		file://0001-mtce-compute-dont-install-empty-directory-unless-nee.patch \
		file://0001-mtce-control-dont-install-empty-directory-unless-nee.patch \
		file://0001-mtce-storage-dont-install-empty-directory-unless-nee.patch \
		file://0001-Use-snprintf-to-avoid-overflowing-amon.tx_buf.patch \
		file://0001-Use-LDFLAGS-when-linking-and-pass-flags-down-to-subm.patch \
		file://0001-stx-metal-remove-argparse-requirement-from-inventory.patch \
		file://stx-warrior-adjust-paths.patch \
		"

inherit setuptools


DEPENDS = " \
	python \
	python-pbr-native \
	stx-fault \
	openssl \
	libevent \
	json-c \
	"
RDEPENDS_${PN}_append = " bash"

require mtce.inc
require inventory.inc
require mtce-common.inc
require mtce-compute.inc
require mtce-control.inc
require mtce-storage.inc
require python-inventoryclient.inc
require pxe-network-installer.inc
# require kickstart.inc

do_configure () {
	:
} 

do_compile() {
	:
}

do_install () {
	:
}

pkg_postinst_ontarget_${PN} () {
# install default config files
}

FILES_${PN} = " "
FILES_${PN}-dbg_append += " "
FILES_${PN}-staticdev_append = " "
FILES_${PN}-dev_append = " "
