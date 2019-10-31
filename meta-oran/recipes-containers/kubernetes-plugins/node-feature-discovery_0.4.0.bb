#
# Copyright (C) 2019 Wind River Systems, Inc.
#

SUMMARY = "Node feature discovery for Kubernetes"
DESCRIPTION = "\
  This software enables node feature discovery for Kubernetes. \
  It detects hardware features available on each node in a Kubernetes \
  cluster, and advertises those features using node labels. \
  \
  NFD consists of two software components: \
    - nfd-master is responsible for labeling Kubernetes node objects \
    - nfd-worker is detects features and communicates them to nfd-master. \
      One instance of nfd-worker is supposed to be run on each node of the \
      cluster \
"
HOMEPAGE = "https://github.com/kubernetes-sigs/node-feature-discovery"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e23fadd6ceef8c618fc1c65191d846fa"

SRC_URI = "\
    https://github.com/kubernetes-sigs/${BPN}/archive/v${PV}.tar.gz;downloadfilename=${BPN}-v${PV}.tar.gz \
"

SRC_URI[md5sum] = "16bcac1d904351a88faa5c6752420ce5"
SRC_URI[sha256sum] = "c9f826e6c7a42161befc155ca0f465ac5667903e74c857e978a99c74574f635e"

S = "${WORKDIR}/${BPN}-${PV}"

K8S_PLUGINS = "${sysconfdir}/kubernetes/plugins/${BPN}"

do_configure() {
    :
}

do_compile() {
    :
}

do_install() {
    install -d ${D}${K8S_PLUGINS}
    install -m 644 ${S}/README.md ${D}${K8S_PLUGINS}
    install -m 644 ${S}/nfd-daemonset-combined.yaml.template ${D}${K8S_PLUGINS}
    install -m 644 ${S}/nfd-worker.conf.example ${D}${K8S_PLUGINS}
    install -m 644 ${S}/nfd-worker-job.yaml.template ${D}${K8S_PLUGINS}
    install -m 644 ${S}/nfd-master.yaml.template ${D}${K8S_PLUGINS}/nfd-master.yaml
    install -m 644 ${S}/nfd-worker-daemonset.yaml.template ${D}${K8S_PLUGINS}/nfd-worker-daemonset.yaml
}
