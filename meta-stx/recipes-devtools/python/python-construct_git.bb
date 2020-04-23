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

DESCRIPTION = "Construct is a powerful declarative and symmetrical parser and builder for binary data"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3fd0f2c25089e629957285e6bc402a20"

SRCREV = "a6a45f09da003b278cc5955486ad17d9144c136b"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/construct/construct.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python python-pbr-native"
inherit setuptools distutils pkgconfig
