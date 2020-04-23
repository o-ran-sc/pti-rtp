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

SUMMARY = "A Puppet module to manage non volatile network and route configuration."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b43880e14353cbc12cf3981fb93a0944"


PV = "0.5.0"
SRCREV = "7deacd5fdc22c0543455878a8d1872f2f5417c1d"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = " \
	git://github.com/voxpupuli/puppet-network;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://puppet-network/Add-gemspec.patch \
	file://puppet-network/metadata.json.patch \
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
	install -d -m 0755 ${D}/${datadir}/puppet/modules/network
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/network
	# Remove broken symlinks for now
	rm -f ${D}/${libdir}/ruby/gems/2.5.0/gems/puppet-network-0.5.0/spec/fixtures/modules/network/manifests
	rm -f ${D}/${libdir}/ruby/gems/2.5.0/gems/puppet-network-0.5.0/spec/fixtures/modules/network/templates
	rm -f ${D}/${libdir}/ruby/gems/2.5.0/gems/puppet-network-0.5.0/spec/fixtures/modules/network/files
	rm -f ${D}/${libdir}/ruby/gems/2.5.0/gems/puppet-network-0.5.0/spec/fixtures/modules/network/lib
	rm -f ${D}/${datadir}/puppet/modules/network/spec/fixtures/modules/network/files
}

FILES_${PN} += " ${datadir}"
