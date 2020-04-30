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

DESCRIPTION = "stx-ha"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCNAME = "ha"
SRCREV = "a7b7d35b9922a3f2a8462492b7f1958f135a612d"
S = "${WORKDIR}/git"
PV = "1.0.0"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
	file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://service-mgmt-api/sm-api/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://service-mgmt-client/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://service-mgmt-client/sm-client/LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2 \
	file://service-mgmt-tools/sm-tools/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://service-mgmt/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://service-mgmt/sm-common/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://service-mgmt/sm-db/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://service-mgmt/sm/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

SRC_URI = "git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://0001-Allow-user-to-define-destination-libdir.patch \
	file://0002-Install-sm-eru-sm-eru-dump-and-sm-eru-watchdog.patch \
	file://0003-pragma-ignore-Wunused-result-errors-with-gcc-8.3.patch \
	file://0004-Cast-size_t-to-int-to-silence-gcc-8.3.patch \
	"

inherit setuptools
inherit pkgconfig
inherit systemd

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

DEPENDS += " \
	stx-fault \
	stx-metal \
	sqlite3 \
	python \
	python-pbr-native \
	glib-2.0 \
	"

require sm-common.inc
require sm-db.inc
require sm.inc
require sm-api.inc
require sm-client.inc
require sm-tools.inc
require stx-ocf-scripts.inc

LDFLAGS_remove = "-Wl,--as-needed"

do_configure () {
	:
} 

do_compile() {
	:	
}

do_install () {
	:
}

FILES_${PN} = " "
FILES_${PN}-dev += " \
	${localstatedir}/lib/sm/watchdog/modules/libsm_watchdog_nfs.so \
	"

