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

DESCRIPTION = "A library to handle official service types for OpenStack and their historical aliases."
HOMEPAGE = "https://opendev.org/openstack/os-service-types"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

STABLE = "master"
PROTOCOL = "https"
BRANCH = "master"
SRCREV = "2e5b38088a43539621de82aa2d0c1b366c2638a9"
S = "${WORKDIR}/git"

SRC_URI = "git://opendev.org/openstack/os-service-types.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools 

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
        python-lxml \
        python-requests \
        python-cython \
        "
