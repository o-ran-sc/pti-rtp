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

DESCRIPTION = "Oslo db library"
HOMEPAGE = "http://launchpad.net/oslo"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

SRCREV = "4de33ebd504a2c3dbddc2492bdb96ae7bca77d66"
SRCNAME = "oslo.db"
PROTOCOL = "https"
BRANCH = "stable/stein"
S = "${WORKDIR}/git"
PV = "4.27.0+git${SRCPV}"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
        python-oslo.config \
	python-oslo.i18n \
	python-oslo.serialization \
	python-oslo.utils \
	python-six \
        python-alembic \
	python-sqlalchemy \
	python-sqlalchemy-migrate \
	python-stevedore \
	python-pbr \
	python-debtcollector \
	"
