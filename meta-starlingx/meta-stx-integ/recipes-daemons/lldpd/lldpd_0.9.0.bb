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

SUMMARY = "A 802.1ab implementation (LLDP) to help you locate neighbors of all your equipments"
SECTION = "net/misc"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/ISC;md5=f3b90e78ea0cffb20bf5cca7947a896d"

DEPENDS = "libbsd libevent json-c"

SRC_URI = "\
    http://media.luffy.cx/files/${BPN}/${BPN}-${PV}.tar.gz \
    "

SRC_URI[md5sum] = "ed0226129b0c90b3a45c273fe1aba8de"
SRC_URI[sha256sum] = "300e4a590f7bf21c79d5ff94c2d6a69d0b2c34dbc21e17281496462a04ca80bc"

inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "networking/lldpd"

SRC_URI_STX = " \
    file://centos/files/lldpd-create-run-dir.patch \
    file://centos/files/lldpd-i40e-disable.patch \
    file://centos/files/lldpd-clear-station.patch \
    file://${BP}/lldpd-interface-show.patch \
    "

SOURCE1 = "${STX_METADATA_PATH}/${BP}/lldpd.init"
SOURCE2 = "${STX_METADATA_PATH}/${BP}/lldpd.default"
SOURCE3 = "${STX_METADATA_PATH}/centos/files/i40e-lldp-configure.sh"

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

inherit autotools update-rc.d useradd systemd pkgconfig bash-completion

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system -g lldpd --shell /bin/false lldpd"
GROUPADD_PARAM_${PN} = "--system lldpd"

EXTRA_OECONF += "--without-embedded-libevent \
                 --disable-oldies \
                 --with-privsep-user=lldpd \
                 --with-privsep-group=lldpd \
                 --with-systemdsystemunitdir=${systemd_system_unitdir} \
                 --without-sysusersdir \
"

PACKAGECONFIG ??= "cdp fdp edp sonmp lldpmed dot1 dot3"
PACKAGECONFIG[xml] = "--with-xml,--without-xml,libxm2"
PACKAGECONFIG[snmp] = "--with-snmp,--without-snmp,net-snmp"
PACKAGECONFIG[readline] = "--with-readline,--without-readline,readline"
PACKAGECONFIG[seccomp] = "--with-seccomp,--without-seccomp,libseccomp"
PACKAGECONFIG[cdp] = "--enable-cdp,--disable-cdp"
PACKAGECONFIG[fdp] = "--enable-fdp,--disable-fdp"
PACKAGECONFIG[edp] = "--enable-edp,--disable-edp"
PACKAGECONFIG[sonmp] = "--enable-sonmp,--disable-sonmp"
PACKAGECONFIG[lldpmed] = "--enable-lldpmed,--disable-lldpmed"
PACKAGECONFIG[dot1] = "--enable-dot1,--disable-dot1"
PACKAGECONFIG[dot3] = "--enable-dot3,--disable-dot3"
PACKAGECONFIG[custom] = "--enable-custom,--disable-custom"

INITSCRIPT_NAME = "lldpd"
INITSCRIPT_PARAMS = "defaults"

SYSTEMD_SERVICE_${PN} = "lldpd.service"

do_install_append() {
    install -d -m 0755 ${D}/${sysconfdir}/init.d
    install -Dm 0755 ${SOURCE1} ${D}${sysconfdir}/init.d/lldpd
    install -Dm 0644 ${SOURCE2} ${D}${sysconfdir}/default/lldpd
    install -Dm 0755 ${SOURCE3} ${D}${sysconfdir}/init.d/
    # Make an empty configuration file
    touch ${D}${sysconfdir}/lldpd.conf
}

PACKAGES =+ "${PN}-zsh-completion"

FILES_${PN} += "${libdir}/sysusers.d"
RDEPENDS_${PN} += "os-release bash"

FILES_${PN}-zsh-completion += "${datadir}/zsh/"
# FIXME: zsh is broken in meta-oe so this cannot be enabled for now
#RDEPENDS_${PN}-zsh-completion += "zsh"
