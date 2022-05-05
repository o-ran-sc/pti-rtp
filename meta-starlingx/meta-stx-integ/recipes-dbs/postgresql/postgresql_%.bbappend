COMPUTE_IP="127.0.0.1"
CONTROLLER_IP="127.0.0.1"

FILESEXTRAPATHS_prepend := "${THISDIR}/postgresql:"

require ${@bb.utils.contains('DISTRO_FEATURES', 'openstack', '${BPN}_openstack.inc', '', d)}

SRC_URI += " \
    file://postgresql.service.update \
    "

do_install_append() {
       install -d ${D}${systemd_unitdir}/system
       install -m 0755 ${WORKDIR}/postgresql.service.update ${D}${systemd_unitdir}/system/postgresql.service
}

FILES_${PN} += "${systemd_unitdir}/system/postgresql.service"
