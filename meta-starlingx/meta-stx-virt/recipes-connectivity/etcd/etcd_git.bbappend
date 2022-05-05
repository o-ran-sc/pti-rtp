
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
    file://etcd.service \
    file://etcd.conf \
    "

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "etcd.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

do_install_append() {
	install -m 0644 ${WORKDIR}/etcd.service ${D}${systemd_system_unitdir}
	install -d ${D}${sysconfdir}/etcd
	install -m 0644 ${WORKDIR}/etcd.conf ${D}${sysconfdir}/etcd
}
