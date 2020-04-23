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

SUMMARY = "A comprehensive, portable cryptographic toolkit"
DESCRIPTION = " \
  A comprehensive, modular and portable cryptographic toolkit that provides \
  developers with a vast array of well known published block ciphers, one-way \
  hash functions, chaining modes, pseudo-random number generators, public key \
  cryptography and a plethora of other routines. Designed from the ground up to \
  be very simple to use. It has a modular and standard API that allows new ciphers, \
  hashes and PRNGs to be added or removed without change to the overall end application. \
  It features easy to use functions and a complete user manual which has many source \
  snippet examples. \
"
HOMEPAGE = "https://github.com/libtom/libtomcrypt"

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=71baacc459522324ef3e2b9e052e8180"

DEPENDS = "libtool-cross"

SRC_URI = "https://github.com/libtom/libtomcrypt/releases/download/v${PV}/crypt-${PV}.tar.xz"

SRC_URI[md5sum] = "e8d22351b7c95bef423c1cd02dcf836d"
SRC_URI[sha256sum] = "96ad4c3b8336050993c5bc2cf6c057484f2b0f9f763448151567fbab5e767b84"

EXTRA_OEMAKE = " \
	LIBTOOL=${STAGING_BINDIR_CROSS}/${HOST_SYS}-libtool \
	LIBPATH=${libdir} \
	INCPATH=${includedir} \
	-f makefile.shared \
	"

do_compile() {
	oe_runmake library
}

do_install() {
	oe_runmake install DESTDIR=${D}
}
