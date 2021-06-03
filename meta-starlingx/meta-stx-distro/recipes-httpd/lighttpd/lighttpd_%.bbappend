FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "lighttpd-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

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

    install -m755 ${STX_METADATA_PATH}/files/lighttpd.init ${D}/${sysconfdir}/init.d/lighttpd

    install -m640 ${STX_METADATA_PATH}/files/lighttpd.conf          ${D}/${sysconfdir}/lighttpd/lighttpd.conf
    install -m644 ${STX_METADATA_PATH}/files/lighttpd-inc.conf      ${D}/${sysconfdir}/lighttpd/lighttpd-inc.conf
    install -m644 ${STX_METADATA_PATH}/files/index.html.lighttpd    ${D}/www/pages/index.html
    install -m644 ${STX_METADATA_PATH}/files/lighttpd.logrotate    ${D}/${sysconfdir}/logrotate.d/lighttpd

}

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"
