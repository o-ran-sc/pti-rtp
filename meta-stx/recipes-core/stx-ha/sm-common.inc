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

PACKAGES += " libsm-common"
PACKAGES += " sm-common-libs"
PACKAGES += " sm-eru"

RDEPENDS_sm-common-libs += " \
	bash \
	sqlite \
	util-linux \
	systemd \
	"
RDEPENDS_sm-eru = " sm-common-libs"
RDEPENDS_libsm-common = " sm-common-libs"


do_configure_prepend () {
	:
} 

do_compile_prepend () {
	cd ${S}/service-mgmt/sm-common/src
	oe_runmake -e VER=0 VER_MJR=1 \
		INCLUDES="-I. $(pkg-config --cflags glib-2.0)" \
		CCFLAGS="${CXXFLAGS} -fPIC" LDFLAGS="${LDFLAGS} -shared -rdynamic" \
		EXTRACCFLAGS="${LDFLAGS}" 
	
}

do_install_prepend () {
	cd ${S}/service-mgmt/sm-common/src
	oe_runmake -e DEST_DIR=${D} BIN_DIR=${bindir} UNIT_DIR=${systemd_system_unitdir} \
			LIB_DIR=${libdir} INC_DIR=${includedir} VER=0 VER_MJR=1 install
 
	cd ${S}/service-mgmt/sm-common/
	install -d -m 755 ${D}/${systemd_system_unitdir}
	# install -m 755 -d ${D}/${sysconfdir}/pmon.d
	# install -m 755 -d ${D}/${sysconfdir}/init.d

	install -m 644 -p -D scripts/sm-eru.service ${D}/${systemd_system_unitdir}/sm-eru.service
	install -m 644 -p -D scripts/sm-watchdog.service ${D}/${systemd_system_unitdir}/sm-watchdog.service

	install -m 640 -p -D scripts/sm-eru.conf ${D}/${sysconfdir}/pmon.d/sm-eru.conf
	install -m 640 -p -D scripts/sm-watchdog.conf ${D}/${sysconfdir}/pmon.d/sm-watchdog.conf
	install -m 750 -p -D scripts/sm-eru ${D}/${sysconfdir}/init.d/sm-eru
	install -m 750 -p -D scripts/sm-watchdog ${D}/${sysconfdir}/init.d/sm-watchdog
}

FILES_libsm-common = " \
	${libdir}/libsm_common.so.0 \
	${libdir}/libsm_common.so.1 \
	${libdir}/libsm_common.so \
	"

FILES_sm-common-libs = " \
	var/lib/sm/watchdog/modules/libsm_watchdog_nfs.so.0 \
	var/lib/sm/watchdog/modules/libsm_watchdog_nfs.so.1 \
	"

FILES_sm-eru = " \
	${bindir}/sm-eru \
	${bindir}/sm-eru-dump \
	${bindir}/sm-watchdog \
	${systemd_system_unitdir}/sm-eru.service \
	${systemd_system_unitdir}/sm-watchdog.service \
	${sysconfdir}/init.d/sm-eru \
	${sysconfdir}/pmon.d/sm-eru.conf \
	${sysconfdir}/init.d/sm-watchdog \
	${sysconfdir}/pmon.d/sm-watchdog.conf \
	"

SYSTEMD_PACKAGES += "sm-eru"
SYSTEMD_SERVICE_sm-eru = "sm-eru.service sm-watchdog.service"
SYSTEMD_AUTO_ENABLE_sm-eru = "enable"
