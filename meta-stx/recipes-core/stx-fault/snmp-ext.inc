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

PACKAGES += " snmp-ext"

####################
# DEPENDS = " fm-common snmp-audittrail"
####################

do_compile_append() {
	cd ${S}/snmp-ext/sources
	oe_runmake -e PATCH=0 \
		INCLUDES="-I. -I${S}/fm-common/sources" \
		LDFLAGS="${LDFLAGS} -L${S}/fm-common/sources" \
		CCFLAGS="${CXXFLAGS} -fPIC" lib
}

do_install_append () {
	cd ${S}/snmp-ext/sources
	oe_runmake -e DEST_DIR=${D} LIB_DIR=${libdir} MIBVER=0 PATCH=0 install
}

FILES_snmp-ext_append = " \
	${datadir}/snmp/mibs/wrsAlarmMib.mib.txt \
	${datadir}/snmp/mibs/wrsEnterpriseReg.mib.txt \
	${libdir}/libcgtsAgentPlugin.so.1.0.0 \
	${libdir}/libcgtsAgentPlugin.so.1 \
	"
