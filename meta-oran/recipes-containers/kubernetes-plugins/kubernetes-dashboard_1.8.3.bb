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

SRC_URI[md5sum] = "8c3949eea7b9f7dd15d70d7d1c9af77b"
SRC_URI[sha256sum] = "9096f86d4107a6d23f2cff5edd1acae2faf25719a343c319860fd6a7408f761d"

S = "${WORKDIR}/dashboard-${PV}"

K8S_PLUGINS = "${sysconfdir}/kubernetes/plugins/${BPN}"

do_install() {
    install -d ${D}${K8S_PLUGINS}
    install -m 644 ${WORKDIR}/kubernetes-dashboard-admin.rbac.yaml ${D}${K8S_PLUGINS}
    install -m 644 ${S}/src/deploy/recommended/kubernetes-dashboard.yaml ${D}${K8S_PLUGINS}
    install -m 644 ${S}/README.md ${D}${K8S_PLUGINS}
}
