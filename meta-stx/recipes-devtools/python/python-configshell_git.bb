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

DESCRIPTION = " \
ConfigShell is a Python library that provides a framework for building simple but nice CLI-based applications running both as single-command tools and interactive shells providing a UNIX filesystem-like navigation interface, as well as full autocompletion support and interactive inline help. It is part of LIO. \
	"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRCREV = "020d540850ca36f31af68e9b545a520f1122ea69"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/Datera/configshell.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python python-pyparsing-native"
inherit setuptools distutils pkgconfig
