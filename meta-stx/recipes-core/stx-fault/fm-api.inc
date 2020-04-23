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

PACKAGES += " fm-api"

do_configure_append () {
	cd ${S}/fm-api
	distutils_do_configure
} 

do_compile_append() {
	cd ${S}/fm-api
	distutils_do_compile
}

do_install_append() {
	cd ${S}/fm-api
	distutils_do_install

	# fix the path for binaries
	sed -i -e 's|/usr/local/bin|${bindir}|' ${D}${libdir}/python2.7/site-packages/fm_api/constants.py
}

FILES_fm-api_append = " \
	${libdir}/python2.7/site-packages/fm_api-1.0.0-py2.7.egg-info/ \
	${libdir}/python2.7/site-packages/fm_api \
	"
