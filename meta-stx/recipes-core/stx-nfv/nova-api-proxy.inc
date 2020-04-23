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

PACKAGES += " nova-api-proxy"

inherit setuptools


do_configure_append () {
	cd ${S}/nova-api-proxy/nova-api-proxy
	distutils_do_configure
} 

do_compile_append( ) {
	cd ${S}/nova-api-proxy/nova-api-proxy
	distutils_do_compile
}

do_install_append () {
	cd ${S}/nova-api-proxy/nova-api-proxy
	distutils_do_install

	install -d -m 755 ${D}/${systemd_system_unitdir}
	install -p -D -m 644 nova_api_proxy/scripts/api-proxy.service ${D}/${systemd_system_unitdir}/api-proxy.service
	install -d -m 755 ${D}/${sysconfdir}/rc.d/init.d
	install -p -D -m 755 nova_api_proxy/scripts/api-proxy ${D}/${sysconfdir}/rc.d/init.d/api-proxy

	install -d -m 755 ${D}/${sysconfdir}/proxy
	install -p -D -m 700 nova_api_proxy/nova-api-proxy.conf ${D}${sysconfdir}/proxy/nova-api-proxy.conf
	install -p -D -m 700 nova_api_proxy/api-proxy-paste.ini ${D}${sysconfdir}/proxy/api-proxy-paste.ini
	

}


FILES_nova-api-proxy = " \ 
	${bindir}/nova-api-proxy \
	${sysconfdir}/proxy/api-proxy-paste.ini \
	${sysconfdir}/proxy/api-proxy-paste.conf \
	${sysconfdir}/proxy/nova-api-proxy.conf \
	${sysconfdir}/rc.d/init.d/api-proxy \
	${systemd_system_unitdir}/api-proxy.service \
	${libdir}/python2.7/site-packages/nova_api_proxy/ \
	${libdir}/python2.7/site-packages/api_proxy*egg-info \
	"
