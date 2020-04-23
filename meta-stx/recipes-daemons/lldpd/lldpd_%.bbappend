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

FILESEXTRAPATHS_prepend := "${THISDIR}/patches:${THISDIR}/files:"

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

SRC_URI += " \
	file://0001-lldpd-client-add-show-interfaces-cmd-from-upstream.patch \
	file://0002-Clear-station-bit-if-any-other-capability-is-enabled.patch \
	file://i40e-lldp-configure.sh \
	"

# TODO: 
# Check Yocto handling of i40e firmware?
# See i40e-lldp-configure.sh and lldpd-i40e-disable.patch

# file://lldpd.init 
# lldpd-create-run-dir.patch

do_install_append() {
	cd ${S}
	install -d -m 0755 ${D}/${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/i40e-lldp-configure.sh ${D}/${sysconfdir}/init.d/
}

FILES_${PN}_append = " \
	${sysconfdir}/init.d/i40e-lldp-configure.sh \
	"

RDEPENDS_${PN} += "bash"
