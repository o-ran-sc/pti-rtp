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

SUMMARY = " Lang - Python Language Constraints"
DESCRIPTION = "\
	Lang is a Python module that allows enforcing programming language constraints. Lang was \
	built using a Java like mindset, so many of the constraints that are supported are mirrors \
	of constraints in the Java programming language. \
	"


LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fa818a259cbed7ce8bc2a22d35a464fc"

SRCREV = "feb4c638ebc581d9913f440965e83558fd10018c"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git/src"

SRC_URI = "git://github.com/amitassaraf/lang.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python3 python3-setuptools-scm-native"
inherit setuptools3 distutils3 pkgconfig
