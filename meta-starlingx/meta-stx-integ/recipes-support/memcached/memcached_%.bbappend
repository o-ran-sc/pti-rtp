FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "memcached-custom"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/centos/memcached-custom.spec;beginline=1;endline=10;md5=b3063b05db239c326cb7f5c267e0d023 \
	"

SRC_URI += " \
	file://memcached.sysconfig \
	"

inherit useradd

USERADD_PACKAGES = "${PN}"

USERADD_PARAM_${PN} = "-r -g memcached -d /run/memcached -s /sbin/nologin -c 'Memcached daemon' memcached"
GROUPADD_PARAM_${PN} = "-r memcached"

do_install_append () {
    install -d ${D}${sysconfdir}/sysconfig
    install -d ${D}/${sysconfdir}/systemd/system/
    install -m 0644 ${WORKDIR}/memcached.sysconfig ${D}${sysconfdir}/sysconfig/memcached
    install -m 0644 ${STX_METADATA_PATH}/files/memcached.service \
    		${D}/${sysconfdir}/systemd/system/memcached
}
