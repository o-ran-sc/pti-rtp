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

DESCRIPTION = "\
		"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e73f0786a936da3814896df06ad225a9"

SRCREV = "f74b7104c0124db56c123ed171f378e82bd207c2"
PROTOCOL = "https"
BRANCH = "stable-0.6.0"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/stefanberger/libtpms.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

DEPENDS += " tpm2-tss tpm2-tools tpm2-abrmd openssh openssl"

inherit autotools

EXTRA_OECONF += "--with-openssl --with-tpm2"
