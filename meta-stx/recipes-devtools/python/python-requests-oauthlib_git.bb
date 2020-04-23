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

DESCRIPTION = "This project provides first-class OAuth library support for Requests."

LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://LICENSE;md5=22d117a849df10d047ed9b792838e863"

SRCREV = "e19ac082682ecf3ed06cdcc5538e3c4bb2aa7762"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "1.2.0"

SRC_URI = "git://github.com/requests/requests-oauthlib;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
