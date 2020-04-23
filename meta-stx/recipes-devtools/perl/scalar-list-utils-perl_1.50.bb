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

SUMMARY = "A selection of general-utility list subroutines"
DESCRIPTION = "\
  List::Util contains a selection of subroutines that people have expressed \
  would be nice to have in the perl core, but the usage would not really be \
  high enough to warrant the use of a keyword, and the size so small such \
  that being individual extensions would be wasteful.\
"
HOMEPAGE= "https://metacpan.org/release/Scalar-List-Utils"

SECTION = "libs"

LICENSE = "Artistic-1.0 | GPL-1.0+"

LIC_FILES_CHKSUM = "file://META.yml;beginline=11;endline=11;md5=963ce28228347875ace682de56eef8e8"

CPAN_PACKAGE = "Scalar-List-Utils"

SRC_URI = "${CPAN_MIRROR}/authors/id/P/PE/PEVANS/${CPAN_PACKAGE}-${PV}.tar.gz"

SRC_URI[md5sum] = "136313884d1afa2fb6840695a1034b2c"
SRC_URI[sha256sum] = "06aab9c693380190e53be09be7daed20c5d6278f71956989c24cca7782013675"

S = "${WORKDIR}/${CPAN_PACKAGE}-${PV}"

inherit cpan

BBCLASSEXTEND = "native nativesdk"
