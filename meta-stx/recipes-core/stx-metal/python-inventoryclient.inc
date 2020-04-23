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

PACKAGES += " python-inventoryclient"


# python-keystoneauth1 >= 3.1.0
# python-pbr >= 2.0.0
# python-six >= 1.9.0
# python-oslo-i18n >= 2.1.0
# python-oslo-utils >= 3.20.0
# python-requests
# bash-completion
#
RDEPENDS_python-inventoryclient_append = " \
	python \
	python-pbr \
	python-six \
	python-oslo.i18n \
	python-oslo.utils \
	python-requests \
	bash \
	bash-completion \
	"

do_configure_prepend () {
	cd ${S}/python-inventoryclient/inventoryclient
	distutils_do_configure
} 

do_compile_prepend () {
	cd ${S}/python-inventoryclient/inventoryclient
	distutils_do_compile
}

do_install_prepend () {
	cd ${S}/python-inventoryclient/inventoryclient
	distutils_do_install
	
	install -d -m 755 ${D}/${sysconfdir}/bash_completion.d
	install -p -D -m 664 tools/inventory.bash_completion ${D}/${sysconfdir}/bash_completion.d

}

FILES_python-inventoryclient = " \
	${bindir}/inventory \
	${sysconfdir}/bash_completion.d/inventory.bash_completion \
	${libdir}/python2.7/site-packages/inventoryclient-*.egg-info/ \
	${libdir}/python2.7/site-packages/inventoryclient/ \
	"
