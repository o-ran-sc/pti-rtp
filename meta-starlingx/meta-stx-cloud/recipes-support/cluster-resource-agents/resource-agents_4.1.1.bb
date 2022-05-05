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

SUMMARY = "OCF resource agents for use by compatible cluster managers"

DESCRIPTION = "A set of scripts to interface with several services \
to operate in a High Availability environment for both Pacemaker and \
rgmanager service managers. \
"
HOMEPAGE = "http://sources.redhat.com/cluster/wiki/"

LICENSE = "GPLv2+ & LGPLv2+ & GPLv3"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
    file://COPYING.LGPL;md5=4fbd65380cdd255951079008b364516c \
    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
"

SRC_URI = " \
    https://codeload.github.com/ClusterLabs/${BPN}/tar.gz/v${PV};downloadfilename=${BPN}-${PV}.tar.gz \
    file://0001-disable-doc-build.patch \
    file://0002-Set-OCF_ROOT_DIR-to-libdir-ocf.patch \
    file://0003-fix-header-defs-lookup.patch \
    file://0004-configure.ac-fix-install-sh-not-found.patch \
    \
    file://stx/new_ocf_return_codes.patch \
    file://stx/ipaddr2_check_if_state.patch \
    file://stx/copyright.patch \
    file://stx/umount-in-namespace.patch \
    file://stx/lvm_vg_activation.patch \
    file://stx/pgsql.patch \
    file://stx/Fix-VG-activity-bug-in-heartbeat-LVM-script.patch \
    file://stx/ocf-shellfuncs_change_logtag.patch \
    file://stx/lvm_cleanup_refs_on_stop.patch \
    file://stx/ipaddr2_if_down.patch \
    file://stx/ipaddr2_ignore_lo_if_state.patch \
    file://stx/ipaddr2-avoid-failing-svc-if-down.patch \
    file://stx/ipaddr2-use-host-scope-for-addresses-on-loopback.patch \
"

SRC_URI[md5sum] = "82e3a335f942347f1b7c27b9f8e8e984"
SRC_URI[sha256sum] = "decc370bce20ee7a034886eb19ddb54d823f5e95c58f4d121f53cc965147f736"

DEPENDS = "cluster-glue"

inherit autotools systemd pkgconfig
inherit update-alternatives

ALTERNATIVE_PRIORITY = "70"
ALTERNATIVE_${PN} = "drbd.sh"
ALTERNATIVE_LINK_NAME[drbd.sh] = "${datadir}/cluster/drbd.sh"

EXTRA_OECONF += " \
    --disable-fatal-warnings \
"

do_install_append() {
    rm -rf "${D}${localstatedir}/run"
    rmdir --ignore-fail-on-non-empty "${D}${localstatedir}"

    mv ${D}${datadir}/cluster/drbd.sh ${D}${datadir}/cluster/drbd.sh.${PN}

    # Create symbolic link between IPAddr and IPAddr2
    rm -f ${D}${libdir}/ocf/resource.d/heartbeat/IPaddr
    ln -s ${libdir}/ocf/resource.d/heartbeat/IPaddr2 ${D}${libdir}/ocf/resource.d/heartbeat/IPaddr
}

PACKAGES_prepend  = " \
    ldirectord \
"

FILES_ldirectord = " \
    ${sbindir}/ldirectord \
    ${sysconfdir}/ha.d/resource.d/ldirectord \
    ${sysconfdir}/init.d/ldirectord \
    ${sysconfdir}/logrotate.d/ldirectord \
    ${libdir}/ocf/resource.d/heartbeat/ldirectord \
"

FILES_${PN} += " \
    ${datadir}/cluster/* \
    ${datadir}/${BPN}/ocft/configs/portblock \
    ${libdir}/ocf/resource.d/heartbeat/ \
    ${libdir}/ocf/lib/heartbeat/* \
    ${libdir}/ocf/resource.d/redhat \
    ${libdir}/tmpfiles.d/ \
"

FILES_${PN}-dbg += " \
    ${libdir}/ocf/resource.d/heartbeat/.debug \
    ${libdir}/${BPN}/heartbeat/.debug \
"

# There are many tools and scripts that need bash and perl.
# lvm.sh requires: lvm2
# ip.sh requires: ethtool iproute2 iputils-arping
# fs.sh requires: e2fsprogs-e2fsck util-linux quota
# netfs.sh requires: procps util-linux nfs-utils
RDEPENDS_${PN} += " \
    bash perl lvm2 \
    ethtool iproute2 iputils-arping \
    e2fsprogs-e2fsck util-linux quota \
    procps nfs-utils \
"

RDEPENDS_ldirectord += " \
    ipvsadm \
    libdbi-perl \
    libdigest-hmac-perl \
    libmailtools-perl \
    libnet-dns-perl \
    libsocket6-perl \
    libwww-perl \
    perl \
    perl-module-getopt-long \
    perl-module-net-ftp \
    perl-module-net-smtp \
    perl-module-pod-usage \
    perl-module-posix \
    perl-module-socket \
    perl-module-strict \
    perl-module-sys-hostname \
    perl-module-sys-syslog \
    perl-module-vars \
"

SYSTEMD_PACKAGES += "ldirectord"
SYSTEMD_SERVICE_${PN} += "resource-agents-deps.target"
SYSTEMD_SERVICE_ldirectord += "ldirectord.service"
