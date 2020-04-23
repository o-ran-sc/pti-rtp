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

SUMMARY = "perlfilter - Source Filters"
DESCRIPTION = "\
  perlfilter - Source Filters \
    Modules included: \
    - Filter::Util::Call - Perl Source Filter Utility Module \
    - Filter::Util::Exec - exec source filter \
    - Filter::decrypt - template for a decrypt source filter \
    - Filter::cpp - cpp source filter \
    - Filter::exec - exec source filter \
    - Filter::m4 - M4 source filter \
    - Filter::sh - sh source filter \
    - Filter::tee - tee source filter \
"
HOMEPAGE = "https://metacpan.org/release/Filter"

SECTION = "libs"

LICENSE = "Artistic-1.0 | GPL-1.0+"
LIC_FILES_CHKSUM = "file://META.yml;beginline=11;endline=11;md5=963ce28228347875ace682de56eef8e8"

CPAN_PACKAGE = "Filter"

SRC_URI = "${CPAN_MIRROR}/authors/id/R/RU/RURBAN/${CPAN_PACKAGE}-${PV}.tar.gz"

SRC_URI[md5sum] = "54e08a158bff1f35e0a93b3993dcf52f"
SRC_URI[sha256sum] = "b4babfad4e0566a9a61199735f6e622a60d3274122752304f18f623412bf4e5a"

S = "${WORKDIR}/${CPAN_PACKAGE}-${PV}"

inherit cpan

BBCLASSEXTEND = "native nativesdk"
