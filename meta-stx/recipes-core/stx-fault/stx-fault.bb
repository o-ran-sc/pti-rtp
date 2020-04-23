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

DESCRIPTION = "stx-fault"

INSANE_SKIP_${PN} = "ldflags"


PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV = "2025f585c5b92890c8cb32c480b0151c7c1cb545"
S = "${WORKDIR}/git"
PV = "1.0.0"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = " \
	git://opendev.org/starlingx/fault.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://0001-Honor-the-build-system-LDFLAGS.patch \
	file://0001-Use-build-systems-LDFLAGS.patch \
	file://0001-snmp-ext-use-build-systems-LDFLAGS.patch \
	"

inherit setuptools
DEPENDS = " \
	util-linux \
	postgresql \
	python \
	python-pbr-native \
	python-six \
	python-oslo.i18n \
	python-oslo.utils \
	python-requests \
	bash \
	net-snmp \
"

RDEPENDS_${PN} += " bash"

cgcs_doc_deploy = "/opt/deploy/cgcs_doc"

require fm-common.inc
require fm-api.inc
require fm-doc.inc
require fm-mgr.inc
require fm-rest-api.inc
require python-fmclient.inc
require snmp-audittrail.inc
require snmp-ext.inc

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
	:
}

FILES_${PN} = " "
