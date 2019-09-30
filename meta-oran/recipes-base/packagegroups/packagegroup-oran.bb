#
# Copyright (C) 2019 Wind River Systems, Inc.
#

DESCRIPTION = "Packagegroup for ORAN packages"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES = " \
    ${PN}-base \
    ${PN}-docker \
    ${PN}-k8s \
    ${PN}-vm \
    ${PN}-criu \
    ${PN}-trace-tools \
    ${PN}-lttng-toolchain \
"

RDEPENDS_${PN} = "\
    ${PN}-base \
    ${PN}-docker \
    ${PN}-k8s \
    ${PN}-vm \
    ${PN}-criu \
    ${PN}-trace-tools \
    ${PN}-lttng-toolchain \
"

RDEPENDS_${PN}-base = "\
    vim \
    rt-tests \
    tunctl \
    udev \
    udev-extraconf \
"

RDEPENDS_${PN}-docker = "\
    docker \
    docker-registry \
"

RDEPENDS_${PN}-k8s = "\
    cni \
    iproute2-tc \
    kubernetes \
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

RDEPENDS_${PN}-criu = "\
    criu \
    protobuf-c \
"

RDEPENDS_${PN}-trace-tools = "\
    diod \
    socat \
"

RDEPENDS_${PN}-trace-tools = "\
    babeltrace \
    lttng-tools \
    lttng-ust \
"
