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

SUMMARY = "A Puppet module to manage the nslcd daemon which provides authentication via LDAP"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://Modulefile;md5=674f57ad12dfafcf6c3943f34d459ded"

PV = "0.0.1"
SRCREV = "b8c19b1ada89865f2e50758e054583798ad8011a"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = " \
	git://github.com/jlyheden/puppet-nslcd;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
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

do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/nslcd
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/nslcd
}

FILES_${PN} += " ${datadir}"
