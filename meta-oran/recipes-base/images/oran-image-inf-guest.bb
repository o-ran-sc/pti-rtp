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

DESCRIPTION = "An image suitable for a O-RAN INF guest."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

require recipes-base/images/oran-image-inf-minimal.bb

IMAGE_INSTALL += " \
    acpid-default-scripts \
    hwloc \
    kernel-modules \
    packagegroup-base-extended \
    packagegroup-oran-trace-tools \
    packagegroup-wr-base \
    packagegroup-wr-base-net \
"
IMAGE_INSTALL_append_qemux86-64 = " dpdk"

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

COMPATIBLE_MACHINE = "qemux86|qemux86-64"
