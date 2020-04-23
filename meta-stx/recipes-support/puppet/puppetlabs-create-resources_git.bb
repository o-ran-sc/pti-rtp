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

SUMMARY = "Function to dynamically create resources from hashes."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6089b6bd1f0d807edb8bdfd76da0b038 "

PV = "0.0.1"
STABLE = "master"
PROTOCOL = "https"
BRANCH = "master"
SRCREV = "4639819a7f3a4fa9310d2ba583c63e467df7e2c3"
S = "${WORKDIR}/git"


SRC_URI = " \
	git://github.com/puppetlabs/puppetlabs-create_resources.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
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
	install -d -m 0755 ${D}/${datadir}/puppet/modules/create_resources
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/create_resources
}

FILES_${PN} += " ${datadir}"
