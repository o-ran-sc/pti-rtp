#
# Copyright (C) 2019 Wind River Systems, Inc.
#
DESCRIPTION = "An image suitable for a O-RAN INF host."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"


require recipes-base/images/oran-image-inf-minimal.bb

IMAGE_INSTALL += " \
    kernel-modules \
    packagegroup-base-extended \
    packagegroup-wr-base \
    packagegroup-wr-base-net \
    packagegroup-wr-boot \
"

IMAGE_INSTALL += " \
    aufs-util \
    celt051 \
    ceph \
    dpdk \
    hwloc \
    openvswitch \
    packagegroup-containers \
    packagegroup-glusterfs \
    packagegroup-oran-criu \
    packagegroup-oran-debug \
    packagegroup-oran-default-monitoring \
    packagegroup-oran-docker \
    packagegroup-oran-lttng-toolchain \
    python-pyparsing \
    rt-tests \
    schedtool-dl \
    spice \
    system-report \
"

IMAGE_FEATURES += " \
    nfs-server \
    package-management \
    wr-core-db \
    wr-core-interactive \
    wr-core-net \
    wr-core-perl \
    wr-core-python \
    wr-core-sys-util \
    wr-core-util \
    wr-core-mail \
"

# enable build out .ext3 image file, shall be useful for qemu
IMAGE_FSTYPES += "ext3"
