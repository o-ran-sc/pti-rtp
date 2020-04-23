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

SUMMARY = "TCP/HTTP proxy and load balancer for high availability environments"
DESCRIPTION = " \
HAProxy is a TCP/HTTP reverse proxy which is particularly suited for high \
availability environments. Indeed, it can: \
 - route HTTP requests depending on statically assigned cookies \
 - spread load among several servers while assuring server persistence \
   through the use of HTTP cookies \
 - switch to backup servers in the event a main server fails \
 - accept connections to special ports dedicated to service monitoring \
 - stop accepting connections without breaking existing ones \
 - add, modify, and delete HTTP headers in both directions \
 - block requests matching particular patterns \
 - report detailed status to authenticated users from a URI \
   intercepted by the application \
"
HOMEPAGE = "http://www.haproxy.org/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2d862e836f92129cdc0ecccc54eed5e0"

DEPENDS = "libpcre openssl zlib"

MAJOR_VER = "${@'.'.join(d.getVar('PV').split('.')[0:2])}"

SRC_URI = "https://www.haproxy.org/download/${MAJOR_VER}/src/haproxy-${PV}.tar.gz \
           file://haproxy.service \
           file://haproxy.cfg \
          "

inherit systemd useradd

# create a user for running haproxy
HAP_USER_HOME = "${libdir}/haproxy"
USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --home ${HAP_USER_HOME} --shell /sbin/nologin \
                       --groups haproxy --gid haproxy haproxy"
GROUPADD_PARAM_${PN} = "haproxy"

# for haproxy 1.x
HAP_TARGET = "linux2628"

EXTRA_OEMAKE = " \
	'CPU=generic' \
	'TARGET=${HAP_TARGET}' \
	'USE_GETADDRINFO=1' \
	'USE_OPENSSL=1' \
	'USE_PCRE=1' 'USE_PCRE_JIT=1' \
	'USE_ZLIB=1' \
	'USE_LINUX_TPROXY=1' \
	"

EXTRA_OEMAKE_append_x86 = " USE_REGPARM=1"
EXTRA_OEMAKE_append_x86-64 = " USE_REGPARM=1"


do_configure() {
	:
}

do_compile() {
	oe_runmake CC="${CC}" CFLAGS="${CFLAGS}" SBINDIR="${bindir}" \
	           PREFIX="${prefix}" \
	           ZLIB_INC=${STAGING_INCDIR} \
	           ZLIB_LIB=${STAGING_LIBDIR} \
	           PCRE_INC=${STAGING_INCDIR} \
	           PCRE_LIB=${STAGING_LIBDIR} \
	           SSL_INC=${STAGING_INCDIR} \
	           SSL_LIB=${STAGING_LIBDIR}
	oe_runmake -C contrib/halog halog
	oe_runmake -C contrib/iprange iprange
}

do_install() {
	oe_runmake install-bin \
	           PREFIX="${prefix}" \
	           SBINDIR="${bindir}" \
	           DESTDIR=${D} \
	           INCLUDEDIR=${includedir}

	install -D -m 0644 ${WORKDIR}/haproxy.service ${D}${systemd_system_unitdir}/haproxy.service
	install -D -m 0644 ${WORKDIR}/haproxy.cfg ${D}${sysconfdir}/haproxy/haproxy.cfg

	# install ssl folder for certificate
	install -m 700 -d ${D}/${sysconfdir}/ssl/haproxy
	chown haproxy:haproxy ${D}/${sysconfdir}/ssl/haproxy

	# install halog and iprange
	install -D -m 755 contrib/halog/halog ${D}${bindir}/halog
	install -D -m 755 contrib/iprange/iprange ${D}${bindir}/iprange
}

FILES_${PN} = " \
	${bindir} \
	${sbindir} \
	${sysconfdir} \
	${systemd_system_unitdir} \
	"
RDEPENDS_${PN} = "openssl"

SYSTEMD_SERVICE_${PN} = "haproxy.service"

INSANE_SKIP_${PN} += "already-stripped"
