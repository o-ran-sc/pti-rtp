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

SUMMARY = "Event loop abstraction for Libraries"
DESCRIPTION = "Libverto exists to isolate libraries from the particular event loop \
chosen by an application. Libverto provides an asynchronous \
programming interface independent of any particular event loop and \
allows applications to attach this interface to whatever event loop \
they select."
HOMEPAGE = "http://fedorahosted.net/libverto"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=bc8917ab981cfa6161dc29319a4038d9"


DEPENDS += "libevent libtevent"

# fedorahosted tarball cannot be fetched completely, so switch to use other source
# SRC_URI = "http://fedorahosted.org/releases/l/i/${PN}/${PN}-${PV}.tar.gz"
SRC_URI = "https://github.com/latchset/libverto/releases/download/0.2.5/${PN}-${PV}.tar.gz \
          "
SRC_URI[md5sum] = "144fb8f00759ef8ad71c472333847f03"

inherit autotools pkgconfig

PACKAGECONFIG ??= "libevent tevent"
PACKAGECONFIG[glib] = "--with-glib,--without-glib,glib-2.0"
PACKAGECONFIG[libev] = "--with-libev,--without-libev,libev"
PACKAGECONFIG[libevent] = "--with-libevent,--without-libevent,libevent"
PACKAGECONFIG[tevent] = "--with-tevent,--without-tevent,libtevent"

PACKAGES =+ "${PN}-libevent ${PN}-tevent"

FILES_${PN}-libevent = "${libdir}/libverto-libevent${SOLIBS}"
FILES_${PN}-tevent = "${libdir}/libverto-tevent${SOLIBS}"

RPROVIDES_${PN}-libevent += "${PN}-module-base"
RPROVIDES_${PN}-tevent += "${PN}-module-base"

