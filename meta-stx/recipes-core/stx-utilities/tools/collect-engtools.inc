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

PACKAGES += " collect-engtools"

RDEPENDS_collect-engtools += " \
	iperf3 \
	bash \
	perl \
	python \
	"

SUMMARY_collect-engtools= "Host performance data collection tools package"
DESCRIPTION_collect-engtools= " \
This package contains data collection tools to monitor host performance. \
Tools are general purpose engineering and debugging related. Includes \
overall memory, cpu occupancy, per-task cpu, per-task scheduling, per-task \
io. \
"


do_configure_append() {
	:
}
do_compile_append() {
	:
}

do_install_append() {
	cd ${S}/tools/engtools/hostdata-collectors/scripts

	install -d -m0755 ${D}/${bindir}
	install -m 755 buddyinfo.py ${D}/${bindir}
	install -m 755 chewmem ${D}/${bindir}
	install -m 755 ceph.sh ${D}/${bindir}
	install -m 755 cleanup-engtools.sh ${D}/${bindir}
	install -m 755 collect-engtools.sh ${D}/${bindir}
	install -m 755 diskstats.sh ${D}/${bindir}
	install -m 755 engtools_util.sh ${D}/${bindir}
	install -m 755 filestats.sh ${D}/${bindir}
	install -m 755 iostat.sh ${D}/${bindir}
	install -m 755 linux_benchmark.sh ${D}/${bindir}
	install -m 755 memstats.sh ${D}/${bindir}
	install -m 755 netstats.sh ${D}/${bindir}
	install -m 755 postgres.sh ${D}/${bindir}
	install -m 755 rabbitmq.sh ${D}/${bindir}
	install -m 755 remote/rbzip2-engtools.sh ${D}/${bindir}
	install -m 755 remote/rstart-engtools.sh ${D}/${bindir}
	install -m 755 remote/rstop-engtools.sh ${D}/${bindir}
	install -m 755 remote/rsync-engtools-data.sh ${D}/${bindir}
	install -m 755 slab.sh ${D}/${bindir}
	install -m 755 ticker.sh ${D}/${bindir}
	install -m 755 top.sh ${D}/${bindir}
	install -m 755 vswitch.sh ${D}/${bindir}
	install -m 755 live_stream.py ${D}/${bindir}

	install -p -d -m0755 ${D}/${sysconfdir}/engtools/
	install -m0644 -p cfg/engtools.conf ${D}/${sysconfdir}/engtools
	install -d -m0755 ${D}/${sysconfdir}/init.d
	install -m0755 init.d/collect-engtools.sh ${D}/${sysconfdir}/init.d

	install -d -m0755 ${D}/${systemd_system_unitdir}
	install -m0644 -p -D collect-engtools.service ${D}/${systemd_system_unitdir}

	sed -i -e 's/network.service/networking.service/' \
		${D}/${systemd_system_unitdir}/collect-engtools.service

	sed -i -e 's|/usr/local/bin/|${bindir}/|' \
		${D}/${sysconfdir}/init.d/collect-engtools.sh \
	        ${D}/${bindir}/cleanup-engtools.sh
}

FILES_collect-engtools = "  \
	${bindir}/buddyinfo.py \
	${bindir}/chewmem \
	${bindir}/ceph.sh \
	${bindir}/cleanup-engtools.sh \
	${bindir}/collect-engtools.sh \
	${bindir}/diskstats.sh \
	${bindir}/engtools_util.sh \
	${bindir}/filestats.sh \
	${bindir}/iostat.sh \
	${bindir}/linux_benchmark.sh \
	${bindir}/memstats.sh \
	${bindir}/netstats.sh \
	${bindir}/postgres.sh \
	${bindir}/rabbitmq.sh \
	${bindir}/rbzip2-engtools.sh \
	${bindir}/rstart-engtools.sh \
	${bindir}/rstop-engtools.sh \
	${bindir}/rsync-engtools-data.sh \
	${bindir}/slab.sh \
	${bindir}/ticker.sh \
	${bindir}/top.sh \
	${bindir}/vswitch.sh \
	${bindir}/live_stream.py \
	${sysconfdir}/engtools/engtools.conf \
	${sysconfdir}/init.d/collect-engtools.sh \
	${systemd_system_unitdir}/collect-engtools.service \
	"

SYSTEMD_PACKAGES += "collect-engtools"
SYSTEMD_SERVICE_collect-engtools = "collect-engtools.service"
SYSTEMD_AUTO_ENABLE_collect-engtools = "enable"
