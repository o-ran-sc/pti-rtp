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

DESCRIPTION = "CLI and python client library for OpenStack Sahara"
HOMEPAGE = "https://launchpad.net/sahara"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

DEPENDS += " \
        python-pip \
        python-pbr \
        "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
        python-pbr-native \
"

RDEPENDS_${PN} += " \
	python-pbr \
	"

SRCNAME = "saharaclient"

SRC_URI = "git://github.com/openstack/python-saharaclient.git;branch=master"

PV = "2.3.0+git${SRCPV}"
SRCREV = "3107b452467537f4eef3d9ecfb5e35d110d19662"
S = "${WORKDIR}/git"

inherit setuptools

