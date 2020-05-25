#
## Copyright (C) 2019 Wind River Systems, Inc.
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

EXTRA_IMAGE_FEATURES += " empty-root-password"
EXTRA_IMAGE_FEATURES += " allow-empty-password"
EXTRA_IMAGE_FEATURES += " allow-root-login"

#Systemd system
VIRTUAL-RUNTIME_init_manager = "systemd"
DISTRO_FEATURES_NATIVE_append = "systemd"
DISTRO_FEATURES_append = " systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED_append = "sysvinit"

#Security
# INITRAMFS_IMAGE = "secure-core-image-initramfs"
# DISTRO_FEATURES_append = " selinux"
#DISTRO_FEATURES_NATIVE_append = "ima tpm tpm2 efi-secure-boot luks"
#DISTRO_FEATURES_append += "ima tpm tpm2 efi-secure-boot luks modsign"
#MACHINE_FEATURES_NATIVE_append = "efi"
#MACHINE_FEATURES_append = "efi"
#INITRAMFS_SCRIPTS = "initramfs-live-boot \
#                     initramfs-live-install \
#                    initramfs-live-install-efi \
#                    "

#SECURE_CORE_IMAGE_EXTRA_INSTALL ?= "\
#    packagegroup-efi-secure-boot \
#    packagegroup-tpm \
#    packagegroup-tpm2 \
#    packagegroup-ima \
#    packagegroup-luks \
#    "


# virtualization and cloud stuff
DISTRO_FEATURES_append = " virtualization"
DISTRO_FEATURES_append = " kvm"
DISTRO_FEATURES_append = " openstack"
PREFERRED_PROVIDER_virtual/containerd = "containerd-opencontainers"

# packagegroup-base-zeroconf depends on libnss-mdns which breaks
# the dns resolution for StarlingX
DISTRO_FEATURES_remove = "zeroconf"

#Graphics and misc
#DISTRO_FEATURES_append = " x11 opengl"
#EXTRA_IMAGE_FEATURES_append = " x11-base"
#VIRTUAL-RUNTIME_graphical_init_manager = "lxdm"

PACKAGE_CLASSES = "package_rpm"
DISTRO_FEATURES_append = " bluez pam largefile opengl"

#misc
PREFERRED_PROVIDER_virtual/containerd = "containerd-opencontainers"
PREFERRED_PROVIDER_virtual/kernel ?= "linux-yocto"

VIRTUAL-RUNTIME_syslog = "syslog-ng"
VIRTUAL-RUNTIME_vim = "vim"

DISTRO_FEATURES_append = " selinux"
PREFERRED_PROVIDER_virtual/refpolicy ?= "refpolicy-mls"
