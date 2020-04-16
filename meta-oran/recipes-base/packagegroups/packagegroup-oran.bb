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

DESCRIPTION = "Packagegroup for ORAN packages"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES += " \
    ${PN}-base \
    ${PN}-docker \
    ${PN}-kernel \
    ${PN}-k8s \
    ${PN}-vm \
    ${PN}-trace-tools \
    ${PN}-lttng-toolchain \
    ${PN}-glusterfs \
"

RDEPENDS_${PN} = "\
    ${PN}-base \
    ${PN}-docker \
    ${PN}-kernel \
    ${PN}-k8s \
    ${PN}-vm \
    ${PN}-trace-tools \
    ${PN}-lttng-toolchain \
    ${PN}-glusterfs \
"

RDEPENDS_${PN}-base = "\
    vim \
    rt-tests \
    tunctl \
    udev \
    udev-extraconf \
    turbostat \
    cpupower \
    cpufrequtils \
    msr-tools \
    htop \
"

RDEPENDS_${PN}-docker = "\
    docker \
    docker-registry \
"

RDEPENDS_${PN}-kernel = "\
    kernel-base \
    kernel-dev \
    kernel-devsrc \
    kernel-modules \
    kernel-vmlinux \
"
RDEPENDS_${PN}-k8s = "\
    cni \
    flannel \
    iproute2-tc \
    kubernetes \
    kubernetes-dashboard \
    node-feature-discovery \
    multus-cni \
"

RDEPENDS_${PN}-vm = "\
    qemu \
    libvirt \
    libvirt-libvirtd \
    libvirt-virsh \
    libvmi \
"

RRECOMMENDS_${PN}-vm = "\
    kernel-module-kvm \
    kernel-module-kvm-intel \
    kernel-module-kvm-amd \
"

RDEPENDS_${PN}-trace-tools = "\
    socat \
"
# It's blacklisted becasue of build failure
#    diod

RDEPENDS_${PN}-lttng-toolchain = "\
    babeltrace \
    lttng-tools \
    lttng-ust \
"

RDEPENDS_${PN}-glusterfs = "\
    fuse \
    fuse-utils \
    libulockmgr \
    glusterfs \
    glusterfs-rdma \
    glusterfs-geo-replication \
    glusterfs-fuse \
    glusterfs-server \
    xfsdump \
    xfsprogs \
"
