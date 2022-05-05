FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "systemd-config"
DSTSUFX0 = "stx-configfiles"
SUBPATH1 = "io-scheduler"
DSTSUFX1 = "stx-io-scheduler"


LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://stx-ioscheduler-LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"
SRC_URI += " \
	file://0900-inject-milisec-in-syslog-date.patch \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX1};branch="r/stx.3.0";subpath=${SUBPATH1};name=opendev \
	"

STX_DEFAULT_LOCALE ?= "en_US.UTF-8"

do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -f ${WORKDIR}/${DSTSUFX0}/files/LICENSE ${S}/stx-configfiles-LICENSE
    cp -f ${WORKDIR}/${DSTSUFX1}/centos/files/LICENSE ${S}/stx-ioscheduler-LICENSE
}

do_install_append () {
	install -d ${D}${sysconfdir}
	echo LANG=${STX_DEFAULT_LOCALE} >> ${D}${sysconfdir}/locale.conf
	
	install -d -m 0755 ${D}/${sysconfdir}/udev/rules.d
	install -d -m 0755 ${D}/${sysconfdir}/tmpfiles.d
	install -d -m 0755 ${D}/${sysconfdir}/systemd
	
	install -m644 ${WORKDIR}/${DSTSUFX0}/files/60-persistent-storage.rules \
		${D}/${sysconfdir}/udev/rules.d/60-persistent-storage.rules

	install -m644 ${WORKDIR}/${DSTSUFX0}/files/systemd.conf.tmpfiles.d ${D}/${sysconfdir}/tmpfiles.d/systemd.conf
	install -m644 ${WORKDIR}/${DSTSUFX0}/files/tmp.conf.tmpfiles.d ${D}/${sysconfdir}/tmpfiles.d/tmp.conf
	install -m644 ${WORKDIR}/${DSTSUFX0}/files/tmp.mount ${D}/${sysconfdir}/systemd/system/tmp.mount
	install -m644 ${WORKDIR}/${DSTSUFX1}/centos/files/60-io-scheduler.rules \
		${D}/${sysconfdir}/udev/rules.d/60-io-scheduler.rules

}


FILES_${PN} += "${sysconfdir}/locale.conf"

PACKAGECONFIG_append = " \
    coredump \
"
