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
SRC_URI[test-pod-perl-1.52.md5sum] = "472dda77746d48e6465bf62e47aeca81"
SRC_URI[test-pod-perl-1.52.sha256sum] = "60a8dbcc60168bf1daa5cc2350236df9343e9878f4ab9830970a5dde6fe8e5fc"
S = "${WORKDIR}/Test-Pod-${PV}"

inherit cpan
