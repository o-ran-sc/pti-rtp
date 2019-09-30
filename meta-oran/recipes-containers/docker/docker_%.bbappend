FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

inherit ${@bb.utils.contains('GOVERSION', '1.2.%', 'go112', '', d)}

do_install_append() {
    sed -i '/ExecStart=/a ExecStartPost=/usr/sbin/iptables -P FORWARD ACCEPT' ${D}${systemd_unitdir}/system/docker.service
}
