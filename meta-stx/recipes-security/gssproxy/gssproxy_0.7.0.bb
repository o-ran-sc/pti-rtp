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

SUMMARY = "provides a daemon to manage access to GSSAPI credentials"
HOMEPAGE = "https://pagure.io/gssproxy"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=a9ac3d0a983ebc781f7aa7173499e2e5"

DEPENDS += "popt ding-libs keyutils libverto krb5 libxslt-native libxml2-native \
            libselinux libpthread-stubs gettext-native \
            "
RDEPENDS_${PN} += "libinih popt libverto-libevent libverto-tevent keyutils \
                   libgssapi-krb5 libgssrpc libk5crypto libkadm5clnt-mit \
                   libkadm5srv-mit libkdb5 libkrad libkrb5 libkrb5support \
                   libxslt libxslt-bin libxml2-utils libxml2 \
                   "


FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = "https://releases.pagure.org/gssproxy/gssproxy-${PV}.tar.gz \
      file://Properly-renew-expired-credentials.patch;striplevel=2 \
      file://Change-impersonator-check-code.patch;striplevel=2 \
      file://Allow-connection-to-self-when-impersonator-set.patch;striplevel=2 \
      file://Make-proc-file-failure-loud-but-nonfatal.patch;striplevel=2 \
      file://Turn-on-Wextra.patch;striplevel=2 \
      file://Fix-unused-variables.patch;striplevel=2 \
      file://Fix-mismatched-sign-comparisons.patch;striplevel=2 \
      file://Fix-error-checking-on-get_impersonator_fallback.patch;striplevel=2 \
      file://Remove-gpm_release_ctx-to-fix-double-unlock.patch;striplevel=2 \
      file://Appease-gcc-7-s-fallthrough-detection.patch;striplevel=2 \
      file://Fix-memory-leak.patch;striplevel=2 \
      file://Fix-most-memory-leaks.patch;striplevel=2 \
      file://Fix-segfault-when-no-config-files-are-present.patch;striplevel=2 \
      file://Update-systemd-file.patch;striplevel=2 \
      file://Fix-error-handling-in-gp_config_from_dir.patch;striplevel=2 \
      file://Do-not-call-gpm_grab_sock-twice.patch;striplevel=2 \
      file://Only-empty-FILE-ccaches-when-storing-remote-creds.patch;striplevel=2 \
      file://Handle-outdated-encrypted-ccaches.patch;striplevel=2 \
      file://Separate-cred-and-ccache-manipulation-in-gpp_store_r.patch;striplevel=2 \
      file://Properly-locate-credentials-in-collection-caches-in-.patch;striplevel=2 \
      file://Properly-initialize-ccaches-before-storing-into-them.patch;striplevel=2 \
      file://Include-header-for-writev.patch;striplevel=2 \
      file://Tolerate-NULL-pointers-in-gp_same.patch;striplevel=2 \
      file://Add-Client-ID-to-debug-messages.patch;striplevel=2 \
      file://client-Switch-to-non-blocking-sockets.patch;striplevel=2 \
      file://server-Add-detailed-request-logging.patch;striplevel=2 \
      file://Fix-potential-free-of-non-heap-address.patch;striplevel=2 \
      file://Prevent-uninitialized-read-in-error-path-of-XDR-cont.patch;striplevel=2 \
      file://Simplify-setting-NONBLOCK-on-socket.patch;striplevel=2 \
      file://Fix-handling-of-non-EPOLLIN-EPOLLOUT-events.patch;striplevel=2 \
      file://Fix-error-handling-in-gpm_send_buffer-gpm_recv_buffe.patch;striplevel=2 \
      file://Emit-debug-on-queue-errors.patch;striplevel=2 \
      file://Conditionally-reload-kernel-interface-on-SIGHUP.patch;striplevel=2 \
      file://Don-t-leak-mech_type-when-CONTINUE_NEEDED-from-init_.patch;striplevel=2 \
      file://Always-use-the-encype-we-selected.patch;striplevel=2 \
      file://Clarify-debug-and-debug_level-in-man-pages.patch;striplevel=2 \
      file://Always-choose-highest-requested-debug-level.patch;striplevel=2 \
      "

SRC_URI[md5sum] = "1837acb5766ffbc6fcc70ecaa72e285f"

inherit autotools systemd pkgconfig

SYSTEMD_SERVICE_${PN} = "gssproxy.service"

EXTRA_OECONF="--with-pubconf-path=${sysconfdir}/gssproxy \
              --with-initscript=systemd --disable-static \
              --disable-rpath --with-gpp-default-behavior=REMOTE_FIRST \
              --with-systemdunitdir=${systemd_system_unitdir} \
              --with-manpages=no \
              "

CFLAGS += "-fPIE -fstack-protector-all"
LDFLAGS += "-fPIE -pie -Wl,-z,now"

