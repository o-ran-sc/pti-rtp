#
# Copyright (C) 2019 Wind River Systems, Inc.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

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

