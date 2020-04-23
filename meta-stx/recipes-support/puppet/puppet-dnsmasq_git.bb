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

DESCRIPTION = "puppet module for dnsmasq"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://README.md;md5=71e3bfa9ffc5e93324727bbffae917f5"

PV = "1.1.0"
STABLE = "master"
PROTOCOL = "https"
BRANCH = "master"
SRCREV = "cff07e90890662972c97684a2baee964f68ff3ed"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/netmanagers/puppet-dnsmasq;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://${PN}/Add-gemspec.patch \
	file://${PN}/metadata.json.patch \
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

RUBY_INSTALL_GEMS = "${PN}-${PV}.gem"

do_install_append () {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/dnsmasq
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/dnsmasq/
}

FILES_${PN} += " ${datadir}"
