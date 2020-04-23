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

DESCRIPTION = "Useful additions to futures, from the future"
HOMEPAGE = "https://pypi.python.org/pypi/futurist"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRC_URI[md5sum] = "3e5a4b8254ded1624ec807cb7ae04ba5"
SRC_URI[sha256sum] = "499ee57728a987028725f836ac22aa18899702162fa0a0f1bbe2ecd5c8daf1eb"

inherit setuptools pypi

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
        python-pbr \
        python-six \
        python-monotonic \
        python-futures \
        python-contextlib2 \
        python-prettytable \
        "

