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

DESCRIPTION = "High-performance AMQP-compliant message broker written in Erlang."
HOMEPAGE = "http://www.rabbitmq.com/"
SECTION = "network"

LICENSE = "MPL-1.1"
LIC_FILES_CHKSUM = "file://LICENSE-MPL-RabbitMQ;md5=ce8293a7cc187353c90cb24a8ecee4ce"

SRC_URI = " \
    http://www.rabbitmq.com/releases/rabbitmq-server/v${PV}/${PN}-${PV}.tar.xz \
    file://rabbitmq-server-0001-Remove-excessive-sd_notify-code.patch \
    file://rabbitmq-server-0002-Add-systemd-notification-support.patch \
    file://rabbitmq-server-0003-Revert-Distinct-exit-codes-for-CLI-utilities.patch \
    file://rabbitmq-server-0004-Allow-guest-login-from-non-loopback-connections.patch \
    file://rabbitmq-server-0005-Avoid-RPC-roundtrips-in-list-commands.patch \
    file://rabbitmq-server-0006-rabbit_prelaunch-must-use-RABBITMQ_SERVER_ERL_ARGS.patch \
    file://rabbitmq-common-0001-Avoid-RPC-roundtrips-while-listing-items.patch;patchdir=deps/rabbit_common \
    file://rabbitmq-common-0002-Use-proto_dist-from-command-line.patch;patchdir=deps/rabbit_common \
    file://rabbitmq-server-fails-with-home-not-set.patch \
    file://rabbitmq-script-wrapper \
    file://rabbitmq-server.logrotate \
    file://rabbitmq-server.tmpfiles \
"

SRC_URI[md5sum] = "e9f96b5763a89a246f53250e46c2796b"
SRC_URI[sha256sum] = "9550433ca8aaf5130bf5235bb978c44d3c4694cbd09d97114b3859f4895788ec"

DEPENDS = " \
    coreutils-native\
    erlang-ssl \
    erlang-ssl-dev \
    erlang-native \
    libxslt \
    libxslt-native \
    python-simplejson \
    rsync-native \
    unzip-native \
    xmlto-native \
    zip-native \
"

# ../../../../../recipe-sysroot/usr/lib/erlang/lib/ssl-5.3.3/src/
do_compile() {
    export SOCKJS_ERLC_OPTS="-Dpre17_type_specs"
    rm -rf deps/rabbit_common/include/ssl
    mkdir ${S}/deps/rabbit_common/include/ssl
    cp -r ${RECIPE_SYSROOT}/${libdir}/erlang/lib/ssl-5.3.3/src ${S}/deps/rabbit_common/include/ssl
    oe_runmake
}

do_install() {
    RABBIT_LIB_DIR=${libdir}/rabbitmq

    oe_runmake install \
               DESTDIR=${D} \
               PREFIX=${prefix} \
               RMQ_ROOTDIR=${RABBIT_LIB_DIR}

    oe_runmake install-man \
               DESTDIR=${D} \
               PREFIX=${prefix} \
               RMQ_ROOTDIR=${RABBIT_LIB_DIR}

    mkdir -p ${D}${localstatedir}/lib/rabbitmq/mnesia
    mkdir -p ${D}${localstatedir}/log/rabbitmq

    # Copy all necessary lib files etc.
    install -p -D -m 0644 ${S}/docs/rabbitmq-server.service.example ${D}${systemd_system_unitdir}/rabbitmq-server.service
    install -p -D -m 0755 ${WORKDIR}/rabbitmq-script-wrapper ${D}${sbindir}/rabbitmqctl
    install -p -D -m 0755 ${WORKDIR}/rabbitmq-script-wrapper ${D}${sbindir}/rabbitmq-server
    install -p -D -m 0755 ${WORKDIR}/rabbitmq-script-wrapper ${D}${sbindir}/rabbitmq-plugins

    # Make necessary symlinks
    mkdir -p ${D}${RABBIT_LIB_DIR}/bin
    for app in rabbitmq-defaults rabbitmq-env rabbitmq-plugins rabbitmq-server rabbitmqctl; do
        ln -s ${RABBIT_LIB_DIR}/lib/rabbitmq_server-${PV}/sbin/${app} ${D}${RABBIT_LIB_DIR}/bin/${app}
    done

    install -p -D -m 0755 ${S}/scripts/rabbitmq-server.ocf ${D}${exec_prefix}/lib/ocf/resource.d/rabbitmq/rabbitmq-server
    install -p -D -m 0755 ${S}/scripts/rabbitmq-server-ha.ocf ${D}${exec_prefix}/lib/ocf/resource.d/rabbitmq/rabbitmq-server-ha

    install -p -D -m 0644 ${WORKDIR}/rabbitmq-server.logrotate ${D}${sysconfdir}/logrotate.d/rabbitmq-server

    install -p -D -m 0644 ${S}/docs/rabbitmq.config.example ${D}${sysconfdir}/rabbitmq/rabbitmq.config

    rm -rf ${D}${RABBIT_LIB_DIR}/lib/rabbitmq_server-${PV}/{LICENSE,LICENSE-*,INSTALL}

    install -p -D -m 0644 ${WORKDIR}/rabbitmq-server.tmpfiles ${D}${prefix}/lib/tmpfiles.d/${BPN}.conf
}

inherit useradd systemd openssl10

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system rabbitmq"
USERADD_PARAM_${PN}  = " \
    --system --create-home \
    --home ${localstatedir}/lib/rabbitmq \
    -g rabbitmq rabbitmq \
"

SYSTEMD_SERVICE_${PN} = "rabbitmq-server.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

FILES_${PN} += " \
    ${RABBIT_LIB_DIR}/* \
    ${exec_prefix}/lib/* \
    ${localstatedir} \
"

FILES_${PN}-doc += "LICENSE* INSTALL"

RDEPENDS_${PN} = " \
    erlang \
    erlang-modules \
"

INSANE_SKIP_${PN} = "unsafe-references-in-scripts"
