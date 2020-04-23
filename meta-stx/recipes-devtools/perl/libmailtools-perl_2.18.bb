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

DESCRIPTION = "MailTools is a set of Perl modules related to mail applications"
HOMEPAGE = "http://search.cpan.org/dist/MailTools/"
SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0+"
LIC_FILES_CHKSUM = "file://lib/Mail/Mailer.pod;beginline=144;md5=93c9027e72612b3555f857c4fc17b953"
DEPENDS = " \
	libtest-pod-perl-native \
	libtimedate-perl-native \
	"	
RDEPENDS_${PN} += " \
	libtest-pod-perl \
	libtimedate-perl \
	perl-module-io-handle \
	perl-module-net-smtp \
	perl-module-test-more \
	"
BBCLASSEXTEND = "native"

SRC_URI = "http://search.cpan.org/CPAN/authors/id/M/MA/MARKOV/MailTools-${PV}.tar.gz"
SRC_URI[md5sum] = "972468ab5207b90398d77bed4ffc361d"
SRC_URI[sha256sum] = "dfee9e770257371112f20d978e637759e81bc4f19e97b083585c71ecab37b527"

S = "${WORKDIR}/MailTools-${PV}"

inherit cpan

