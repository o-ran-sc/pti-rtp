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

DESCRIPTION = "Python client for containers service"
HOMEPAGE = "https://github.com/openstack/python-magnumclient"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d2794c0df5b907fdace235a619d80314"

SRCREV = "37e602d160632a386c2960ec8777bfc65642a9a9"
SRCNAME = "python-magnumclient"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "2.12.0+git${SRCPV}"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

RDEPENDS_${PN} +=" \
	python-pbr \
	python-babel \
	python-stevedore \
	python-requests \
	python-oslo.i18n \
	python-oslo.serialization \
	python-oslo.utils \
	python-os-client-config \
	python-osc-lib \
	python-prettytable \
	python-cryptography \
	python-decorator \
	"

inherit setuptools
