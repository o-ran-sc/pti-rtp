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

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# erlang < 20.0 is not compatibel with OpenSSL 1.1.x
inherit openssl10
DEPENDS_append = " openssl-native"

SRC_URI += "file://erts-configure.in-avoid-RPATH-warning.patch"

EXTRA_OECONF = '--with-ssl=${STAGING_DIR_NATIVE}/usr --without-krb5 --without-zlib'
