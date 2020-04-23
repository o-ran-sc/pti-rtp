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

DESCRIPTION = "Generic Key Manager interface for OpenStack"
HOMEPAGE = "https://github.com/openstack/castellan"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRCREV = "8e2929b8779eaa03f15a25da5cf64ef8539a026b"
SRCNAME = "castellan"
PROTOCOL = "https"
BRANCH = "stable/stein"
S = "${WORKDIR}/git"
PV = "0.17.0+git${SRCPV}"

SRC_URI = "git://opendev.org/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
        python-pbr \
        python-babel \
        python-cryptography \
        python-barbicanclient \
        python-oslo.config \
        python-oslo.context \
        python-oslo.i18n \
        python-oslo.log \
        python-oslo.utils \
        python-keystoneauth1 \
        "
