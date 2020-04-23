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

PACKAGES += " cgts-client"

inherit setuptools distutils python-dir
DEPENDS += " \
	python-pbr-native \
	"

RDEPENDS_cgts-client += " \
	python-prettytable \
	bash-completion \
	python-neutronclient \
	python-keystoneclient \
	python-six \
	python-httplib2 \
	"

do_configure_append() {
	cd ${S}/sysinv/cgts-client/cgts-client
	distutils_do_configure
} 

do_compile_append() {
	cd ${S}/sysinv/cgts-client/cgts-client
	distutils_do_compile
}

do_install_append() {
	cd ${S}/sysinv/cgts-client/cgts-client
	distutils_do_install
}

FILES_cgts-client = " \
	${bindir}/system \
	${PYTHON_SITEPACKAGES_DIR}/sysinv/ \
	${PYTHON_SITEPACKAGES_DIR}/cgtsclient/ \
	${PYTHON_SITEPACKAGES_DIR}/cgtsclient-*.egg-info \
	${PYTHON_SITEPACKAGES_DIR}/sysinv-*.egg-info \
	"
