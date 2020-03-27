#
# Copyright (C) 2020 Wind River Systems, Inc.
#

require kubernetes.inc

PV = "1.16.2+git${SRCREV_kubernetes}"
SRCREV_kubernetes = "c97fe5036ef3df2967d086711e6c0c405941e14b"
SRC_BRANCH = "release-1.16"

SRC_URI += "\
	file://kubernetes-accounting.conf \
	file://kubeadm.conf \
	file://kubelet-cgroup-setup.sh \
"

INSANE_SKIP_${PN} += "textrel"
INSANE_SKIP_${PN}-misc += "textrel"
INSANE_SKIP_kubelet += "textrel"

SYSTEMD_AUTO_ENABLE_kubelet = "disable"

inherit go112

do_install_append() {
    # Install the sysctl config for k8s
    # install -d ${D}${sysconfdir}/sysctl.d/
    # install -m 644 -D ${WORKDIR}/k8s.conf ${D}${sysconfdir}/sysctl.d/

	# kubeadm:
	install -d -m 0755 ${D}/${sysconfdir}/systemd/system/kubelete.service.d
	install -m 0644 ${WORKDIR}/kubeadm.conf ${D}/${sysconfdir}/systemd/system/kubelete.service.d

	# kubelete-cgroup-setup.sh
	install -d -m 0755 ${D}/${bindir}
	install -m 0644 ${WORKDIR}/kubelet-cgroup-setup.sh ${D}/${bindir}

	# enable CPU and Memory accounting
	install -d -m 0755 ${D}/${sysconfdir}/systemd/system.conf.d
	install -m 0644 ${WORKDIR}/kubernetes-accounting.conf ${D}/${sysconfdir}//systemd/system.conf.d/
}

