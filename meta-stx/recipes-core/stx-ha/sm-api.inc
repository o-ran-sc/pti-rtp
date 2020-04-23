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

PACKAGES += " sm-api"

RDEPENDS_sm-api += " \
	bash \
	python-six \
	chkconfig \
	mtce-pmon \
	"

do_configure_append () {
	cd ${S}/service-mgmt-api/sm-api
	distutils_do_configure
} 

do_compile_append () {
	cd ${S}/service-mgmt-api/sm-api
	distutils_do_compile
}

do_install_append () {
	cd ${S}/service-mgmt-api/sm-api
	distutils_do_install
	install -d -m 0755 ${D}/${sysconfdir}/sm
	install -d -m 0755 ${D}/${sysconfdir}/init.d
	install -d -m 0755 ${D}/${sysconfdir}/pmon.d
	install -d -m 0755 ${D}/${sysconfdir}/sm-api
	install -d -m 0755 ${D}/${systemd_system_unitdir}
	install -m 644 scripts/sm_api.ini ${D}/${sysconfdir}/sm
	install -m 755 scripts/sm-api ${D}/${sysconfdir}/init.d
	install -m 644 scripts/sm-api.service ${D}/${systemd_system_unitdir}
	install -m 644 scripts/sm-api.conf ${D}/${sysconfdir}/pmon.d
	install -m 644 etc/sm-api/policy.json ${D}/${sysconfdir}/sm-api
}

FILES_sm-api = " \
	${libdir}/python2.7/site-packages/sm_api*.egg-info/ \
	${libdir}/python2.7/site-packages/sm_api/ \
	${systemd_system_unitdir}/sm-api.service \
	${sysconfdir}/init.d/sm-api \
	${sysconfdir}/pmon.d/sm-api.conf \
	${sysconfdir}/sm/sm_api.ini \
	${sysconfdir}/sm-api/policy.json \
	${bindir}/sm-api \
	"

SYSTEMD_PACKAGES += "sm-api"
SYSTEMD_SERVICE_sm-api = "sm-api.service"
SYSTEMD_AUTO_ENABLE_sm-api = "enable"
