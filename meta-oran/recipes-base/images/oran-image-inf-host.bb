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

DESCRIPTION = "An image suitable for a O-RAN INF host."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

require recipes-base/images/oran-image-inf-minimal.bb

IMAGE_INSTALL += " \
    aufs-util \
    celt051 \
    dpdk \
    hwloc \
    kernel-modules \
    openvswitch \
    packagegroup-base-extended \
    packagegroup-oran \
    packagegroup-wr-base \
    packagegroup-wr-base-net \
    python-pyparsing \
    rt-tests \
    schedtool-dl \
"

IMAGE_INSTALL_append_x86-64 = " \
    ceph \
    spice \
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
