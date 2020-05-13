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

DESCRIPTION = "stx-update"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV = "2542c5539bab060830009d02cbb257cc8bf4a376"
S = "${WORKDIR}/git"
PV = "1.0.0"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = " \
	git://opendev.org/starlingx/update.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://0001-Remove-use-of-rpmUtils.miscutils-from-cgcs-patch.patch \
	file://0002-Cleanup-smartpm-references.patch \
	file://0003-Cleaning-up-pylint-settings-for-cgcs-patch.patch \
	file://0004-Address-python3-pylint-errors-and-warnings.patch \
	file://0005-Clean-up-pylint-W1201-logging-not-lazy-in-cgcs-patch.patch \
	file://0006-Migrate-patch-agent-to-use-DNF-for-swmgmt.patch \
	file://0007-patch_agent-do-not-do-the-packages_iter-if-pkggrp-is.patch \
	"

DEPENDS = " \
	python \
	python-pbr-native \
	"

RDEPENDS_${PN} += " python-requests-toolbelt"

require cgcs-patch.inc
require enable-dev-patch.inc
require patch-alarm.inc

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

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"
