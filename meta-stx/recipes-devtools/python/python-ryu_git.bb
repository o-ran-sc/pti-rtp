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

DESCRIPTION = "Ryu is a component-based software defined networking framework."
HOMEPAGE = "https://github.com/osrg/ryu"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
	file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57\
	"

SRCREV = "1c008060fa3dab51c3a59c1485a7529b13cf0dd1"
SRCNAME = "ryu"
BRANCH = "master"
PROTOCOL = "https"
PV = "4.24+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/osrg/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools python-dir

DEPENDS += " \
	python-pip \
	python-pbr-native \
	"

RDEPENDS_${PN}_append = " \
	${PYTHON_PN}-eventlet \
	${PYTHON_PN}-msgpack \
	${PYTHON_PN}-netaddr \
	${PYTHON_PN}-oslo.config \
	${PYTHON_PN}-ovs \
	${PYTHON_PN}-routes \
	${PYTHON_PN}-six \
	${PYTHON_PN}-tinyrpc \
	${PYTHON_PN}-webob \
	"

FILES_${PN}_append = " \
	${datadir}/etc/${SRCNAME} \
	"
