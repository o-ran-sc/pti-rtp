SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "net-snmp-config"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;beginline=1;endline=10;md5=ea07d0b28c02168e45abc208d8193e1a \
	"

SRC_URI += " \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
	"
	
inherit useradd

USERADD_PACKAGES = "net-snmp-server-snmpd"
USERADD_PARAM_net-snmp-server-snmpd = "-r -g snmpd -d /usr/share/snmp -s /sbin/nologin -c 'net-snmp' snmpd"
GROUPADD_PARAM_net-snmp-server-snmpd = "-r snmpd"

SYSTEMD_AUTO_ENABLE_${PN}-server-snmpd = "disable"
SYSTEMD_AUTO_ENABLE_${PN}-server-snmptrapd = "disable"

SYSTEMD_SERVICE_${PN}_append = "snmpd.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"


do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
   cp -pf ${WORKDIR}/${DSTSUFX0}/centos/net-snmp-config.spec ${S}/stx-configfiles-LICENSE
}

do_install_append () {

    install -d ${D}/${sysconfdir}/rc.d/init.d
    install -d ${D}/${sysconfdir}/init.d
    install -d ${D}/${sysconfdir}/systemd/system

    install -m 640 ${WORKDIR}/${DSTSUFX0}/files/stx.snmpd.conf    ${D}/${sysconfdir}/snmp/snmpd.conf
    install -m 755 ${WORKDIR}/${DSTSUFX0}/files/stx.snmpd         ${D}/${sysconfdir}/rc.d/init.d/snmpd
    install -m 755 ${WORKDIR}/${DSTSUFX0}/files/stx.snmpd         ${D}/${sysconfdir}/init.d/snmpd
    install -m 660 ${WORKDIR}/${DSTSUFX0}/files/stx.snmp.conf     ${D}/${datadir}/snmp/snmp.conf
    install -m 644 ${WORKDIR}/${DSTSUFX0}/files/snmpd.service     ${D}/${sysconfdir}/systemd/system/snmpd.service
    chmod 640 ${D}/${sysconfdir}/snmp/snmpd.conf
    chmod 640 ${D}/${sysconfdir}/snmp/snmptrapd.conf
}

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

FILES_${PN}-server-snmpd_append = " ${sysconfdir}/rc.d/init.d/snmpd"
