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

DESCRIPTION = "A Python library for the Docker Engine API"
HOMEPAGE = "https://pypi.org/project/docker/3.3.0"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34f3846f940453127309b920eeb89660"

SRC_URI[md5sum] = "660c3c0c3776cb88f34fc549d7c89ed2"
SRC_URI[sha256sum] = "dc5cc0971a0d36fe94c5ce89bd4adb6c892713500af7b0818708229c3199911a"

inherit setuptools pypi

RDEPENDS_${PN}_append = " \
	python-docker-pycreds \
	python-backports-ssl \
	"
