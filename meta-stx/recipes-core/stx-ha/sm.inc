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

PACKAGES += " sm"

RDEPENDS_sm += " \
	bash \
	python \
	logrotate \
	time \
	systemd \
	chkconfig \
	mtce-pmon \
	"
#	aaa_base

do_configure_append () {
	:
}

do_compile_append() {
	cd ${S}/service-mgmt/sm/src
	oe_runmake -e -j1 VER=0 VER_MJR=1 \
		INCLUDES="-I. -I${S}/service-mgmt/sm-common/src \
			-I${S}/service-mgmt/sm-db/src $(pkg-config --cflags glib-2.0)" \
		EXTRACCFLAGS="-I. -I${S}/service-mgmt/sm-common/src \
			-I${S}/service-mgmt/sm-db/src $(pkg-config --cflags glib-2.0) \
			-L${S}/service-mgmt/sm-common/src -L${S}/service-mgmt/sm-db/src \
				$(pkg-config --ldlags glib-2.0)" \
		CCFLAGS="${CXXFLAGS} -std=c++11" LDFLAGS="${LDFLAGS} -rdynamic"
}

do_install_append () {
	cd ${S}/service-mgmt/sm/src
	oe_runmake -e DEST_DIR=${D} BIN_DIR=${bindir} UNIT_DIR=${systemd_system_unitdir} \
		LIB_DIR=${libdir} INC_DIR=${includedir} VER=0 VER_MJR=1 install
	cd ${S}/service-mgmt/sm/scripts

	install -d ${D}/${sysconfdir}/init.d
	install sm ${D}/${sysconfdir}/init.d/sm
	install sm.shutdown ${D}/${sysconfdir}/init.d/sm-shutdown
	install -d -m0755 ${D}/${sysconfdir}/pmon.d
	install -m 644 sm.conf ${D}/${sysconfdir}/pmon.d/sm.conf
	install -d ${D}/${sysconfdir}/logrotate.d
	install -m 644 sm.logrotate ${D}/${sysconfdir}/logrotate.d/sm.logrotate
	install -d -m 755 ${D}/${sbindir}
	install sm.notify ${D}/${sbindir}/stx-ha-sm-notify
	install sm.troubleshoot ${D}/${sbindir}/sm-troubleshoot
	install sm.notification ${D}/${sbindir}/sm-notification
	install -d $(D)${systemd_system_unitdir}
	install -m 644 *.service ${D}/${systemd_system_unitdir}
}

FILES_sm = " \
	${bindir}/sm \
	${sysconfdir}/init.d/sm \
	${sysconfdir}/init.d/sm-shutdown \
	${sysconfdir}/pmon.d/sm.conf \
	${sysconfdir}/logrotate.d/sm.logrotate \
	${sbindir}/stx-ha-sm-notify \
	${sbindir}/sm-troubleshoot \
	${sbindir}/sm-notification \
	${systemd_system_unitdir}/sm-shutdown.service \
	${systemd_system_unitdir}/sm.service \
	"

pkg_postinst_ontarget_sm_append () {
	/usr/bin/update-alternatives --install /usr/sbin/sm-notify sm-notify /usr/sbin/stx-ha-sm-notify 5
}

SYSTEMD_PACKAGES += "sm"
SYSTEMD_SERVICE_sm = "sm.service sm-shutdown.service"
SYSTEMD_AUTO_ENABLE_sm = "enable"
