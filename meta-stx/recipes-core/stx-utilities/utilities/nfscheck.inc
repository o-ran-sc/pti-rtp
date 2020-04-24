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

RDEPENDS_nfscheck  += " bash systemd"
DESCRIPTION_nfscheck = "NFS Audit"
SUMMARY_nfscheck = "NFS Audit"

do_configure_append() {
	:
}
do_compile_append() {
	:
}

do_install_append() {
	cd ${S}/utilities/nfscheck/files

	install -d -m0755 ${D}/${bindir}
	install -m0755 nfscheck.sh ${D}/${bindir}

	install -d -m0755 ${D}/${systemd_system_unitdir}
	install -m0644 nfscheck.service ${D}/${systemd_system_unitdir}
}

FILES_nfscheck = "  \
	${bindir}/nfscheck.sh \
	${systemd_system_unitdir}/nfscheck.service \
	"

SYSTEMD_PACKAGES += "nfscheck"
SYSTEMD_SERVICE_nfscheck = "nfscheck.service"
SYSTEMD_AUTO_ENABLE_nfscheck = "enable"
