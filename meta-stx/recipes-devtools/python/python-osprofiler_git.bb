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

DESCRIPTION = "Library for cross-project profiling library"
HOMEPAGE = "https://docs.openstack.org/osprofiler/latest/"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=19cbd64715b51267a47bf3750cc6a8a5"

SRCREV = "6d68170f72ef303e6564e164aafb1ec53a8b8314"
SRCNAME = "osprofiler"
BRANCH = "master"
PROTOCOL = "https"
PV = "2.3.0+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = "git://opendev.org/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"
	
inherit setuptools

DEPENDS += " python-pbr-native"

RDEPENDS_${PN}_append = " \
	python-pbr \
	python-six \
	python-oslo.messaging \
	python-oslo.log \
	python-oslo.utils \
	python-webob \
	python-requests \
	python-netaddr \
	python-oslo.concurrency \
	"
