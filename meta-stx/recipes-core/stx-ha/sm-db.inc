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

PACKAGES += " sm-db"

DEPENDS =+ "sqlite3-native"

do_configure_append () {
	cd ${S}/service-mgmt/sm-db
	oe_runmake clean
	rm -rf database/sm.db database/sm.hb.db
} 

do_compile_append () {
	cd ${S}/service-mgmt/sm-db
	sqlite3 database/sm.db < database/create_sm_db.sql
	sqlite3 database/sm.hb.db < database/create_sm_hb_db.sql

	oe_runmake -e VER=0 VER_MJR=1 \
		INCLUDES="-I. -I${S}/service-mgmt/sm-common/src " \
		CCFLAGS="${CXXFLAGS} -fPIC" \
		LDFLAGS="${LDFLAGS} -shared -rdynamic -L${S}/service-mgmt/sm-common/src " \
		EXTRACCFLAGS="-D_TEST__AA__BB ${LDFLAGS} -L${S}/service-mgmt/sm-common/src "
}

do_install_append () {
	cd ${S}/service-mgmt/sm-db
	oe_runmake -e DEST_DIR=${D} BIN_DIR=${bindir} UNIT_DIR=${systemd_system_unitdir} \
			LIB_DIR=${libdir} INC_DIR=${includedir} VER=0 VER_MJR=1 install
}

FILES_sm-db = " \
	${bindir}/sm_db_build \
	${libdir}/libsm_db.so.1 \
	${libdir}/libsm_db.so.0 \
	${localstatedir}/lib/sm \
	"
