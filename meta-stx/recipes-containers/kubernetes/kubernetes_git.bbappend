#
## Copyright (C) 2019 Wind River Systems, Inc.
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

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
PV = "1.16.2+git${SRCREV_kubernetes}"
SRCREV_kubernetes = "c97fe5036ef3df2967d086711e6c0c405941e14b"

SRC_URI = "git://github.com/kubernetes/kubernetes.git;branch=release-1.16;name=kubernetes \
	file://0001-hack-lib-golang.sh-use-CC-from-environment.patch \
	file://0001-cross-don-t-build-tests-by-default.patch \
	file://kubernetes-accounting.conf \
	file://kubeadm.conf \
	file://kubelet-cgroup-setup.sh \
	file://contrib/* \
	"

INSANE_SKIP_${PN} += "textrel"
INSANE_SKIP_${PN}-misc += "textrel"
INSANE_SKIP_kubelet += "textrel"

inherit useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "-r -g kube -d / -s /sbin/nologin -c 'Kubernetes user' kube"
GROUPADD_PARAM_${PN} = "-r kube"

do_install () {
	install -d ${D}${bindir}
	install -d ${D}${systemd_system_unitdir}/

	# Install binaries
	install -m 755 -D ${S}/src/import/_output/local/bin/${TARGET_GOOS}/${TARGET_GOARCH}/* ${D}/${bindir}

	# kubeadm:
	install -d -m 0755 ${D}/${sysconfdir}/systemd/system/kubelet.service.d
	install -m 0644 ${WORKDIR}/kubeadm.conf ${D}/${sysconfdir}/systemd/system/kubelet.service.d

	# kubelete-cgroup-setup.sh
	install -m 0700 ${WORKDIR}/kubelet-cgroup-setup.sh ${D}/${bindir}

	# install the bash completion
	install -d -m 0755 ${D}${datadir}/bash-completion/completions/
	${D}${bindir}/kubectl completion bash > ${D}${datadir}/bash-completion/completions/kubectl

	# install config files
	install -d -m 0755 ${D}${sysconfdir}/${BPN}
	install -m 644 -t ${D}${sysconfdir}/${BPN} ${WORKDIR}/contrib/init/systemd/environ/*

	# install service files
	install -d -m 0755 ${D}${systemd_system_unitdir}
	install -m 0644 -t ${D}${systemd_system_unitdir} ${WORKDIR}/contrib/init/systemd/*.service

	# install the place the kubelet defaults to put volumes
	install -d ${D}${localstatedir}/lib/kubelet

	# install systemd tmpfiles
	install -d -m 0755 ${D}${sysconfdir}/tmpfiles.d
	install -p -m 0644 -t ${D}${sysconfdir}/tmpfiles.d ${WORKDIR}/contrib/init/systemd/tmpfiles.d/kubernetes.conf

	# enable CPU and Memory accounting
	install -d -m 0755 ${D}/${sysconfdir}/systemd/system.conf.d
	install -m 0644 ${WORKDIR}/kubernetes-accounting.conf ${D}/${sysconfdir}//systemd/system.conf.d/
}

SYSTEMD_PACKAGES += "${PN} kube-proxy"
SYSTEMD_SERVICE_kube-proxy = "kube-proxy.service"
SYSTEMD_SERVICE_${PN} = "\
	kube-scheduler.service \
	kube-apiserver.service \
	kube-controller-manager.service \
	"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
SYSTEMD_AUTO_ENABLE_kubelet = "disable"
SYSTEMD_AUTO_ENABLE_kube-proxy = "disable"

FILES_${PN} += "\
	${bindir}/kube-scheduler \
	${bindir}/kube-apiserver \
	${bindir}/kube-controller-manager \
	${bindir}/hyperkube \
	${bindir}/kubelet-cgroup-setup.sh \
	"

FILES_kubectl += "\
	${datadir}/bash-completion/completions/kubectl \
	"

FILES_${PN}-misc = "\
	${bindir}/conversion-gen \
	${bindir}/openapi-gen \
	${bindir}/apiextensions-apiserver \
	${bindir}/defaulter-gen \
	${bindir}/mounter \
	${bindir}/deepcopy-gen \
	${bindir}/go-bindata \
	${bindir}/go2make \
	"

RDEPENDS_${PN} += "\
	bash \
	kube-proxy \
	"
