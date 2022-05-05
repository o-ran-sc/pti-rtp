SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "shadow-utils-config"
DSTSUFX0 = "stx-configfiles"
SUBPATH1 = "util-linux-config"
DSTSUFX1 = "stx-util-linux"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://stx-util-linux-LICENSE;beginline=1;endline=10;md5=5801a9b9ee2a1468c289f27bd8ee8af3 \
	"
SRC_URI += " \
	 git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
	 git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX1};branch="r/stx.3.0";subpath=${SUBPATH1};name=opendev \
	"
do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/stx-configfiles/files/LICENSE ${S}/stx-configfiles-LICENSE
    cp -pf ${WORKDIR}/stx-util-linux/centos/util-linux-config.spec ${S}/stx-util-linux-LICENSE
}

do_install_append_class-target () { 

    install -d ${D}/${sysconfdir}/pam.d
    install -m 644 ${WORKDIR}/stx-util-linux/files/stx.su     ${D}/${sysconfdir}/pam.d/su
    install -m 644 ${WORKDIR}/stx-util-linux/files/stx.login  ${D}/${sysconfdir}/pam.d/login

    install -D -m644 ${WORKDIR}/${DSTSUFX0}/files/login.defs ${D}/${sysconfdir}/login.defs
    install -D -m644 ${WORKDIR}/${DSTSUFX0}/files/clear_shadow_locks.service  \
              ${D}/${systemd_system_unitdir}/clear_shadow_locks.service
}

inherit systemd
SYSTEMD_PACKAGES += "shadow"
SYSTEMD_SERVICE_${PN} = "clear_shadow_locks.service"
SYSTEMD_AUTO_ENABLE_${PN} += "enable"
