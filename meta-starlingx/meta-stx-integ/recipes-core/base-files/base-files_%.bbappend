SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "setup-config"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
        file://stx-configfiles-LICENSE;beginline=1;endline=10;md5=0ba4936433e3eb7acdd7d236af0d2496 \
        "

SRC_URI += " \
        git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
        "

do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/stx-configfiles/centos/setup-config.spec ${S}/stx-configfiles-LICENSE
}

do_install_append() {

    install -d ${D}/${sysconfdir}/profile.d
    install -m 644 ${WORKDIR}/${DSTSUFX0}/files/motd ${D}/${sysconfdir}/motd
    install -m 644 ${WORKDIR}/${DSTSUFX0}/files/prompt.sh ${D}/${sysconfdir}/profile.d/prompt.sh
    install -m 644 ${WORKDIR}/${DSTSUFX0}/files/custom.sh ${D}/${sysconfdir}/profile.d/custom.sh
   # chmod 600 ${D}/{sysconfdir}/exports
   # chmod 600 ${D}/{sysconfdir}/fstab
}
