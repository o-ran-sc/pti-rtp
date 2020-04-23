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

SUMMARY = "Manage Linux IP sets"
DESCRIPTION = " \
IP sets are a framework inside the Linux kernel since version 2.4.x, which can  \
be administered by the ipset utility. Depending on the type, currently an IP \
set may store IP addresses, (TCP/UDP) port numbers or IP addresses with MAC \
addresses in a way, which ensures lightning speed when matching an entry \
against a set. \
\
If you want to: \
 - store multiple IP addresses or port numbers and match against the collection \
   by iptables at one swoop; \
 - dynamically update iptables rules against IP addresses or ports without \
   performance penalty; \
 - express complex IP address and ports based rulesets with one single iptables \
   rule and benefit from the speed of IP sets \
then ipset may be the proper tool for you. \
"

HOMEPAGE = "http://ipset.netfilter.org"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

SECTION = "network"

DEPENDS = "libtool libmnl"

SRC_URI = "http://ftp.netfilter.org/pub/ipset/${BP}.tar.bz2"

SRC_URI[md5sum] = "0e5d9c85f6b78e7dff0c996e2900574b"
SRC_URI[sha256sum] = "ceef625ba31fe0aaa422926c7231a819de0b07644c02c17ebdd3022a29e3e244"

inherit autotools pkgconfig module-base

EXTRA_OECONF += "-with-kbuild=${KBUILD_OUTPUT} --with-ksource=${STAGING_KERNEL_DIR}"

RDEPENDS_${PN} = "kernel-module-ip-set"
RRECOMMENDS_${PN} = "\
    kernel-module-ip-set-bitmap-ip \
    kernel-module-ip-set-bitmap-ipmac \
    kernel-module-ip-set-bitmap-port \
    kernel-module-ip-set-hash-ip \
    kernel-module-ip-set-hash-ipmac \
    kernel-module-ip-set-hash-ipmark \
    kernel-module-ip-set-hash-ipport \
    kernel-module-ip-set-hash-ipportip \
    kernel-module-ip-set-hash-ipportnet \
    kernel-module-ip-set-hash-mac \
    kernel-module-ip-set-hash-net \
    kernel-module-ip-set-hash-netiface \
    kernel-module-ip-set-hash-netnet \
    kernel-module-ip-set-hash-netport \
    kernel-module-ip-set-hash-netportnet \
    kernel-module-ip-set-list-set \
"
