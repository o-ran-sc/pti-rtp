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

DESCRIPTION = "requests-toolbelt"

STABLE = "master"
PROTOCOL = "https"
BRANCH = "master"
SRCREV = "1e384626476f7afbff0f649fe41886d0f27473d6"
S = "${WORKDIR}/git"
PV = "0.9.1+${SRCPV}"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=71760e0f1dda8cff91b0bc9246caf571"

SRC_URI = "git://github.com/requests/toolbelt.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

DEPENDS = " \
	python \
	python-pbr-native \
	"

inherit setuptools

RDEPENDS_${PN} += " bash"
