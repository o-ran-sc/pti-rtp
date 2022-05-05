
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
PV = "1.16.2+git${SRCREV_kubernetes}"
SRCREV_kubernetes = "c97fe5036ef3df2967d086711e6c0c405941e14b"

LICENSE += "(Apache-2.0&MIT)&(Apache-2.0|CC-BY-4.0)"
LIC_FILES_CHKSUM_append = " \
	file://src/import/logo/LICENSE;md5=b431638b9986506145774a9da0d0ad85 \
	file://src/import/vendor/github.com/morikuni/aec/LICENSE;md5=86852eb2df591157c788f3ba889c8aec \
	file://src/import/staging/src/k8s.io/sample-controller/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://src/import/test/images/kitten/Dockerfile;beginline=1;endline=13;md5=78cb21f802c15df77b75bd56f9417ccf \
	file://src/import/test/images/nautilus/Dockerfile;beginline=1;endline=13;md5=78cb21f802c15df77b75bd56f9417ccf \
	file://src/import/staging/src/k8s.io/kubectl/LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e \
	file://src/import/vendor/github.com/grpc-ecosystem/go-grpc-middleware/LICENSE;md5=7ab5c73bb7e4679b16dd7c11b3559acf \
	"

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
