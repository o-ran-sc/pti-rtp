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

DESCRIPTION = "oslo.concurrency library"
HOMEPAGE = "https://github.com/openstack/oslo.concurrency"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

SRCREV = "5b42d276350666410a7d010a5152467ad509d3f9"
SRCNAME = "oslo.concurrency"
PROTOCOL = "https"
BRANCH = "stable/train"
S = "${WORKDIR}/git"
PV = "3.26.0+git${SRCPV}"

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
        python-pbr \
        python-fasteners \
        python-oslo.config \
        python-oslo.i18n \
        python-oslo.utils \
        python-six \
        python-enum34 \
        "
