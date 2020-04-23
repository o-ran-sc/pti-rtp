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

SUMMARY = "Puppet module for OpenStack Keystone"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=12a15a9ebddda7d856c783f745e5ee47"

PV = "11.3.0"
SRCREV = "305c91cac00f720ad6461b442e71b52b12f9ae57"
PROTOCOL = "https"
BRANCH = "stable/pike"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/openstack/puppet-keystone.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://puppet-keystone/Add-gemspec.patch \
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

RUBY_INSTALL_GEMS = "puppet-keystone-${PV}.gem"

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/keystone
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/keystone
}

FILES_${PN} += " ${datadir}"
