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

DESCRIPTION = "Python classes to extract information from the Linux kernel /proc files"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b"


SRCREV = "d170839360edf3fbac4a31c7771c4ec5bb0b6121"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "0.6.1"

SRC_URI = "git://git.kernel.org/pub/scm/libs/python/python-linux-procfs/python-linux-procfs.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
RDEPENDS_${PN} = "python3-core"

inherit setuptools3 distutils3 pkgconfig
