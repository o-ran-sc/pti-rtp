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

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${PN}/0001-pike-rebase-squash-titanium-patches.patch \
	file://${PN}/0002-remove-the-Keystone-admin-app.patch \
	file://${PN}/0003-remove-eventlet_bindhost-from-Keystoneconf.patch \
	file://${PN}/0004-escape-special-characters-in-bootstrap.patch \
	file://${PN}/0005-Add-support-for-fernet-receipts.patch \
	file://${PN}/0007-puppet-keystone-specify-full-path-to-openrc.patch \
	file://${PN}/0008-params.pp-fix-the-service-name-of-openstack-keystone.patch \
	"

do_install_append () {
	# fix the name of python-memcached
	sed -i -e 's/python-memcache\b/python-memcached/' ${D}/${datadir}/puppet/modules/keystone/manifests/params.pp
}

RDEPENDS_${PN} += " \
	python-memcached \
	python-ldappool \
	"

inherit openssl10
