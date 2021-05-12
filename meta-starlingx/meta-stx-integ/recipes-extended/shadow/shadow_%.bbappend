inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH0 = "shadow-utils-config"
STX_SUBPATH1 = "util-linux-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/${STX_SUBPATH0}/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://${STX_METADATA_PATH}/${STX_SUBPATH1}/centos/util-linux-config.spec;beginline=1;endline=10;md5=5801a9b9ee2a1468c289f27bd8ee8af3 \
	"

do_install_append_class-target () { 

    install -d ${D}/${sysconfdir}/pam.d
    install -m 644 ${STX_METADATA_PATH}/${STX_SUBPATH1}/files/stx.su     ${D}/${sysconfdir}/pam.d/su
    install -m 644 ${STX_METADATA_PATH}/${STX_SUBPATH1}/files/stx.login  ${D}/${sysconfdir}/pam.d/login

    install -D -m644 ${STX_METADATA_PATH}/${STX_SUBPATH0}/files/login.defs ${D}/${sysconfdir}/login.defs
    install -D -m644 ${STX_METADATA_PATH}/${STX_SUBPATH0}/files/clear_shadow_locks.service  \
              ${D}/${systemd_system_unitdir}/clear_shadow_locks.service
}

inherit systemd
SYSTEMD_PACKAGES += "shadow"
SYSTEMD_SERVICE_${PN} = "clear_shadow_locks.service"
SYSTEMD_AUTO_ENABLE_${PN} += "enable"
