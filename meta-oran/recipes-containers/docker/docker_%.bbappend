#
# Copyright (C) 2019 Wind River Systems, Inc.
#

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

inherit go112

do_install_append() {
    sed -i '/ExecStart=/a ExecStartPost=/usr/sbin/iptables -P FORWARD ACCEPT' ${D}${systemd_unitdir}/system/docker.service
}
