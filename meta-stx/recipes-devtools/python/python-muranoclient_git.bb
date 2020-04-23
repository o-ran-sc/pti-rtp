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

DESCRIPTION = "python-muranoclient"
STABLE = "master"
PROTOCOL = "https"
BRANCH = "master"
SRCREV = "70b4392c7f8524ac25dbf3ab0feb3ac4127c1ecf"
S = "${WORKDIR}/git"
PV = "1.1.1"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRC_URI = "git://github.com/openstack/python-muranoclient.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

DEPENDS += " \
	python \
	python-pbr-native \
	"

inherit setuptools

RDEPENDS_${PN}_append = " \
	bash	\
	python-pbr \
	python-prettytable \
	python-glanceclient \
	python-keystoneclient \
	python-iso8601 \
	python-six \
	python-babel \
	python-pyopenssl \
	python-requests \
	python-pyyaml \
	python-yaql \
	python-osc-lib \
	python-murano-pkg-check \
	python-oslo.serialization \
	python-oslo.utils \
	python-oslo.log \
	python-oslo.i18n \
	"
