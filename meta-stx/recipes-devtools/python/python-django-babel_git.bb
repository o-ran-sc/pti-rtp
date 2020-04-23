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

DESCRIPTION = "Tools for using Babel with Django"
HOMEPAGE = "https://github.com/python-babel/django-babel"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=5ae97ab65116b8d7890c59de57577b46"

SRCREV = "1da5c0ba7ef3b12810154d9e64b3e847ecbb06cc"
SRCNAME = "django-babel"
BRANCH = "master"
PROTOCOL = "https"
PV = "0.6.2+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/python-babel/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"
	
inherit setuptools

DEPENDS += " python-pip"

RDEPENDS_${PN}_append = " \
	python-django \
	python-babel \
	"

