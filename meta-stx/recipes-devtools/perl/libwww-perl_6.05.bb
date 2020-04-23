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

DESCRIPTION = "libwww-perl provides a simple and consistent API to the World Wide Web"
HOMEPAGE = "http://search.cpan.org/dist/libwww-perl/"
SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0+"
LIC_FILES_CHKSUM = "file://README;beginline=92;endline=98;md5=3da13bc02f8f17ed35ac5d192cae6fe4"
DEPENDS = "liburi-perl-native libhtml-parser-perl-native libhtml-tagset-perl-native"
RDEPENDS_${PN} += " \
	libhtml-parser-perl \
	libhtml-tagset-perl \
	liburi-perl \
	perl-module-digest-md5 \
	perl-module-net-ftp \
	"
BBCLASSEXTEND = "native"


SRC_URI = "http://search.cpan.org/CPAN/authors/id/G/GA/GAAS/libwww-perl-${PV}.tar.gz;name=libwww-perl-${PV}"
SRC_URI[libwww-perl-6.05.md5sum] = "637d5f1eb61336ca2caa6e026b382f87"
SRC_URI[libwww-perl-6.05.sha256sum] = "7b25799ff7eec18e8e4e97dc0cad7b2a5b433b50b13feb59d9179173bee78f23"

S = "${WORKDIR}/libwww-perl-${PV}"

inherit cpan

