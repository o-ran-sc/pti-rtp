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

DESCRIPTION = "Backport of pathlib-compatible object wrapper for zip files"
HOMEPAGE = "https://github.com/jaraco/zipp"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a33f38bbf47d48c70fe0d40e5f77498e"

SRC_URI[md5sum] = "d4451a749d8a7c3c392a9edd1864a937"
SRC_URI[sha256sum] = "3718b1cbcd963c7d4c5511a8240812904164b7f381b647143a89d3b98f9bcd8e"

DEPENDS += "${PYTHON_PN}-setuptools-scm-native"
RDEPENDS_${PN} += "${PYTHON_PN}-more-itertools"

inherit pypi setuptools

BBCLASSEXTEND = "native nativesdk"
