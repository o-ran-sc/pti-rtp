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

DESCRIPTION = "Client side API implementation of the Redfish RESTful API for Data Center Hardware Management."
HOMEPAGE = "https://github.com/DMTF/Redfishtool"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=cee7a7694b5bf14bc9d3e0fbe78a64af"


SRCREV = "2bdcd905e1ad227f40809ec298804d5401047612"
SRCNAME = "Redfishtool"
BRANCH = "master"
PROTOCOL = "https"
PV = "1.1.0+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/DMTF/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools3

RDEPENDS_${PN} += " \
	python3-requests \
"

do_install_append() {
	:
}

FILES_${PN} += " \
	"
