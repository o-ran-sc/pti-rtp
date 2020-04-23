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

DESCRIPTION = "Middleware for Openstack identity API"
HOMEPAGE = "https://launchpad.net/keystonemiddleware"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4a4d0e932ffae1c0131528d30d419c55"

SRCREV = "83d0612e03471f56be3be2b521cc21974118cebe"
SRCNAME = "keystonemiddleware"
BRANCH = "stable/train"
PROTOCOL = "https"
PV = "5.1.0"
S = "${WORKDIR}/git"

SRC_URI = "git://git.openstack.org/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


inherit setuptools

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
        python-keystoneauth1 \
        python-oslo.cache \
        python-oslo.config \
        python-oslo.context \
        python-oslo.i18n \
        python-oslo.log \
        python-oslo.serialization \
        python-oslo.utils \
        python-pbr \
        python-positional \
        python-pycadf \
        python-keystoneclient \
        python-requests \
        python-six \
        python-webob \
        "
