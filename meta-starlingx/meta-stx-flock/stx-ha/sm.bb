require ha-common.inc
SUBPATH0 = "service-mgmt/sm"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_sm += " \
	bash \
	python \
	logrotate \
	time \
	systemd \
	chkconfig \
	mtce-pmon \
	"

DEPENDS_append = " \
	libsm-common \
	mtce \
	sm-db \
	"

EXTRA_OEMAKE = ' \
	-e -j1 VER=0 VER_MJR=1 \
	INCLUDES="-I.  $(pkg-config --cflags glib-2.0)" \
	EXTRACCFLAGS="-I. $(pkg-config --cflags glib-2.0) $(pkg-config --ldflags glib-2.0) -lsqlite3" \
	CCFLAGS="${CXXFLAGS} -std=c++11" LDFLAGS="${LDFLAGS} -rdynamic" \
	'
do_install() {
	cd ${S}/src
	oe_runmake -e DEST_DIR=${D} BIN_DIR=${bindir} UNIT_DIR=${systemd_system_unitdir} \
		LIB_DIR=${libdir} INC_DIR=${includedir} VER=0 VER_MJR=1 install

	cd ${S}/scripts
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
	install -d -m0755 ${D}/${systemd_system_unitdir}
	install -m 644 *.service ${D}/${systemd_system_unitdir}
}

pkg_postinst_ontarget_sm_append () {
	/usr/bin/update-alternatives --install /usr/sbin/sm-notify sm-notify /usr/sbin/stx-ha-sm-notify 5
}

SYSTEMD_PACKAGES += "sm"
SYSTEMD_SERVICE_sm = "sm.service sm-shutdown.service"
SYSTEMD_AUTO_ENABLE_sm = "enable"
