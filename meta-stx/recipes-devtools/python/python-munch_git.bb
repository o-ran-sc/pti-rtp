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

DESCRIPTION = "Munch is a dictionary that supports attribute-style access, a la JavaScript."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=f0291cd32b2d6a91d9b19970c94d0a46"


SRCREV = "d0fbbce7b8205a243337faed0b61472dfe09706d"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/Infinidat/munch.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
