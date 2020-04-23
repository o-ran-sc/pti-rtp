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

PACKAGES += " ceph-manager"

RDEPENDS_ceph-manager += " sysinv"
DESCRIPTION_ceph-manager = " \
Handle Ceph API calls and provide status updates via alarms. \
Handle sysinv RPC calls for long running Ceph API operations: \
	- cache tiering enable \
	- cache tiering disable \
"

do_configure_append() {
	cd ${S}/ceph/ceph-manager/ceph-manager
	distutils_do_configure
}
do_compile_append() {
	cd ${S}/ceph/ceph-manager/ceph-manager
	distutils_do_compile
}

do_install_append() {
	cd ${S}/ceph/ceph-manager/ceph-manager
	distutils_do_install

	install -d -m0755 ${D}/${bindir}
	install -d -m0755 ${D}/${sysconfdir}/init.d
	install -d -m0755 ${D}/${sysconfdir}/logrotate.d
	install -d -m0755 ${D}/${systemd_system_unitdir}

	install -p -m0700 ${S}/ceph/ceph-manager/scripts/bin/ceph-manager ${D}/${sysconfdir}/init.d
	install -p -m0700 ${S}/ceph/ceph-manager/scripts/bin/ceph-manager ${D}/${bindir}
	install -p -m0700 ${S}/ceph/ceph-manager/files/ceph-manager.logrotate ${D}/${sysconfdir}/logrotate.d
	install -p -m0700 ${S}/ceph/ceph-manager/files/ceph-manager.service ${D}/${systemd_system_unitdir}
}

FILES_ceph-manager = "  \
	${bindir}/ceph-manager \
	${sysconfdir}/init.d/ceph-manager \
	${sysconfdir}/logrotate.d/ceph-manager.logrotate \
	${systemd_system_unitdir}/ceph-manager.service \
	${PYTHON_SITEPACKAGES_DIR}/ceph_manager \
	${PYTHON_SITEPACKAGES_DIR}/ceph_manager-1.0.0-py2.7.egg-info \
	"
