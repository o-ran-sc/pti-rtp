inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH0 = "setup-config"
STX_SUBPATH1 = "centos-release-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
        file://${STX_METADATA_PATH}/${STX_SUBPATH0}/centos/setup-config.spec;beginline=1;endline=10;md5=0ba4936433e3eb7acdd7d236af0d2496 \
        "

do_install_append() {

    install -d ${D}/${sysconfdir}/profile.d
    install -m 644 ${STX_METADATA_PATH}/${STX_SUBPATH0}/files/motd ${D}/${sysconfdir}/motd
    install -m 644 ${STX_METADATA_PATH}/${STX_SUBPATH0}/files/prompt.sh ${D}/${sysconfdir}/profile.d/prompt.sh
    install -m 644 ${STX_METADATA_PATH}/${STX_SUBPATH0}/files/custom.sh ${D}/${sysconfdir}/profile.d/custom.sh

    install -m 644 ${STX_METADATA_PATH}/${STX_SUBPATH1}/files/issue ${D}/${sysconfdir}/issue
    install -m 644 ${STX_METADATA_PATH}/${STX_SUBPATH1}/files/issue.net ${D}/${sysconfdir}/issue.net
    sed -i -e 's/@PLATFORM_RELEASE@/${STX_REL}/' ${D}${sysconfdir}/issue*
}
