#
# Copyright (C) 2019 Wind River Systems, Inc.
#

SUMMARY = "General-purpose web UI for Kubernetes clusters"
DESCRIPTION = "\
  Kubernetes Dashboard is a general purpose, web-based UI \
  for Kubernetes clusters. It allows users to manage applications \
  running in the cluster and troubleshoot them, as well as manage \
  the cluster itself. \
"
HOMEPAGE = "https://github.com/kubernetes/dashboard"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b1e01b26bacfc2232046c90a330332b3"

SRC_URI = "\
    https://github.com/kubernetes/dashboard/archive/v${PV}.tar.gz;downloadfilename=${BPN}-v${PV}.tar.gz \
    file://kubernetes-dashboard-admin.rbac.yaml \
    file://kubernetes-dashboard.yaml-set-the-NodePort-type.patch \
"

SRC_URI[md5sum] = "52c30b2c4e9cfc31a1ea67144e81513f"
SRC_URI[sha256sum] = "088ba30335e2effec4c5386e97ef25f47f9d042b99bbdeba9a1e3faf87ad2a6d"

S = "${WORKDIR}/dashboard-${PV}"

K8S_PLUGINS = "${sysconfdir}/kubernetes/plugins/${BPN}"

do_install() {
    install -d ${D}${K8S_PLUGINS}
    install -m 644 ${WORKDIR}/kubernetes-dashboard-admin.rbac.yaml ${D}${K8S_PLUGINS}
    install -m 644 ${S}/src/deploy/recommended/kubernetes-dashboard.yaml ${D}${K8S_PLUGINS}
    install -m 644 ${S}/README.md ${D}${K8S_PLUGINS}
}
