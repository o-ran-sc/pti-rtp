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

SUMMARY = "Tools for working with directory and file names"
DESCRIPTION = "\
  Modules included: \
    - Cwd - get pathname of current working directory \
    - File::Spec - portably perform operations on file names \
    - File::Spec::AmigaOS - File::Spec for AmigaOS \
    - File::Spec::Cygwin - methods for Cygwin file specs \
    - File::Spec::Epoc - methods for Epoc file specs \
    - File::Spec::Functions - portably perform operations on file names \
    - File::Spec::Mac - File::Spec for Mac OS (Classic) \
    - File::Spec::OS2 - methods for OS/2 file specs \
    - File::Spec::Unix - File::Spec for Unix, base for other File::Spec modules \
    - File::Spec::VMS - methods for VMS file specs \
    - File::Spec::Win32 - methods for Win32 file specs \
"
HOMEPAGE = "https://metacpan.org/release/PathTools"

SECTION = "libs"

LICENSE = "Artistic-1.0 | GPL-1.0+"
LIC_FILES_CHKSUM = "file://META.yml;beginline=11;endline=11;md5=963ce28228347875ace682de56eef8e8"

CPAN_PACKAGE = "PathTools" 

SRC_URI = "${CPAN_MIRROR}/authors/id/X/XS/XSAWYERX/${CPAN_PACKAGE}-${PV}.tar.gz"

SRC_URI[md5sum] = "8f329058f74468a576442d841c62aa62"
SRC_URI[sha256sum] = "a558503aa6b1f8c727c0073339081a77888606aa701ada1ad62dd9d8c3f945a2"

S = "${WORKDIR}/${CPAN_PACKAGE}-${PV}"

inherit cpan

RDEPENDS_${PN} = " \
    perl-module-carp \
    perl-module-file-basename \
    perl-module-scalar-util \
    perl-module-test-more \
"

BBCLASSEXTEND = "native nativesdk"
