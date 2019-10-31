#
# Copyright (C) 2019 Wind River Systems, Inc.
#

SUMMARY = "CPU Manager for Kubernetes"
uDESCRIPTION = "\
  This project provides basic core affinity for NFV-style workloads \
  on top of vanilla Kubernetes v1.5+. \
  This project ships a single multi-use command-line program to perform \
  various functions for host configuration, managing groups of CPUs, \
  and constraining workloads to specific CPUs. \
"
HOMEPAGE = "https://github.com/intel/CPU-Manager-for-Kubernetes"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d62f25248fea71c71fb2b520c72b5171"

SRC_URI = "\
    https://github.com/intel/${BPN}/archive/v${PV}.tar.gz;downloadfilename=${BPN}-v${PV}.tar.gz \
    file://cmk-requirements.txt-add-urllib3-1.24.patch \
    file://cmk-cluster-init-pod-template.yaml \
"

SRC_URI[md5sum] = "5ec9f665524b86654dedb2e6826851ed"
SRC_URI[sha256sum] = "e86feb81751c6715247577c47070beca273022b470ae09c856e6da72f185688f"

S = "${WORKDIR}/CPU-Manager-for-Kubernetes-${PV}"

K8S_PLUGINS_SRC = "/opt/kubernetes_plugins/${BPN}"
K8S_PLUGINS = "${sysconfdir}/kubernetes/plugins/${BPN}"

do_configure() {
    :
}

do_compile() {
    :
}

do_install() {
    # Install the config files
    install -d ${D}${K8S_PLUGINS}
    install -m 644 ${S}/resources/authorization/cmk-serviceaccount.yaml ${D}${K8S_PLUGINS}
    install -m 644 ${S}/resources/authorization/cmk-rbac-rules.yaml ${D}${K8S_PLUGINS}
    install -m 644 ${WORKDIR}/cmk-cluster-init-pod-template.yaml ${D}${K8S_PLUGINS}

    # Install all the src
    install -d ${D}${K8S_PLUGINS_SRC}
    cp -a --no-preserve=ownership ${S}/* ${D}${K8S_PLUGINS_SRC}
}

FILES_${PN} += "${K8S_PLUGINS_SRC}"

# provides a short alias
RPROVIDES_${PN} = "cmk"

INSANE_SKIP_${PN} = "file-rdeps"
