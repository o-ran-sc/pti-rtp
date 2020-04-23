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

PACKAGES += " sm-tools"

RDEPENDS_sm-tools += " python"

do_configure_append () {
	cd ${S}/service-mgmt-tools/sm-tools
	distutils_do_configure
} 

do_compile_append() {
	cd ${S}/service-mgmt-tools/sm-tools
	distutils_do_compile
}

do_install_append () {
	cd ${S}/service-mgmt-tools/sm-tools
	distutils_do_install
}

FILES_sm-tools = " \
	${bindir}/sm-query \
	${bindir}/sm-provision \
	${bindir}/sm-restart \
	${bindir}/sm-unmanage \
	${bindir}/sm-dump \
	${bindir}/sm-iface-state \
	${bindir}/sm-configure \
	${bindir}/sm-manage \
	${bindir}/sm-deprovision \
	${bindir}/sm-restart-safe \
	${libdir}/python2.7/site-packages/sm_tools-*.egg-info/ \
	${libdir}/python2.7/site-packages/sm_tools/ \
	"
