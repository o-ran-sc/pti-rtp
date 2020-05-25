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

SUMMARY = "Lightweight, easy to configure DNS forwarder and DHCP server"
DESCRIPTION = "\
Dnsmasq is lightweight, easy to configure DNS forwarder and DHCP server. \
It is designed to provide DNS and, optionally, DHCP, to a small network. \
It can serve the names of local machines which are not in the global \
DNS. The DHCP server integrates with the DNS server and allows machines \
with DHCP-allocated addresses to appear in the DNS with names configured \
either in each host or in a central configuration file. Dnsmasq supports \
static and dynamic DHCP leases and BOOTP for network booting of diskless \
machines. \
"
HOMEPAGE = "http://www.thekelleys.org.uk/dnsmasq"
SECTION = "net"

# GPLv3 was added in version 2.41 as license option
LICENSE = "GPLv2 | GPLv3"
LIC_FILES_CHKSUM = "\
    file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
    file://COPYING-v3;md5=d32239bcb673463ab874e80d47fae504 \
"

SRC_URI = " \
    http://www.thekelleys.org.uk/${BPN}/${BP}.tar.gz \
    file://init \
    file://dnsmasq-resolvconf.service \
    file://lua.patch \
    \
    file://stx/dnsmasq-2.76-dns-sleep-resume.patch \
    file://stx/dnsmasq-2.76-fix-dhcp-option-arrangements.patch \
    file://stx/dnsmasq-2.76-pftables.patch \
    file://stx/dnsmasq-2.76-fix-crash-dns-resume.patch \
    file://stx/dnsmasq-2.76-warning-fixes.patch \
    file://stx/dnsmasq-2.76-label-warning.patch \
    file://stx/dnsmasq-2.76-label-man.patch \
    file://stx/dnsmasq-2.76-coverity.patch \
    file://stx/dnsmasq-2.76-dhcp-script-log.patch \
    file://stx/dnsmasq-2.76-file_offset32.patch \
    file://stx/dnsmasq-2.76-CVE-2017-14491.patch \
    file://stx/dnsmasq-2.76-CVE-2017-14492.patch \
    file://stx/dnsmasq-2.76-CVE-2017-14493.patch \
    file://stx/dnsmasq-2.76-CVE-2017-14494.patch \
    file://stx/dnsmasq-2.76-CVE-2017-14496.patch \
    file://stx/dnsmasq-2.76-CVE-2017-14495.patch \
    file://stx/dnsmasq-2.76-gita3303e196.patch \
    file://stx/dnsmasq-2.76-underflow.patch \
    file://stx/dnsmasq-2.76-misc-cleanups.patch \
    file://stx/dnsmasq-2.76-CVE-2017-14491-2.patch \
    file://stx/dnsmasq-2.76-inotify.patch \
    file://stx/dnsmasq-update-ipv6-leases-from-config.patch \
    file://stx/close-tftp-sockets-immediately.patch \
    file://stx/dnsmasq.service \
"
SRC_URI[md5sum] = "6610f8233ca89b15a1bb47c788ffb84f"
SRC_URI[sha256sum] = "777c4762d2fee3738a0380401f2d087b47faa41db2317c60660d69ad10a76c32"

inherit pkgconfig update-rc.d systemd

INITSCRIPT_NAME = "dnsmasq"
INITSCRIPT_PARAMS = "defaults"

PACKAGECONFIG ?= "dbus idn"
PACKAGECONFIG[dbus] = ",,dbus"
PACKAGECONFIG[idn] = ",,libidn"
PACKAGECONFIG[conntrack] = ",,libnetfilter-conntrack"
PACKAGECONFIG[lua] = ",,lua"
PACKAGECONFIG[resolvconf] = ",,,resolvconf"

EXTRA_OEMAKE = "\
    'COPTS=${@bb.utils.contains('PACKAGECONFIG', 'dbus', '-DHAVE_DBUS', '', d)} \
           ${@bb.utils.contains('PACKAGECONFIG', 'idn', '-DHAVE_IDN', '', d)} \
           ${@bb.utils.contains('PACKAGECONFIG', 'conntrack', '-DHAVE_CONNTRACK', '', d)} \
           ${@bb.utils.contains('PACKAGECONFIG', 'lua', '-DHAVE_LUASCRIPT', '', d)}' \
    'CFLAGS=${CFLAGS}' \
    'LDFLAGS=${LDFLAGS}' \
"

SRC_URI += "${@bb.utils.contains('PACKAGECONFIG', 'resolvconf', 'file://dnsmasq.resolvconf file://99_dnsmasq file://dnsmasq-resolvconf-helper', '', d)}"

do_compile_append() {
    # build dhcp_release
    cd ${S}/contrib/lease-tools
    oe_runmake
}

do_install () {
    oe_runmake "PREFIX=${D}${prefix}" \
               "BINDIR=${D}${bindir}" \
               "MANDIR=${D}${mandir}" \
               install

    install -d ${D}${sysconfdir}/
    install -d ${D}${sysconfdir}/init.d
    install -d ${D}${sysconfdir}/dnsmasq.d

    install -m 644 ${S}/dnsmasq.conf.example ${D}${sysconfdir}/dnsmasq.conf
    cat << EOF >> ${D}${sysconfdir}/dnsmasq.conf

# Include all files in /etc/dnsmasq.d except RPM backup files
conf-dir=/etc/dnsmasq.d,.rpmnew,.rpmsave,.rpmorig
EOF

    install -m 755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/dnsmasq

    install -d ${D}${systemd_system_unitdir}

    if [ "${@bb.utils.filter('PACKAGECONFIG', 'resolvconf', d)}" ]; then
        install -m 0644 ${WORKDIR}/dnsmasq-resolvconf.service ${D}${systemd_system_unitdir}/dnsmasq.service
    else
        install -m 0644 ${WORKDIR}/stx/dnsmasq.service ${D}${systemd_system_unitdir}/dnsmasq.service
    fi

    install -m 0755 ${S}/contrib/lease-tools/dhcp_release ${D}${bindir}
    install -m 0755 ${S}/contrib/lease-tools/dhcp_release6 ${D}${bindir}
    install -m 0755 ${S}/contrib/lease-tools/dhcp_lease_time ${D}${bindir}

    if [ "${@bb.utils.filter('PACKAGECONFIG', 'dbus', d)}" ]; then
        install -d ${D}${sysconfdir}/dbus-1/system.d
        install -m 644 dbus/dnsmasq.conf ${D}${sysconfdir}/dbus-1/system.d/
    fi
    if [ "${@bb.utils.filter('PACKAGECONFIG', 'resolvconf', d)}" ]; then
        install -d ${D}${sysconfdir}/resolvconf/update.d/
        install -m 0755 ${WORKDIR}/dnsmasq.resolvconf ${D}${sysconfdir}/resolvconf/update.d/dnsmasq

        install -d ${D}${sysconfdir}/default/volatiles
        install -m 0644 ${WORKDIR}/99_dnsmasq ${D}${sysconfdir}/default/volatiles
        install -m 0755 ${WORKDIR}/dnsmasq-resolvconf-helper ${D}${bindir}
    fi
}

CONFFILES_${PN} = "${sysconfdir}/dnsmasq.conf"

RPROVIDES_${PN} += "${PN}-systemd"
RREPLACES_${PN} += "${PN}-systemd"
RCONFLICTS_${PN} += "${PN}-systemd"
SYSTEMD_SERVICE_${PN} = "dnsmasq.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
