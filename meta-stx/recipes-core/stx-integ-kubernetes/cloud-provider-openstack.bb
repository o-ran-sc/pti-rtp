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

DESCRIPTION = "cloud-provider-openstack"

STABLE = "starlingx/master"
PROTOCOL = "https"
BRANCH = "master"
SRCREV = "70609a3d55e5b7d2be82667fc35792505f9013c4"
S = "${WORKDIR}/git"
PV = "19.05"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://opendev.org/starlingx/config.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

RDEPENDS_${PN} += " bash"

do_configure () {
	:
} 

do_compile() {
	:
}

do_install () {

	cd ${S}/filesystem/cloud-provider-openstack/
	install -d -m755 ${D}/${bindir}
	install -d -m755 ${D}/${systemd_system_unitdir}

	install -D -m644 files/cloud-provider-openstack.sh ${D}/${bindir}
	install -D -m644 files/cloud-provider-openstack.service ${D}/${systemd_system_unitdir}


}

#pkg_postinst_ontarget_${PN} () {
#	/usr/bin/systemctl enable cloud-provider-openstack.service
#}	

FILES_${PN}_append += " \
		${systemd_system_unitdir} \
		"
