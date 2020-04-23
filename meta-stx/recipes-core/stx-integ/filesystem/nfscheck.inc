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

PACKAGES += " nfscheck"

RDEPENDS_nfscheck_append += " bash"

do_configure_append () {
	:
} 

do_compile_append () {
	:
}

do_install_append () {

	cd ${S}/filesystem/nfscheck/
	install -d -m755 ${D}/${bindir}
	install -d -m755 ${D}/${systemd_system_unitdir}

	install -D -m644 files/nfscheck.sh ${D}/${bindir}
	install -D -m644 files/nfscheck.service ${D}/${systemd_system_unitdir}


}

#pkg_postinst_ontarget_nfscheck () {
#	/bin/systemctl enable nfscheck.service
#}	

FILES_nfscheck_append = " \
		${systemd_system_unitdir}/nfscheck.service \
		${bindir}/nfscheck.sh \
		"
