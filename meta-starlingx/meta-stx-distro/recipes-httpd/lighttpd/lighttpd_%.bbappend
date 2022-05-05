FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "lighttpd-config"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"
SRC_URI += " \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
	file://lighttpd-init-script-chroot.patch;striplevel=3 \
	"

do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/${DSTSUFX0}/files/LICENSE ${S}/stx-configfiles-LICENSE
    cp -pf ${WORKDIR}/${DSTSUFX0}/files/lighttpd.init ${S}/lighttpd.init
}

do_install_append () {
    # remove the symlinks
    rm ${D}/www/logs
    rm ${D}/www/var

    # use tmpfile to create dirs
    install -d ${D}${sysconfdir}/tmpfiles.d/
    echo "d /www/var 0755 www root -" > ${D}${sysconfdir}/tmpfiles.d/${BPN}.conf
    echo "d /www/var/log 0755 www root -" >> ${D}${sysconfdir}/tmpfiles.d/${BPN}.conf


    install -d -m 1777 ${D}/www/tmp
    install -d ${D}/${sysconfdir}/lighttpd/ssl
    install -d ${D}/www/pages/dav

    install -d -m755 ${D}/${sysconfdir}/logrotate.d

    install -m755 ${S}/lighttpd.init ${D}/${sysconfdir}/init.d/lighttpd

    install -m640 ${WORKDIR}/${DSTSUFX0}/files/lighttpd.conf          ${D}/${sysconfdir}/lighttpd/lighttpd.conf
    install -m644 ${WORKDIR}/${DSTSUFX0}/files/lighttpd-inc.conf      ${D}/${sysconfdir}/lighttpd/lighttpd-inc.conf
    install -m644 ${WORKDIR}/${DSTSUFX0}/files/index.html.lighttpd    ${D}/www/pages/index.html
    install -m644 ${WORKDIR}/${DSTSUFX0}/files/lighttpd.logrotate    ${D}/${sysconfdir}/logrotate.d/lighttpd

}

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"
