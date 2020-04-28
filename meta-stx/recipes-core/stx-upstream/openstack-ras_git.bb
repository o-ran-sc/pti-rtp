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

DESCRIPTION = "Pacemaker High Availability resource agents for OpenStack"
SUMMARY = "Openstack Resource Agents from Madkiss"

PROTOCOL = "https"
BRANCH = "master"
SRCREV = "cc6f677570b4f0e7ba44a91d5dff33164b8b76bc"
S = "${WORKDIR}/git"
PV = "1.0.0"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/starlingx-staging/stx-openstack-ras.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

do_configure() {
	:
}


do_compile() {
	:
}


do_install() {
	make  DESTDIR=${D} install
	rm -rf ${D}/usr/lib/ocf/resource.d/openstack/ceilometer-agent-central
	rm -rf ${D}/usr/lib/ocf/resource.d/openstack/ceilometer-alarm-evaluator
	rm -rf ${D}/usr/lib/ocf/resource.d/openstack/ceilometer-alarm-notifier
}

FILES_${PN} += " ${libdir}"
