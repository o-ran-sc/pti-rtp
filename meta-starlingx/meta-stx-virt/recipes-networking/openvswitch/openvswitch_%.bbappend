FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

RDEPENDS_${PN} += " \
		python-pyelftools \
		firewalld \
		logrotate \
		hwdata \
		"

PACKAGECONFIG = "libcap-ng ssl dpdk"
PACKAGECONFIG[ssl] = "--enable-ssl,--disable-ssl,openssl,"
PACKAGECONFIG[dpdk] = "--with-dpdk=${STAGING_DIR_TARGET}${DPDK_INSTALL_DIR}/share/${TARGET_ARCH}-native-linuxapp-gcc,,dpdk,"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "openvswitch-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

do_install_append () {
	cd ${S}
	install -d -p -m0755 ${D}/${base_libdir}/udev/rules.d
	install -d -m0755 ${D}/${systemd_system_unitdir}
	install -p -m0644 rhel/usr_lib_udev_rules.d_91-vfio.rules ${D}/${base_libdir}/udev/rules.d/91-vfio.rules

	install -p -m0644 \
		rhel/usr_lib_systemd_system_ovs-delete-transient-ports.service \
		${D}/${systemd_system_unitdir}/ovs-delete-transient-ports.service 

	install -p -m0644 \
		rhel/usr_lib_systemd_system_ovn-controller.service \
		${D}/${systemd_system_unitdir}/ovn-controller.service \

	install -p -m0644 \
		rhel/usr_lib_systemd_system_ovn-controller-vtep.service \
		${D}/${systemd_system_unitdir}/ovn-controller-vtep.service \

	install -p -m0644 \
		rhel/usr_lib_systemd_system_ovn-northd.service \
		${D}/${systemd_system_unitdir}/ovn-northd.service \

##############
# TODO: Do we need to use sysv? 
# 		
# 	install -m 0755 rhel/etc_init.d_openvswitch \
#        $RPM_BUILD_ROOT%{_datadir}/openvswitch/scripts/openvswitch.init
#
# TODO: Is this the best solution?
#	install -d -m 0755 $RPM_BUILD_ROOT/%{_sysconfdir}/sysconfig/network-scripts/
#	install -p -m 0755 rhel/etc_sysconfig_network-scripts_ifdown-ovs \
#       	$RPM_BUILD_ROOT/%{_sysconfdir}/sysconfig/network-scripts/ifdown-ovs
#	install -p -m 0755 rhel/etc_sysconfig_network-scripts_ifup-ovs \
#		$RPM_BUILD_ROOT/%{_sysconfdir}/sysconfig/network-scripts/ifup-ovs
# TODO: warrior builds openvswitch with python3.
#	install -d -m 0755 $RPM_BUILD_ROOT%{python2_sitelib}
#	cp -a $RPM_BUILD_ROOT/%{_datadir}/openvswitch/python/* \
#		$RPM_BUILD_ROOT%{python2_sitelib}
#
# TODO: who needs this script?
#      install -p -D -m 0755 \
#		rhel/usr_share_openvswitch_scripts_ovs-systemd-reload \
#		$RPM_BUILD_ROOT%{_datadir}/openvswitch/scripts/ovs-systemd-reload
###########

	install -d -p -m0755 ${D}/${sysconfdir}/logrotate.d
	install -p -D -m 0644 rhel/etc_logrotate.d_openvswitch \
		${D}/${sysconfdir}/logrotate.d/openvswitch


	install -d -p -m 0755 ${D}/${sharedstatedir}/openvswitch
	install -d -p  -m 0755 ${D}/${libdir}/firewalld/services/
	install -p -m 0644 rhel/usr_lib_firewalld_services_ovn-central-firewall-service.xml \
		${D}/${libdir}/firewalld/services/ovn-central-firewall-service.xml

	install -d -p -m 0755 ${D}/${libdir}/ocf/resource.d/ovn
	ln -s ${datadir}/openvswitch/scripts/ovndb-servers.ocf  ${D}/${libdir}/ocf/resource.d/ovn/ovndb-servers

	if ${@bb.utils.contains('PACKAGECONFIG', 'dpdk', 'true', 'false', d)}; then
		install -m 0755 ${STAGING_DATADIR}/dpdk/usertools/dpdk-pmdinfo.py ${D}${datadir}/openvswitch/scripts/dpdk-pmdinfo.py
		install -m 0755 ${STAGING_DATADIR}/dpdk/usertools/dpdk-devbind.py ${D}${datadir}/openvswitch/scripts/dpdk-devbind.py
	fi

	install -m 0644 ${STX_METADATA_PATH}/files/ovsdb-server.pmon.conf ${D}/${sysconfdir}/openvswitch/ovsdb-server.pmon.conf
	install -m 0644 ${STX_METADATA_PATH}/files/ovs-vswitchd.pmon.conf ${D}/${sysconfdir}/openvswitch/ovs-vswitchd.pmon.conf
	install -m 0644 ${STX_METADATA_PATH}/files/etc_logrotate.d_openvswitch ${D}/${sysconfdir}/logrotate.d/openvswitch
      
}


FILES_${PN}_append = " \
	${libdir}/	\
	${base_libdir}/ \
	"
