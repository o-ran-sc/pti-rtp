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

SUMMARY = "Multus CNI enables attaching multiple network interfaces to pods in Kubernetes."
DESCRIPTION = "\
  Multus CNI is a container network interface (CNI) plugin for Kubernetes \
  that enables attaching multiple network interfaces to pods. Typically, \
  in Kubernetes each pod only has one network interface (apart from a loopback) \
  -- with Multus you can create a multi-homed pod that has multiple interfaces. \
  This is accomplished by Multus acting as a "meta-plugin", a CNI plugin that \
  can call multiple other CNI plugins \
"
HOMEPAGE = "https://github.com/intel/multus-cni"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fa818a259cbed7ce8bc2a22d35a464fc"

SRC_URI = "\
    https://github.com/intel/${BPN}/archive/v${PV}.tar.gz;downloadfilename=${BPN}-v${PV}.tar.gz \
"

SRC_URI[md5sum] = "fa75272319b19a6192f9d607b79829ea"
SRC_URI[sha256sum] = "9544fca58e6d1f3943159086651ceb228242b5fd85688bd424d7504c197ec49a"

S = "${WORKDIR}/${BPN}-${PV}"

K8S_PLUGINS = "${sysconfdir}/kubernetes/plugins/${BPN}"

do_install() {
    install -d ${D}${K8S_PLUGINS}
    install -m 644 ${S}/README.md ${D}${K8S_PLUGINS}
    install -m 644 ${S}/images/entrypoint.sh ${D}${K8S_PLUGINS}
    install -m 644 ${S}/images/README.md ${D}${K8S_PLUGINS}/README-deployment.md
    install -m 644 ${S}/images/multus-daemonset-pre-1.16.yml ${D}${K8S_PLUGINS}/multus-daemonset-pre-1.16.yml
    install -m 644 ${S}/images/multus-daemonset.yml ${D}${K8S_PLUGINS}/multus-daemonset.yml
}
