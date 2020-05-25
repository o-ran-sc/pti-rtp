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

SUMMARY = "Flannel is a simple and easy way to configure a layer 3 network fabric designed for Kubernetes."
DESCRIPTION = "\
  Flannel runs a small, single binary agent called flanneld on each host, \
  and is responsible for allocating a subnet lease to each host out of a \
  larger, preconfigured address space. Flannel uses either the Kubernetes \
  API or etcd directly to store the network configuration, the allocated \
  subnets, and any auxiliary data (such as the host's public IP). Packets \
  are forwarded using one of several backend mechanisms including VXLAN and \
  various cloud integrations. \
"
HOMEPAGE = "https://github.com/coreos/flannel"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "\
    https://github.com/coreos/flannel/archive/v${PV}.tar.gz;downloadfilename=${BPN}-v${PV}.tar.gz \
"

SRC_URI[md5sum] = "1007747571bc6b8c951f72f64e567205"
SRC_URI[sha256sum] = "7375318b288bcff733aabfe1a1007d478cb9091cdaffe68c8253ddd93bc070ed"

S = "${WORKDIR}/${BPN}-${PV}"

K8S_PLUGINS = "${sysconfdir}/kubernetes/plugins/${BPN}"

do_install() {
    install -d ${D}${K8S_PLUGINS}
    install -m 644 ${S}/README.md ${D}${K8S_PLUGINS}
    install -m 644 ${S}/Documentation/kube-flannel.yml ${D}${K8S_PLUGINS}
}
