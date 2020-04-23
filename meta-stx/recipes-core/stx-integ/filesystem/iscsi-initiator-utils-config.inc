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

PACKAGES += " iscsi-initiator-utils-config"

RDEPENDS_iscsi-initiator-utils-config_append += " bash"

do_configure_append () {
	:
} 

do_compile_append () {
	:
}

do_install_append () {

	cd ${S}/filesystem/iscsi-initiator-utils-config/
	install -d -m755 ${D}/${libdir}/tempfiles.d
	install -d -m755 ${D}/${systemd_system_unitdir}
	install -d -m755 ${D}/${datadir}/starlingx

	install -D -m644 files/iscsi-cache.volatiles ${D}/${libdir}/tempfiles.d
	install -D -m644 files/iscsi-shutdown.service ${D}/${systemd_system_unitdir}
	install -m 0644 files/iscsid.conf ${D}/${datadir}/starlingx/stx.iscsid.conf
}

FILES_iscsi-initiator-utils-config_append = " \
	${libdir}/tempfiles.d/iscsi-cache.volatiles \
	${systemd_system_unitdir}/iscsi-shutdown.service \
	${datadir}/starlingx/stx.iscsid.conf \
	"
