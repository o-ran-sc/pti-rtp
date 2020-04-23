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

DESCRIPTION = "A high-level cross-protocol url-grabber."
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=68ad62c64cc6c620126241fd429e68fe"

SRC_URI[sha256sum] = "79c5a01c5dd31906a7f38ef1f500030e137704804d585644693d3e474ed15f39"

DEPENDS += "python-six python-six-native"
PYPI_PACKAGE = "urlgrabber"
inherit pypi setuptools

FILES_${PN}_append = " ${datadir}"
