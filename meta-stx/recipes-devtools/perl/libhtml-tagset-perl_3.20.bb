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

DESCRIPTION = "HTML Tagset bits."
HOMEPAGE = "http://search.cpan.org/dist/HTML-Tagset/"
SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0+"
LIC_FILES_CHKSUM = "file://README;beginline=60;md5=16ddda2d845a5546f615e6b122d1dbad"

SRC_URI = "http://search.cpan.org/CPAN/authors/id/P/PE/PETDANCE/HTML-Tagset-${PV}.tar.gz"

S = "${WORKDIR}/HTML-Tagset-${PV}"

inherit cpan

BBCLASSEXTEND="native"


SRC_URI[md5sum] = "d2bfa18fe1904df7f683e96611e87437"
SRC_URI[sha256sum] = "adb17dac9e36cd011f5243881c9739417fd102fce760f8de4e9be4c7131108e2"
