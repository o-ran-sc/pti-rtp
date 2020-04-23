#
# Copyright (C) 2019 Wind River Systems, Inc.
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
#

SUMMARY = " CherryPy is a pythonic, object-oriented HTTP framework"
DESCRIPTION = "\
	It allows building web applications in much the same way one would build any other object-oriented program. \
	This design results in less and more readable code being developed faster. It's all just properties and methods. \
	It is now more than ten years old and has proven fast and very stable. \
	It is being used in production by many sites, from the simplest to the most demanding. \
	"


LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=a8cbc5da4e6892b15a972a0b18622b2b"

SRCREV = "994803e4923e53b7079c79f4e9b502cc1b8d0aa6"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "18.2.0"

SRC_URI = "git://github.com/cherrypy/cherrypy.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python3 python3-setuptools-scm-native"
inherit setuptools3 distutils3 pkgconfig

RDEPENDS_${PN} += " \
	python3-compression \
	python3-crypt \
	python3-datetime \
	python3-email \
	python3-fcntl \
	python3-html \
	python3-io \
	python3-json \
	python3-logging \
	python3-netclient \
	python3-netserver \
	python3-profile \
	python3-pydoc \
	python3-xml \
	python3-unixadmin \
	"
	
RDEPENDS_${PN} += " \
	python3-cheroot \
	python3-contextlib2 \
	python3-memcached \
	python3-portend \
	python3-pyopenssl \
	python3-routes \
	python3-simplejson \
	python3-six \
	python3-zc-lockfile \
	"
