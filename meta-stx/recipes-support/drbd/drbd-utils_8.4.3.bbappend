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

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}:"


SRC_URI += " \
	file://0001-skip_wait_con_int_on_simplex.patch \
	file://0002-drbd-conditional-crm-dependency.patch \
	file://0003-drbd_report_condition.patch \
	file://0004-drbdadm-ipaddr-change.patch \
	file://0005-drbd_reconnect_standby_standalone.patch \
	file://0006-avoid-kernel-userspace-version-check.patch \
	file://0007-Update-OCF-to-attempt-connect-in-certain-states.patch \
	file://0008-Increase-short-cmd-timeout-to-15-secs.patch \
	file://0009-Check-for-mounted-device-before-demoting-Primary-DRB.patch \
	file://0010-Include-sysmacros-for-major-minor-macros.patch \
	file://0011-Disable-documentation.patch \
	file://drbd.service \
	"

EXTRA_OECONF = " \
		--with-utils			\
                --with-initdir=/etc/init.d	\
		--without-km			\
                --with-pacemaker		\
                --with-rgmanager		\
                --with-bashcompletion		\
		--with-udev			\
		--with-heartbeat		\
                --with-distro debian		\
               "

FILES_${PN} = "\
	/var/lib/drbd \
	/run \
	${base_sbindir}/drbdsetup \
	${base_sbindir}/drbdadm \
	${base_sbindir}/drbdmeta \
	${nonarch_base_libdir}/drbd/drbdsetup-83 \
	${nonarch_base_libdir}/drbd/drbdadm-83 \
	${sysconfdir}/init.d/drbd \
	${sysconfdir}/drbd.conf \
	${sysconfdir}/xen \
	${sysconfdir}/drbd.d \
	${sbindir}/drbd-overview \
	${libdir}/drbd/outdate-peer.sh \
	${libdir}/drbd/snapshot-resync-target-lvm.sh \
	${libdir}/drbd/unsnapshot-resync-target-lvm.sh \
	${libdir}/drbd/notify-out-of-sync.sh \
	${libdir}/drbd/notify-split-brain.sh \
	${libdir}/drbd/notify-emergency-reboot.sh \
	${libdir}/drbd/notify-emergency-shutdown.sh \
	${libdir}/drbd/notify-io-error.sh \
	${libdir}/drbd/notify-pri-lost-after-sb.sh \
	${libdir}/drbd/notify-pri-lost.sh \
	${libdir}/drbd/notify-pri-on-incon-degr.sh \
	${libdir}/drbd/notify.sh \
	${libdir}/drbd/rhcs_fence \
	${sysconfdir}/udev/rules.d/65-drbd.rules \
	${libdir}/drbd/crm-fence-peer.sh \
	${libdir}/drbd/crm-unfence-peer.sh \
	${libdir}/drbd/stonith_admin-fence-peer.sh \
	${libdir}/ocf/resource.d/linbit/drbd \
	${datadir}/cluster/drbd.sh.drbd-utils \
	${datadir}/cluster/drbd.metadata \
	${sysconfdir}/ha.d/resource.d/drbddisk \
	${sysconfdir}/ha.d/resource.d/drbdupper \
	${sysconfdir}/bash_completion.d/drbdadm* \
	${systemd_system_unitdir}/drbd.service \
	"

inherit update-alternatives
ALTERNATIVE_PRIORITY = "80"
ALTERNATIVE_${PN} = "drbd.sh"
ALTERNATIVE_LINK_NAME[drbd.sh] = "${datadir}/cluster/drbd.sh"

do_install_append() {
	mv ${D}${datadir}/cluster/drbd.sh ${D}${datadir}/cluster/drbd.sh.drbd-utils
	install -d -m 755 ${D}/${systemd_system_unitdir}
	install -p -D -m 644 ${WORKDIR}/drbd.service ${D}/${systemd_system_unitdir}
}

#inherit systemd
#SYSTEMD_PACKAGES += "${PN}"
#SYSTEMD_SERVICE_${PN} = "drbd.service"

pkg_postinst_ontarget_drbd-utils() {
	${base_bindir}/systemctl enable drbd.service
}
