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

DESCRIPTION = "Test::Pod - check for POD errors in files"
SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0+"
LIC_FILES_CHKSUM = "file://README;beginline=26;md5=ea81140c3f3b2937c6490fc732471cd0"
HOMEPAGE = "https://github.com/perl-pod/test-pod/"
BBCLASSEXTEND = "native"
RDEPENDS_${PN} += " perl-module-test-more \
                    perl-module-file-spec \
                    perl-module-pod-simple \
                    perl-module-test-builder-tester \
                    "

SRC_URI ="http://search.cpan.org/CPAN/authors/id/E/ET/ETHER/Test-Pod-${PV}.tar.gz;name=test-pod-perl-${PV}"
SRC_URI[test-pod-perl-1.51.md5sum] = "f806aa84de2f0c0fba48b3a5a8a4aecf"
SRC_URI[test-pod-perl-1.51.sha256sum] = "c1a1d3cedf4a579e3aad89c36f9878a8542b6656dbe71f1581420f49582d7efb"
S = "${WORKDIR}/Test-Pod-${PV}"

inherit cpan

