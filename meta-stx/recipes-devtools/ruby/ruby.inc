#
## Copyright (C) 2019 Wind River Systems, Inc.
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

SUMMARY = "An interpreter of object-oriented scripting language"
DESCRIPTION = "Ruby is an interpreted scripting language for quick \
and easy object-oriented programming. It has many features to process \
text files and to do system management tasks (as in Perl). \
It is simple, straight-forward, and extensible. \
"
HOMEPAGE = "http://www.ruby-lang.org/"
SECTION = "devel/ruby"
LICENSE = "Ruby | BSD | GPLv2"
LIC_FILES_CHKSUM = "\
    file://COPYING;md5=837b32593517ae48b9c3b5c87a5d288c \
    file://BSDL;md5=19aaf65c88a40b508d17ae4be539c4b5 \
    file://GPL;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://LEGAL;md5=3ce1fae39fe573b818c0af162bce6579 \
"

DEPENDS = "ruby-native zlib openssl tcl libyaml gdbm readline libffi libnsl2"
DEPENDS_class-native = "openssl-native libyaml-native readline-native libnsl2"

SHRT_VER = "${@oe.utils.trim_version("${PV}", 2)}"
SRC_URI = " \
	http://cache.ruby-lang.org/pub/ruby/2.0/ruby-2.0.0-p648.tar.gz \
	file://0002-Obey-LDFLAGS-for-the-link-of-libruby.patch \
           "
# file://0002-Obey-LDFLAGS-for-the-link-of-libruby.patch 
# file://extmk.patch 
UPSTREAM_CHECK_URI = "https://www.ruby-lang.org/en/downloads/"

inherit autotools ptest


# This snippet lets compiled extensions which rely on external libraries,
# such as zlib, compile properly.  If we don't do this, then when extmk.rb
# runs, it uses the native libraries instead of the target libraries, and so
# none of the linking operations succeed -- which makes extconf.rb think
# that the libraries aren't available and hence that the extension can't be
# built.

do_configure_prepend() {
    sed -i "s#%%TARGET_CFLAGS%%#$TARGET_CFLAGS#; s#%%TARGET_LDFLAGS%%#$TARGET_LDFLAGS#" ${S}/common.mk
    rm -rf ${S}/ruby/
}
