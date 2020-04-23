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

PACKAGES += " tsconfig"

inherit distutils

RDEPENDS_tsconfig_append = " bash"

do_configure_append () {
	cd ${S}/tsconfig/tsconfig
	distutils_do_configure
} 

do_compile_append () {
	cd ${S}/tsconfig/tsconfig
	distutils_do_compile
}

do_install_append () {
	cd ${S}/tsconfig/tsconfig
	distutils_do_install


	install -m 755 -d ${D}/${bindir}
	install -m 500 scripts/tsconfig ${D}/${bindir}/

}

FILES_tsconfig = " \
	${PYTHON_SITEPACKAGES_DIR}/tsconfig \
	${PYTHON_SITEPACKAGES_DIR}/tsconfig*.egg-info \
	${bindir}/tsconfig \
	"
