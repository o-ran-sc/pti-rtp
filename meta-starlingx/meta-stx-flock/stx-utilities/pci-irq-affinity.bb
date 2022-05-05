DESCRIPTION  = "StarlingX PCI Interrupt Affinity Agent Package"
SUMMARY  = "StarlingX PCI Interrupt Affinity Agent Package"

require utilities-common.inc

SUBPATH0 = "utilities/pci-irq-affinity-agent/pci_irq_affinity"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://setup.py;md5=6980f60051ba4d376975eefc777fb8ae"

RDEPENDS_${PN}_append = " python-novaclient libvirt libvirt-python"

inherit setuptools systemd
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"
SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_SERVICE_${PN} = "${PN}-agent.service"

do_unpack_append() {
    bb.build.exec_func('do_restore_files', d)
}

do_restore_files() {
	cd ${S}
	git reset ${SRCREV} utilities/pci-irq-affinity-agent/files
	git checkout utilities/pci-irq-affinity-agent/files
}


do_install_append() {

	cd ${S}/utilities/pci-irq-affinity-agent/files

	install -p -d -m0755 ${D}/${sysconfdir}/init.d
	install -p -d -m0755 ${D}/${sysconfdir}/pmon.d
	install -p -d -m0755 ${D}/${sysconfdir}/pci_irq_affinity
	install -p -d -m0755 ${D}/${systemd_system_unitdir}
	install -p -d -m0755 ${D}/${bindir}

	install -m0755 pci-irq-affinity-agent ${D}/${sysconfdir}/init.d/pci-irq-affinity-agent
	install -m0644 pci-irq-affinity-agent.service ${D}/${systemd_system_unitdir}/pci-irq-affinity-agent.service

	install -m0755 nova-sriov ${D}/${bindir}/nova-sriov
	install -m0755 config.ini ${D}/${sysconfdir}/pci_irq_affinity/config.ini
}
