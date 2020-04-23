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

SUMMARY = " Web Services Made Easy (WSME)"
DESCRIPTION = "\
	Web Services Made Easy (WSME) simplifies the writing of REST web services by providing simple yet \
	powerful typing, removing the need to directly manipulate the request and the response objects. \
	"


LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5a9126e7f56a0cf3247050de7f10d0f4"

SRCREV = "f36a607124355007d3a830ebe7e53efda777c58a"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git/"

SRC_URI = "git://opendev.org/x/wsme.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python3 python3-pbr-native"
inherit distutils3 
