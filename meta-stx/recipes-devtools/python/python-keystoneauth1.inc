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

DESCRIPTION = "Authentication Library for OpenStack Identity"
HOMEPAGE = "https://pypi.python.org/pypi/keystoneauth1"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=96f840d59b245a1c817fbcb901afc574"

SRC_URI[md5sum] = "0864f3485db8709d1dec0c8fa6073a31"
SRC_URI[sha256sum] = "db91ccab5cc43dac4a25dc2c090642d7c31f9ceb28df2c685620d7c12335a8cd"

inherit pypi

DEPENDS += " \
        ${PYTHON_PN}-pbr \
        "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
        ${PYTHON_PN}-pbr-native \
        "

RDEPENDS_${PN} += " \
        ${PYTHON_PN}-pbr \
        ${PYTHON_PN}-iso8601 \
        ${PYTHON_PN}-requests \
        ${PYTHON_PN}-six \
        ${PYTHON_PN}-stevedore \
	python2-os-service-types \
        "
