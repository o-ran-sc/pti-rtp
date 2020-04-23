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

PACKAGES += " worker-utils"

RDEPENDS_worker-utils  += " perl systemd python bash"
DESCRIPTION_worker-utils  = "Initial worker node resource reservation and misc. utilities"
SUMMARY_worker-utils  = "dynamic MOTD generation"

inherit setuptools distutils python-dir

do_configure_append() {
	:
}
do_compile_append() {
	cd ${S}/utilities/worker-utils/worker-utils
	oe_runmake all
}

do_install_append() {
	cd ${S}/utilities/worker-utils/worker-utils

	oe_runmake install \
		BINDIR=${D}/${bindir} \
		INITDDIR=${D}/${sysconfdir}/init.d \
		GOENABLEDDIR=${D}/${sysconfdir}/goenabled.d \
		PLATFORMCONFDIR=${D}/${sysconfdir}/platform \
		SYSTEMDDIR=${D}/${systemd_system_unitdir}
}

FILES_worker-utils = "  \
	${sysconfdir}/init.d/affine-platform.sh \
	${sysconfdir}/init.d/affine-tasks.sh \
	${sysconfdir}/init.d/cpumap_functions.sh \
	${sysconfdir}/init.d/task_affinity_functions.sh \
	${bindir}/ps-sched.sh \
	${bindir}/topology.py \
	${bindir}/topology.pyc \
	${bindir}/affine-interrupts.sh \
	${bindir}/set-cpu-wakeup-latency.sh \
	${bindir}/topology \
	${sysconfdir}/platform/worker_reserved.conf \
	${sysconfdir}/goenabled.d/worker-goenabled.sh \
	${systemd_system_unitdir}/affine-platform.sh.service \
	${systemd_system_unitdir}/affine-tasks.service \
	"
