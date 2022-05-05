FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "memcached-custom"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;beginline=1;endline=10;md5=b3063b05db239c326cb7f5c267e0d023 \
	"

SRC_URI += " \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
	file://memcached.sysconfig \
	"

inherit useradd

USERADD_PACKAGES = "${PN}"

USERADD_PARAM_${PN} = "-r -g memcached -d /run/memcached -s /sbin/nologin -c 'Memcached daemon' memcached"
GROUPADD_PARAM_${PN} = "-r memcached"


do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
   cp -pf ${WORKDIR}/${DSTSUFX0}/centos/memcached-custom.spec ${S}/stx-configfiles-LICENSE
}

do_install_append () {
    install -d ${D}${sysconfdir}/sysconfig
    install -d ${D}/${sysconfdir}/systemd/system/
    install -m 0644 ${WORKDIR}/memcached.sysconfig ${D}${sysconfdir}/sysconfig/memcached
    install -m 0644 ${WORKDIR}/${DSTSUFX0}/files/memcached.service \
    		${D}/${sysconfdir}/systemd/system/memcached
}
