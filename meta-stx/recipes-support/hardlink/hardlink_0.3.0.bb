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

DESCRIPTION = "Hardlink is a tool which replaces multiple copies of a file with hardlinks"

HOMEPAGE = "https://jak-linux.org/projects/hardlink"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://hardlink.1;md5=c7a9a3326f28fa218d9af0251a5e1ae4"

SRC_URI = "https://jak-linux.org/projects/hardlink/hardlink_0.3.0.tar.xz"
SRC_URI[md5sum] = "72f1a460adb6874c151deab766e434ad"
SRC_URI[sha256sum] = "e8c93dfcb24aeb44a75281ed73757cb862cc63b225d565db1c270af9dbb7300f"

DEPENDS = " attr"
inherit pkgconfig

do_install() {
	cd ${S}
	oe_runmake -e DESTDIR=${D} BINDIR=${bindir} MANDIR=${datadir} install
}

FILES_${PN}-doc = "${datadir}/"
