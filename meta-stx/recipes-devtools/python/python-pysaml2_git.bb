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

DESCRIPTION = "Python implementation of SAML Version 2 to be used in a WSGI environment"
HOMEPAGE = "https://github.com/rohe/pysaml2"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=861cc9087857b5bea2e11356c3de95d9"

SRCREV = "c740a3a270037d6fcb42a12112db594705d3878f"
SRCNAME = "pysaml2"
PROTOCOL = "git"
BRANCH = "v4.9.0"
S = "${WORKDIR}/git"
PV = "4.5.0+git${SRCPV}"

SRC_URI = "git://github.com/rohe/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools

DEPENDS += " \
        python-pip \
        "

RDEPENDS_${PN} += " \
	python-zopeinterface \
	python-repoze.who \
	python-defusedxml \
	"
