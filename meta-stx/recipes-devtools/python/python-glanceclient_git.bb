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

DESCRIPTION = "Client library for Glance built on the OpenStack Images API."
HOMEPAGE = "https://opendev.org/openstack/python-glanceclient"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

SRCREV = "44a4dbd6ce2642daeaca9f45ac99e2d1b39e805a"
SRCNAME = "python-glanceclient"
BRANCH = "stable/train"
PROTOCOL = "https"
PV = "2.16.0+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"
	
inherit setuptools monitor rmargparse

DEPENDS += " \
        python-pip \
        python-pbr-native\
        "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
	bash \
        python-pbr \
        python-prettytable \
        python-keystoneauth1 \
	python-warlock \
        python-six \
        python-oslo.utils \
        python-oslo.i18n \
	python-wrapt \
	python-pyopenssl \
	"


do_install_append() {
	:
}

FILES_${PN} += " \
	"
