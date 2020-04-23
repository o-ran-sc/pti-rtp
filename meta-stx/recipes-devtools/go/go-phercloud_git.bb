#
# Copyright (C) 2019 Wind River Systems, Inc.
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

DESCRIPTION = "an OpenStack SDK for Go"
HOMEPAGE = "https://github.com/gophercloud/gophercloud"
SECTION = "devel/go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=dd19699707373c2ca31531a659130416"

SRCNAME = "gophercloud"

PKG_NAME = "github.com/gophercloud/${SRCNAME}"
SRC_URI = "git://${PKG_NAME}.git"

SRCREV = "aa00757ee3ab58e53520b6cb910ca0543116400a"
PV = "0.3.0+git${SRCREV}"
RDEPENDS_${PN} = "bash"
S = "${WORKDIR}/git"

do_install() {
	install -d ${D}${prefix}/local/go/src/${PKG_NAME}
	cp -r ${S}/* ${D}${prefix}/local/go/src/${PKG_NAME}/
}

SYSROOT_PREPROCESS_FUNCS += "go_phercloud_sysroot_preprocess"

go_phercloud_sysroot_preprocess () {
    install -d ${SYSROOT_DESTDIR}${prefix}/local/go/src/${PKG_NAME}
    cp -r ${D}${prefix}/local/go/src/${PKG_NAME} ${SYSROOT_DESTDIR}${prefix}/local/go/src/$(dirname ${PKG_NAME})
}

FILES_${PN} += "${prefix}/local/go/src/${PKG_NAME}/*"
