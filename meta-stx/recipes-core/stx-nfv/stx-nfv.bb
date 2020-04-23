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

DESCRIPTION = "stx-nfv"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV = "aaa932c00e028dcbaf0eed6843c4d3e51f09b2c1"
S = "${WORKDIR}/git"
PV = "1.0.0"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"


SRC_URI = "git://opendev.org/starlingx/nfv.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://use-ldflags-mtce-guest.patch \
	"

DEPENDS += " \
	python \
	python-pbr-native \
	stx-metal \
	stx-fault \
	json-c \
	openssl \
	libevent \
	libgcc \
	"

require mtce-guest.inc
require nfv-vim.inc
require nfv-common.inc
require nfv-client.inc
require nfv-plugins.inc
require nfv-tools.inc
require nova-api-proxy.inc

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
}

FILES_${PN} = " "
