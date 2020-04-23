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

DESCRIPTION = "Client for the OpenStack Cinder API"
HOMEPAGE = "https://opendev.org/openstack/python-cinderclient"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3572962e13e5e739b30b0864365e0795"

SRCREV = "4e17e1d1912f1902a37e4db543e38cdbe3961358"
SRCNAME = "python-cinderclient"
BRANCH = "stable/train"
PROTOCOL = "https"
PV = "4.1.0+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools

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
	python-oslo.i18n \
	python-oslo.utils \
	python-six \
	python-osc-lib \
	python-babel \
	python-requests \
	python-simplejson \
	"


do_install_append() {
	:
}

FILES_${PN} += " \
	"
