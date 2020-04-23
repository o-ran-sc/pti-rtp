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

DESCRIPTION = "HPE 3PAR HTTP REST Client"
HOMEPAGE = "https://pythonhosted.org/python-3parclient/"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://PKG-INFO;md5=c6f4d3b3208673edc0228bbc3ad053cc"

SRC_URI[md5sum] = "845e688b5607a71fc307e8371daf5d40"
SRC_URI[sha256sum] = "fcd1c5c7d9356f4244a6c0b2b6dd6c64366399642c348b02999ea8fbf79e3a8d"

PYPI_PACKAGE = "python-3parclient"
inherit setuptools pypi
