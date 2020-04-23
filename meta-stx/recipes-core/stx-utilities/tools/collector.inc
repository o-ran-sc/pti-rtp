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

PACKAGES += " collector"

RDEPENDS_collector += " bash"
DESCRIPTION_collector= " \
This packages scripts that implement data and log collection that field \
support can execute to gather current state and runtime history for off \
platform analysis and debug. \
"

do_configure_append() {
	:
}
do_compile_append() {
	:
}

do_install_append() {
	cd ${S}/tools/collector/scripts

	install -m0755 -d ${D}/${sysconfdir}/collect.d
	install -m0755 -d ${D}/${sysconfdir}/collect
	install -m0755 -d ${D}/${sbindir}
	install -m0755 -d ${D}/${bindir}
	install -m0755 -d ${D}/${sbindir}

	install -m 755 collect ${D}/${sbindir}/collect
	install -m 755 collect_host ${D}/${sbindir}/collect_host
	install -m 755 collect_date ${D}/${sbindir}/collect_date
	install -m 755 collect_utils ${D}/${sbindir}/collect_utils
	install -m 755 collect_parms ${D}/${sbindir}/collect_parms
	install -m 755 collect_mask_passwords ${D}/${sbindir}/collect_mask_passwords
	install -m 755 expect_done ${D}/${sbindir}/expect_done

	install -m 755 collect_sysinv.sh ${D}/${sysconfdir}/collect.d/collect_sysinv
	install -m 755 collect_psqldb.sh ${D}/${sysconfdir}/collect.d/collect_psqldb
	install -m 755 collect_openstack.sh ${D}/${sysconfdir}/collect.d/collect_openstack
	install -m 755 collect_networking.sh ${D}/${sysconfdir}/collect.d/collect_networking
	install -m 755 collect_ceph.sh ${D}/${sysconfdir}/collect.d/collect_ceph
	install -m 755 collect_sm.sh ${D}/${sysconfdir}/collect.d/collect_sm
	install -m 755 collect_tc.sh ${D}/${sysconfdir}/collect.d/collect_tc
	install -m 755 collect_nfv_vim.sh ${D}/${sysconfdir}/collect.d/collect_nfv_vim
	install -m 755 collect_ovs.sh ${D}/${sysconfdir}/collect.d/collect_ovs
	install -m 755 collect_patching.sh ${D}/${sysconfdir}/collect.d/collect_patching
	install -m 755 collect_coredump.sh ${D}/${sysconfdir}/collect.d/collect_coredump
	install -m 755 collect_crash.sh ${D}/${sysconfdir}/collect.d/collect_crash
	install -m 755 collect_ima.sh ${D}/${sysconfdir}/collect.d/collect_ima
	install -m 755 collect_fm.sh ${D}/${sysconfdir}/collect.d/collect_fm
	install -m 755 collect_containerization.sh ${D}/${sysconfdir}/collect.d/collect_containerization

	install -m 755 etc.exclude ${D}/${sysconfdir}/collect/etc.exclude
	install -m 755 run.exclude ${D}/${sysconfdir}/collect/run.exclude

	ln -sf ${sbindir}/collect ${D}/${bindir}/collect
}

FILES_collector = "  \
	${sbindir}/collect \
	${sbindir}/collect_host \
	${sbindir}/collect_date \
	${sbindir}/collect_utils \
	${sbindir}/collect_parms \
	${sbindir}/collect_mask_passwords \
	${sbindir}/expect_done \
	${sysconfdir}/collect.d/collect_sysinv \
	${sysconfdir}/collect.d/collect_psqldb \
	${sysconfdir}/collect.d/collect_openstack \
	${sysconfdir}/collect.d/collect_networking \
	${sysconfdir}/collect.d/collect_ceph \
	${sysconfdir}/collect.d/collect_sm \
	${sysconfdir}/collect.d/collect_tc \
	${sysconfdir}/collect.d/collect_nfv_vim \
	${sysconfdir}/collect.d/collect_ovs \
	${sysconfdir}/collect.d/collect_patching \
	${sysconfdir}/collect.d/collect_coredump \
	${sysconfdir}/collect.d/collect_crash \
	${sysconfdir}/collect.d/collect_ima \
	${sysconfdir}/collect.d/collect_fm \
	${sysconfdir}/collect.d/collect_containerization \
	${sysconfdir}/collect/etc.exclude \
	${sysconfdir}/collect/run.exclude \
	${bindir}/collect \
	"
