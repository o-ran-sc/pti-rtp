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

PACKAGES += " python-cephclient"

RDEPENDS_ceph-manager += " \
	python \
	python-ipaddress \
	python-six \
	python-requests \
	"

DESCRIPTION_python-cephclient = " \
A client library in Python for Ceph Mgr RESTful plugin providing REST API \
access to the cluster over an SSL-secured connection. Python API is compatible \
with the old Python Ceph client at \
https://github.com/dmsimard/python-cephclient that no longer works in Ceph \
mimic because Ceph REST API component was removed. \
"

do_configure_append() {
	cd ${S}/ceph/python-cephclient/python-cephclient
	rm -rf .pytest_cache
	rm -rf python_cephclient.egg-info
	rm -f requirements.txt
	distutils_do_configure
}
do_compile_append() {
	cd ${S}/ceph/python-cephclient/python-cephclient
	distutils_do_compile
}

do_install_append() {
	cd ${S}/ceph/python-cephclient/python-cephclient
	distutils_do_install
}

FILES_python-cephclient = "  \
	${PYTHON_SITEPACKAGES_DIR}/cephclient \
	${PYTHON_SITEPACKAGES_DIR}/python_cephclient-13.2.2.0-py2.7.egg-info \
	"
#	/usr/share/licenses/python-cephclient-13.2.2.0
#	/usr/share/licenses/python-cephclient-13.2.2.0/LICENSE

