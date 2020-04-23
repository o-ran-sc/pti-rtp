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

PACKAGES += " filesystem-scripts"

RDEPENDS_filesystem-scripts += " bash"

do_configure_append () {
	:
} 

do_compile_append () {
	:
}

do_install_append () {

	cd ${S}/filesystem/filesystem-scripts/filesystem-scripts-1.0
	install -d -m755 ${D}/${sysconfdir}/init.d
	install -d -m755 ${D}/${libdir}/ocf/resource.d/platform
	install -d -m755 ${D}/${bindir}
	install -d -m755 ${D}/${systemd_system_unitdir}

	install -D -m644 uexportfs  ${D}/${sysconfdir}/init.d
	install -D -m644 nfsserver-mgmt ${D}/${libdir}/ocf/resource.d/platform
	install -D -m644 nfs-mount ${D}/${bindir}
	install -D -m644 uexportfs.service ${D}/${systemd_system_unitdir}

}

FILES_filesystem-scripts_append = " \
	${sysconfdir}/init.d/uexportfs \
	${libdir}/ocf/resource.d/platform/nfsserver-mgmt \
	${bindir}/nfs-mount \
	${systemd_system_unitdir}/uexportfs.service \
	"

#pkg_postinst_ontarget_filesystem-scripts() {
#	/usr/bin/systemctl enable uexportfs.service
#}
