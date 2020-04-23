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

DESCRIPTION = "Perl extensions for IPv6"
HOMEPAGE = "https://metacpan.org/release/Socket6"
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://README;beginline=31;md5=aa15b0e3744ac40eaada8738eccd24df"

BBCLASSEXTEND = "native"

CFLAGS += "-D_LARGEFILE_SOURCE -D_LARGEFILE64_SOURCE"
BUILD_CFLAGS += "-D_LARGEFILE_SOURCE -D_LARGEFILE64_SOURCE"

SRC_URI = "http://search.cpan.org/CPAN/authors/id/U/UM/UMEMOTO/Socket6-${PV}.tar.gz;name=socket6-perl-${PV} \
           file://0001-socket6-perl-fix-configure-error.patch \
"
SRC_URI[socket6-perl-0.28.md5sum] = "aa8489135a3dbcec6233396e1aeb043b"
SRC_URI[socket6-perl-0.28.sha256sum] = "bfd49ab99f3197c99285fed4683c4edc06277c1e4453f593e694d7bff0974586"

S = "${WORKDIR}/Socket6-${PV}"

do_configure_prepend () {
	mkdir -p m4
	autoreconf -Wcross --verbose --install --force || oefatal "autoreconf execution failed."
	sed -i 's:\./configure\(.[^-]\):./configure --build=${BUILD_SYS} --host=${HOST_SYS} --target=${TARGET_SYS} --prefix=${prefix} --exec_prefix=${exec_prefix} --bindir=${bindir} --sbindir=${sbindir} --libexecdir=${libexecdir} --datadir=${datadir} --sysconfdir=${sysconfdir} --sharedstatedir=${sharedstatedir} --localstatedir=${localstatedir} --libdir=${libdir} --includedir=${includedir} --oldincludedir=${oldincludedir} --infodir=${infodir} --mandir=${mandir}\1:' Makefile.PL
}

inherit cpan
