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

DESCRIPTION = "A simple connector pool for python-ldap."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://setup.py;md5=9c24605289b49ad77a51ba7986425158"


SRCREV = "f75c1a8a34d4fbe6df7889205b5c6a4db6886c33"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://opendev.org/openstack/ldappool.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python python-pbr-native"
inherit setuptools distutils pkgconfig
