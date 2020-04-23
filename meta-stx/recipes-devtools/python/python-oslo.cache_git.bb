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

DESCRIPTION = "oslo.config enabled dogpile cache"
HOMEPAGE = "https://github.com/openstack/oslo.cache"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRCREV = "3b8d9c3f6c87d62e5502cf4a9ae89e4067180c1f"
SRCNAME = "oslo.cache"
PROTOCOL = "https"
BRANCH = "stable/train"
S = "${WORKDIR}/git"
PV = "1.26.0+git${SRCPV}"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools rmargparse

DEPENDS += " \
        python-pbr \
        python-pip \
        "

DEPENDS += " \
        python-pbr-native \
        "

# RDEPENDS_default:
RDEPENDS_${PN} += " \
        python-dogpile.cache \
        python-six \
        python-oslo.config \
        python-oslo.i18n \
        python-oslo.log \
        python-oslo.utils \
        "
