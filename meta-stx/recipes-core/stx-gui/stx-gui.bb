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

DESCRIPTION = "stx-gui"

PACKAGES = "starlingx-dashboard"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV = "d1c22e49a95f92e91049b96f44e685f46785977c"
S = "${WORKDIR}/git"
PV = "1.0.0"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"



SRC_URI = "git://opendev.org/starlingx/gui.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit distutils python-dir

DEPENDS = "\
	python \
	python-pbr-native \
	"

RDEPENDS_starlingx-dashboard += " \
	python-django-horizon \
	"

RDEPENDS_starlingx-dashboard_append = " \
	${PYTHON_PN}-cephclient \
	"

do_configure () {
	cd ${S}/starlingx-dashboard/starlingx-dashboard
	distutils_do_configure
}


do_compile () {
	cd ${S}/starlingx-dashboard/starlingx-dashboard
	distutils_do_compile
}


do_install () {
	cd ${S}/starlingx-dashboard/starlingx-dashboard
	distutils_do_install

	install -d -m 0755 ${D}/${datadir}/openstack-dashboard/openstack_dashboard/enabled/
	install -d -m 0755 ${D}/${datadir}/openstack-dashboard/openstack_dashboard/themes/starlingx/
	install -d -m 0755 ${D}/${datadir}/openstack-dashboard/openstack_dashboard/local/local_settings.d
	cp -ra ${D}${PYTHON_SITEPACKAGES_DIR}/starlingx_dashboard/enabled/* \
			${D}/${datadir}/openstack-dashboard/openstack_dashboard/enabled/
	cp -ra ${D}${PYTHON_SITEPACKAGES_DIR}/starlingx_dashboard/themes/starlingx/* \
			${D}/${datadir}/openstack-dashboard/openstack_dashboard/themes/starlingx/
	cp -ra ${D}${PYTHON_SITEPACKAGES_DIR}/starlingx_dashboard/local/local_settings.d/* \
			${D}/${datadir}/openstack-dashboard/openstack_dashboard/local/local_settings.d
	# comment out this line with syntax error
	sed -i -e 's|^OPENSTACK_NEUTRON_NETWORK|# &|' \
			${D}/${datadir}/openstack-dashboard/openstack_dashboard/local/local_settings.d/_30_stx_local_settings.py
}

FILES_starlingx-dashboard = " \
	${PYTHON_SITEPACKAGES_DIR} \
	${datadir}/openstack-dashboard/openstack_dashboard \
	"
