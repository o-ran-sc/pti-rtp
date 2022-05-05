PACKAGES += " mtce-guestagent"
PACKAGES += " mtce-guestserver"

require nfv-common.inc

SUBPATH0 = "mtce-guest/src"

SRC_URI += " \
	file://0001-mtce-guest-Fix-ldflags-usage.patch \
	file://0002-mtce-guest-agent-server-fix-script-path.patch;striplevel=3 \
	file://0003-guest-agent-server-fix-daemon-paths.patch;striplevel=3 \
	"

inherit systemd
SYSTEMD_PACKAGES += "mtce-guestagent"
SYSTEMD_PACKAGES += "mtce-guestserver"
SYSTEMD_SERVICE_mtce-guestagent = "guestAgent.service"
SYSTEMD_SERVICE_mtce-guestserver= "guestServer.service"
SYSTEMD_AUTO_ENABLE_mtce-guestagent = "disable"
SYSTEMD_AUTO_ENABLE_mtce-geustserver = "enable"
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

RDEPENDS_${PN} += " mtce-guestagent mtce-guestserver"

EXTRA_OEMAKE = '-e MAJOR="1" MINONR="0" \
		INCLUDES=" -I. -I${STAGING_INCDIR}/mtce-common/ -I${STAGING_INCDIR}/mtce-daemon/ " \
		CPPFLAGS="${CXXFLAGS}" LDFLAGS="${LDFLAGS}"'

do_install() {

	oe_runmake -e install DESTDIR=${D} PREFIX=${D}/usr/ \
		       SYSCONFDIR=${D}/${sysconfdir} \
		            LOCALBINDIR=${D}/${bindir} \
			    UNITDIR=${D}/${systemd_system_unitdir} 

	rm -rf ${D}/var
	rm -rf ${D}/var/run
}

FILES_mtce-guestserver = " \
	${sysconfdir}/logrotate.d/guestServer.logrotate \
	${systemd_system_unitdir}/guestServer.service \
	"

FILES_mtce-guestagent = " \ 
	${systemd_system_unitdir}/guestAgent.service \
	${sysconfdir}/logrotate.d/guestAgent.logrotate \
	${libdir}/ocf/resource.d/platform/guestAgent \
" 

FILE_${PN} = " \
	${sysconfdir}/mtc/tmp \
	${sysconfdir}/mtc/guestAgent.ini \
	${sysconfdir}/mtc/guestServer.ini \
	${sysconfdir}/init.d/guestServer \
	${sysconfdir}/init.d/guestAgent \
	${sysconfdir}/pmon.d/guestServer.conf \
	${bindir}/guestServer \
	${bindir}/guestAgent \
	"

