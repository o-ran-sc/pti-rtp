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

SUMMARY = "C bindings for apps which will manipulate JSON data"
DESCRIPTION = "JSON-C implements a reference counting object model that allows you to easily construct JSON objects in C."
HOMEPAGE = "https://github.com/json-c/json-c/wiki"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=de54b60fbbc35123ba193fea8ee216f2"

SRC_URI = "\
    https://s3.amazonaws.com/json-c_releases/releases/${BP}.tar.gz \
    file://json-c-CVE-2013-6371.patch \
    file://json-c-Add-FALLTHRU-comment-to-handle-GCC7-warnings.patch \
"
SRC_URI[md5sum] = "aa02367d2f7a830bf1e3376f77881e98"
SRC_URI[sha256sum] = "28dfc65145dc0d4df1dfe7701ac173c4e5f9347176c8983edbfac9149494448c"

RPROVIDES_${PN} = "libjson"

PARALLEL_MAKE = ""

inherit autotools

do_configure_prepend() {
    # Clean up autoconf cruft that should not be in the tarball
    rm -f ${S}/config.status
}

BBCLASSEXTEND = "native nativesdk"
