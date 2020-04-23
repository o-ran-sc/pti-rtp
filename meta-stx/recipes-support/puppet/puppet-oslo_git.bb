#
# Copyright (C) 2019 Wind River Systems, Inc.
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

SUMMARY = "The oslo module is a part of OpenStack intended to provide continuous integration testing and code review."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86e96afa80a52eedda5dccc1af36b4f2"

PV = "11.3.0"
SRCREV = "5ad200e9d8af200a8f50f86a2db4cf3f36ab2a4c"
PROTOCOL = "https"
BRANCH = "stable/pike"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/openstack/puppet-oslo.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://puppet-oslo/Add-gemspec.patch \
	"

inherit ruby

DEPENDS += " \
	ruby \
	facter \
	"

RDEPENDS_${PN} += " \
	ruby \
	facter \
	puppet \
	perl \
	"

RUBY_INSTALL_GEMS = "puppet-oslo-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/oslo
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/oslo
}

FILES_${PN} += " ${datadir}"
