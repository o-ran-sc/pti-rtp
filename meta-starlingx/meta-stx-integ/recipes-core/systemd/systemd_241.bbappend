FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH0 = "systemd-config"
STX_SUBPATH1 = "io-scheduler"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/${STX_SUBPATH0}/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://${STX_METADATA_PATH}/${STX_SUBPATH1}/centos/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

STX_DEFAULT_LOCALE ?= "en_US.UTF-8"

do_install_append () {
	install -d ${D}${sysconfdir}
	echo LANG=${STX_DEFAULT_LOCALE} >> ${D}${sysconfdir}/locale.conf
	
	install -d -m 0755 ${D}/${sysconfdir}/udev/rules.d
	install -d -m 0755 ${D}/${sysconfdir}/tmpfiles.d
	install -d -m 0755 ${D}/${sysconfdir}/systemd
	
	install -m644 ${STX_METADATA_PATH}/${STX_SUBPATH0}/files/60-persistent-storage.rules \
		${D}/${sysconfdir}/udev/rules.d/60-persistent-storage.rules

	install -m644 ${STX_METADATA_PATH}/${STX_SUBPATH0}/files/systemd.conf.tmpfiles.d ${D}/${sysconfdir}/tmpfiles.d/systemd.conf
	install -m644 ${STX_METADATA_PATH}/${STX_SUBPATH0}/files/tmp.conf.tmpfiles.d ${D}/${sysconfdir}/tmpfiles.d/tmp.conf
	install -m644 ${STX_METADATA_PATH}/${STX_SUBPATH0}/files/tmp.mount ${D}/${sysconfdir}/systemd/system/tmp.mount
	install -m644 ${STX_METADATA_PATH}/${STX_SUBPATH1}/centos/files/60-io-scheduler.rules \
		${D}/${sysconfdir}/udev/rules.d/60-io-scheduler.rules

}


FILES_${PN} += "${sysconfdir}/locale.conf"

PACKAGECONFIG_append = " \
    coredump \
"
