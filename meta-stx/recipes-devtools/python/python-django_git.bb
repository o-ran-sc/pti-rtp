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

DESCRIPTION = "client library for Aodh built on the Aodh API"
HOMEPAGE = "https://launchpad.net/python-aodhclient"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause & Python-2.0"
LIC_FILES_CHKSUM = " \
	file://LICENSE;md5=f09eb47206614a4954c51db8a94840fa\
	file://LICENSE.python;md5=6b60258130e4ed10d3101517eb5b9385 \
	"

SRCREV = "1c9cb948d7b0c264d244763b6682ab790a6b90a0"
SRCNAME = "django"
BRANCH = "stable/1.11.x"
PROTOCOL = "https"
PV = "1.11.20+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/django/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"
	
inherit setuptools

FILES_${PN} += "${datadir}/django/"
