require config-common.inc

SUBPATH0 = "sysinv/sysinv-agent"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI += "file://0003-sysinv-agent-remove-legacy-pid-directory.patch;striplevel=3"

RDEPENDS_sysinv-agent += " python"

inherit systemd
SYSTEMD_PACKAGES += "sysinv-agent"
SYSTEMD_SERVICE_sysinv-agent = "sysinv-agent.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install_append() {


	install -d -m 755 ${D}${sysconfdir}/init.d
	install -p -D -m 755 ${S}/sysinv-agent ${D}/${sysconfdir}/init.d/sysinv-agent

	install -d -m 755 ${D}${sysconfdir}/pmon.d
	install -p -D -m 644 ${S}/sysinv-agent.conf ${D}/${sysconfdir}/pmon.d/sysinv-agent.conf
	install -p -D -m 644 ${S}/sysinv-agent.service ${D}/${systemd_system_unitdir}/sysinv-agent.service

}

