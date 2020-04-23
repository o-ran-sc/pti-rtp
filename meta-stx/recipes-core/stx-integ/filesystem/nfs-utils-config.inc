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

PACKAGES += " nfs-utils-config"

RDEPENDS_nfs-utils-config_append  += " bash"

do_configure_append () {
	:
} 

do_compile_append () {
	:
}

do_install_append () {

	cd ${S}/filesystem/nfs-utils-config/
	install -d -m755 ${D}/${sysconfdir}/init.d
	install -d -m755 ${D}/${datadir}/starlingx
	install -d -m755 ${D}/${systemd_system_unitdir}

	install -D -m644 files/nfscommon ${D}/${sysconfdir}/init.d/stx-nfscommon
	install -D -m644 files/nfscommon.service ${D}/${systemd_system_unitdir}/stx-nfscommon.service
	install -D -m644 files/nfsserver ${D}/${sysconfdir}/init.d/stx-nfsserver
	install -D -m644 files/nfsserver.service ${D}/${systemd_system_unitdir}/stx-nfsserver.service
	install -D -m644 files/nfsmount.conf ${D}/${datadir}/starlingx/stx-nfsmount.conf
	install -D -m644 files/nfsmount.conf ${D}/${sysconfdir}/stx-nfsmount.conf

}

#pkg_postinst_ontarget_nfs-utils-config () {
#	systemctl disable rpc-statd.service
#	systemctl disable rpc-statd-notify.service
#	systemctl disable nfs-lock.service
#	systemctl disable nfslock.service
#
#	systemctl enable nfscommon.service  >/dev/null 2>&1 || :
#	systemctl enable nfsserver.service  >/dev/null 2>&1 || :
#}	

FILES_nfs-utils-config_append = " \
	${sysconfdir}/init.d/stx-nfscommon \
	${systemd_system_unitdir}/stx-nfscommon.service \
	${sysconfdir}/init.d/stx-nfsserver \
	${systemd_system_unitdir}/stx-nfsserver.service \
	${datadir}/starlingx/stx-nfsmount.conf \
	${sysconfdir}/stx-nfsmount.conf \
	" 
