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

PACKAGES += " snmp-audittrail"

##################
# DEPENDS = " fm-common net-snmp"
##################

do_compile_append() {
	cd ${S}/snmp-audittrail/sources
	oe_runmake -e \
		LDFLAGS="${LDFLAGS} -L${S}/fm-common/sources" \
		CCFLAGS="${CXXFLAGS} -fPIC" \
		INCLUDES="-I. -I${S}/fm-common/sources" \
		build
}

do_install_append () {
	cd ${S}/snmp-audittrail/sources
	oe_runmake -e DESTDIR=${D} LIB_DIR=${libdir} MIBVER=0 PATCH=0 install
}

FILES_snmp-audittrail = " \
	${libdir}/libsnmpAuditPlugin.so.1.0.0 \
	${libdir}/libsnmpAuditPlugin.so.1 \
	"

