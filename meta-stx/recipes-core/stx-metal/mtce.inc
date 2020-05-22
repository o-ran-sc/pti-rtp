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

MAJOR = "${@d.getVar('PV').split('.')[0]}"
MINOR = "${@d.getVar('PV').split('.')[1]}"

PACKAGES += " mtce"
PACKAGES += " mtce-pmon"
PACKAGES += " mtce-hwmon"
PACKAGES += " mtce-hostw"
PACKAGES += " mtce-lmon"

RDEPENDS_mtce-pmon_append = " \
	bash \
	systemd \
	dpkg \
	fm-common \
	libevent \
	json-c \
	python-rtslib-fb \
	expect \
	util-linux \
	ipmitool \
	"

RDEPENDS_mtce_append = " \
	mtce-pmon \
	python-redfishtool \
	"

do_configure_prepend () {
	:
} 

do_compile_prepend () {
	# use sed to correct the path of systemctl
	sed -i -e 's|${bindir}/systemctl|${base_bindir}/systemctl|' ${S}/mtce/src/pmon/pmonHdlr.cpp
	cd ${S}/mtce/src/
	oe_runmake -e VER=1 VER_MJR=1 INCLUDES=" -I. -I../alarm -I../heartbeat -I../maintenance \
	                        -I../hostw -I../public -I../smash -I../common -I../hwmon \
				-I${S}/mtce-common/src/common \
				-I${S}/mtce-common/src/daemon " \
		CCFLAGS="${CXXFLAGS}" LDFLAGS="${LDFLAGS} -L${S}/mtce-common/src/common \
		-L${S}/mtce-common/src/daemon " build
}

do_install_prepend () {
	cd ${S}/mtce/src/
	install -m 755 -d ${D}/${bindir}
	install -m 755 -d ${D}/${sbindir}
	install -m 755 -d ${D}/${libdir}
	install -m 755 -d ${D}/${libdir}/ocf/resource.d/platform
	install -m 755 -d ${D}/${systemd_system_unitdir}
	
	install -m 755 -d ${D}/${sysconfdir}
	install -m 755 -d ${D}/${sysconfdir}/mtc/tmp
	install -m 755 -d ${D}/${sysconfdir}/bmc/server_profiles.d
	install -m 755 -d ${D}/${sysconfdir}/init.d
	install -m 755 -d ${D}/${sysconfdir}/pmon.d
	install -m 755 -d ${D}/${sysconfdir}/logrotate.d
	
	install -m 755 -d ${D}/${sysconfdir}/services.d
	install -m 750 -d ${D}/${sysconfdir}/services.d/controller
	install -m 750 -d ${D}/${sysconfdir}/services.d/worker
	install -m 750 -d ${D}/${sysconfdir}/services.d/storage
	
	cd ${S}/mtce/src/
	install -m 755 -p -D scripts/mtcAgent ${D}/${libdir}/ocf/resource.d/platform
	install -m 755 -p -D hwmon/scripts/ocf/hwmon ${D}/${libdir}/ocf/resource.d/platform
	
	# Config files
	install -m 644 -p -D scripts/mtc.ini ${D}/${sysconfdir}/mtc.ini
	install -m 644 -p -D scripts/mtc.conf ${D}/${sysconfdir}/mtc.conf
	install -m 644 -p -D fsmon/scripts/fsmond.conf ${D}/${sysconfdir}/mtc/fsmond.conf
	install -m 644 -p -D hwmon/scripts/hwmond.conf ${D}/${sysconfdir}/mtc/hwmond.conf
	install -m 644 -p -D pmon/scripts/pmond.conf ${D}/${sysconfdir}/mtc/pmond.conf
	install -m 644 -p -D lmon/scripts/lmond.conf ${D}/${sysconfdir}/mtc/lmond.conf
	install -m 644 -p -D hostw/scripts/hostwd.conf ${D}/${sysconfdir}/mtc/hostwd.conf
	
	install -m 644 -p -D scripts/sensor_hp360_v1_ilo_v4.profile ${D}/${sysconfdir}/bmc/server_profiles.d/
	install -m 644 -p -D scripts/sensor_hp380_v1_ilo_v4.profile ${D}/${sysconfdir}/bmc/server_profiles.d/
	install -m 644 -p -D scripts/sensor_quanta_v1_ilo_v4.profile ${D}/${sysconfdir}/bmc/server_profiles.d/
	
	
	# binaries
	install -m 755 -p -D maintenance/mtcAgent ${D}/${bindir}/mtcAgent
	install -m 755 -p -D maintenance/mtcClient ${D}/${bindir}/mtcClient
	install -m 755 -p -D heartbeat/hbsAgent ${D}/${bindir}/hbsAgent
	install -m 755 -p -D heartbeat/hbsClient ${D}/${bindir}/hbsClient
	install -m 755 -p -D pmon/pmond ${D}/${bindir}/pmond
	install -m 755 -p -D lmon/lmond ${D}/${bindir}/lmond
	install -m 755 -p -D pmon/pmond ${D}/${bindir}/pmond
	install -m 755 -p -D lmon/lmond ${D}/${bindir}/lmond
	install -m 755 -p -D hostw/hostwd ${D}/${bindir}/hostwd
	install -m 755 -p -D fsmon/fsmond ${D}/${bindir}/fsmond
	install -m 755 -p -D hwmon/hwmond ${D}/${bindir}/hwmond
	install -m 755 -p -D mtclog/mtclogd ${D}/${bindir}/mtclogd
	install -m 755 -p -D alarm/mtcalarmd ${D}/${bindir}/mtcalarmd
	install -m 755 -p -D scripts/wipedisk ${D}/${bindir}/wipedisk
	install -m 755 -p -D fsync/fsync ${D}/${sbindir}/fsync
	install -m 700 -p -D pmon/scripts/pmon-restart ${D}/${sbindir}/pmon-restart
	install -m 700 -p -D pmon/scripts/pmon-start ${D}/${sbindir}/pmon-start
	install -m 700 -p -D pmon/scripts/pmon-stop ${D}/${sbindir}/pmon-stop
	
	# init script files
	install -m 755 -p -D scripts/mtcClient ${D}/${sysconfdir}/init.d/mtcClient
	install -m 755 -p -D scripts/hbsClient ${D}/${sysconfdir}/init.d/hbsClient
	install -m 755 -p -D hwmon/scripts/lsb/hwmon ${D}/${sysconfdir}/init.d/hwmon
	install -m 755 -p -D fsmon/scripts/fsmon ${D}/${sysconfdir}/init.d/fsmon
	install -m 755 -p -D scripts/mtclog ${D}/${sysconfdir}/init.d/mtclog
	install -m 755 -p -D pmon/scripts/pmon ${D}/${sysconfdir}/init.d/pmon
	install -m 755 -p -D lmon/scripts/lmon ${D}/${sysconfdir}/init.d/lmon
	install -m 755 -p -D hostw/scripts/hostw ${D}/${sysconfdir}/init.d/hostw
	install -m 755 -p -D alarm/scripts/mtcalarm.init ${D}/${sysconfdir}/init.d/mtcalarm
	# install -m 755 -p -D scripts/config ${D}/${sysconfdir}/init.d/config
	
	# TODO: Init hack. Should move to proper module
	install -m 755 -p -D scripts/hwclock.sh ${D}/${sysconfdir}/init.d/hwclock.sh
	install -m 644 -p -D scripts/hwclock.service ${D}/${systemd_system_unitdir}/hwclock.service
	
	
	# systemd service files
	install -m 644 -p -D fsmon/scripts/fsmon.service ${D}/${systemd_system_unitdir}/fsmon.service
	install -m 644 -p -D hwmon/scripts/hwmon.service ${D}/${systemd_system_unitdir}/hwmon.service
	install -m 644 -p -D pmon/scripts/pmon.service ${D}/${systemd_system_unitdir}/pmon.service
	install -m 644 -p -D hostw/scripts/hostw.service ${D}/${systemd_system_unitdir}/hostw.service
	install -m 644 -p -D scripts/mtcClient.service ${D}/${systemd_system_unitdir}/mtcClient.service
	install -m 644 -p -D scripts/hbsClient.service ${D}/${systemd_system_unitdir}/hbsClient.service
	install -m 644 -p -D scripts/mtclog.service ${D}/${systemd_system_unitdir}/mtclog.service
	install -m 644 -p -D scripts/hbsClient.service ${D}/${systemd_system_unitdir}/hbsClient.service
	install -m 644 -p -D scripts/mtclog.service ${D}/${systemd_system_unitdir}/mtclog.service
	install -m 644 -p -D scripts/goenabled.service ${D}/${systemd_system_unitdir}/goenabled.service
	install -m 644 -p -D scripts/runservices.service ${D}/${systemd_system_unitdir}/runservices.service
	install -m 644 -p -D alarm/scripts/mtcalarm.service ${D}/${systemd_system_unitdir}/mtcalarm.service
	install -m 644 -p -D lmon/scripts/lmon.service ${D}/${systemd_system_unitdir}/lmon.service
	
	# fix the path for init scripts
	sed -i -e 's|rc.d/||' ${D}/${systemd_system_unitdir}/*.service
	
	# go enabled stuff
	install -m 755 -p -D scripts/goenabled ${D}/${sysconfdir}/init.d/goenabled
	
	# start or stop services test script
	install -m 755 -p -D scripts/mtcTest ${D}/${sysconfdir}/services.d/worker
	install -m 755 -p -D scripts/mtcTest ${D}/${sysconfdir}/services.d/controller
	install -m 755 -p -D scripts/mtcTest ${D}/${sysconfdir}/services.d/storage
	install -m 755 -p -D scripts/runservices ${D}/${sysconfdir}/init.d/runservices
	
	
	# test tools
	install -m 755 -p -D scripts/dmemchk.sh ${D}/${sbindir}
	
	# process monitor config files
	install -m 644 -p -D scripts/mtcClient.conf ${D}/${sysconfdir}/pmon.d/mtcClient.conf
	install -m 644 -p -D scripts/hbsClient.conf ${D}/${sysconfdir}/pmon.d/hbsClient.conf
	install -m 644 -p -D pmon/scripts/acpid.conf ${D}/${sysconfdir}/pmon.d/acpid.conf
	install -m 644 -p -D pmon/scripts/sshd.conf ${D}/${sysconfdir}/pmon.d/sshd.conf
	install -m 644 -p -D pmon/scripts/syslog-ng.conf ${D}/${sysconfdir}/pmon.d/syslog-ng.conf
	install -m 644 -p -D pmon/scripts/nslcd.conf ${D}/${sysconfdir}/pmon.d/nslcd.conf
	install -m 644 -p -D pmon/scripts/syslog-ng.conf ${D}/${sysconfdir}/pmon.d/syslog-ng.conf
	install -m 644 -p -D pmon/scripts/nslcd.conf ${D}/${sysconfdir}/pmon.d/nslcd.conf
	install -m 644 -p -D fsmon/scripts/fsmon.conf ${D}/${sysconfdir}/pmon.d/fsmon.conf
	install -m 644 -p -D scripts/mtclogd.conf ${D}/${sysconfdir}/pmon.d/mtclogd.conf
	install -m 644 -p -D alarm/scripts/mtcalarm.pmon.conf ${D}/${sysconfdir}/pmon.d/mtcalarm.conf
	install -m 644 -p -D lmon/scripts/lmon.pmon.conf ${D}/${sysconfdir}/pmon.d/lmon.conf
	
	# log rotation
	install -m 644 -p -D scripts/mtce.logrotate ${D}/${sysconfdir}/logrotate.d/mtce.logrotate
	install -m 644 -p -D hostw/scripts/hostw.logrotate ${D}/${sysconfdir}/logrotate.d/hostw.logrotate
	install -m 644 -p -D pmon/scripts/pmon.logrotate ${D}/${sysconfdir}/logrotate.d/pmon.logrotate
	install -m 644 -p -D lmon/scripts/lmon.logrotate ${D}/${sysconfdir}/logrotate.d/lmon.logrotate
	install -m 644 -p -D fsmon/scripts/fsmon.logrotate ${D}/${sysconfdir}/logrotate.d/fsmon.logrotate
	install -m 644 -p -D hwmon/scripts/hwmon.logrotate ${D}/${sysconfdir}/logrotate.d/hwmon.logrotate
	install -m 644 -p -D alarm/scripts/mtcalarm.logrotate ${D}/${sysconfdir}/logrotate.d/mtcalarm.logrotate
	
	# software development files
	install -m 644 -p -D heartbeat/mtceHbsCluster.h ${D}/${includedir}/mtceHbsCluster.h
	install -m 755 -p -D public/libamon.so.${MAJOR} ${D}/${libdir}/
	cd ${D}/${libdir} ; ln -s libamon.so.${MAJOR} libamon.so.${MAJOR}.${MINOR}
	cd ${D}/${libdir} ; ln -s libamon.so.${MAJOR} libamon.so
}

SYSTEMD_PACKAGES += " \
	mtce \
	mtce-hostw \
	mtce-lmon \
	mtce-pmon \
	"

SYSTEMD_SERVICE_mtce = " \
	fsmon.service \
	goenabled.service \
	hbsClient.service \
	mtcClient.service \
	mtcalarm.service \
	mtclog.service \
	"
SYSTEMD_SERVICE_mtce-hostw = "hostw.service"
SYSTEMD_SERVICE_mtce-lmon = "lmon.service"
SYSTEMD_SERVICE_mtce-pmon = "pmon.service"

SYSTEMD_AUTO_ENABLE_mtce = "enable"
SYSTEMD_AUTO_ENABLE_mtce-hostw = "enable"
SYSTEMD_AUTO_ENABLE_mtce-lmon = "enable"
SYSTEMD_AUTO_ENABLE_mtce-pmon = "enable"

FILES_mtce-pmon = " \
	${sbindir}/pmon-restart \
	${sbindir}/pmon-start \
	${sbindir}/pmon-stop \
	${bindir}/pmond \
	${systemd_system_unitdir}/pmon.service \
	${sysconfdir}/mtc/pmond.conf \
	${sysconfdir}/init.d/pmon \
	${sysconfdir}/logrotate.d/pmon.logrotate \
	"

FILES_mtce-hwmon = " \
	${bindir}/hwmond \
	${sysconfdir}/init.d/hwmon \
	${libdir}/ocf/resource.d/platform/hwmon \
	${sysconfdir}/logrotate.d/hwmon.logrotate \
	${systemd_system_unitdir}/hwmon.service \
	${sysconfdir}/mtc/hwmond.conf \
	"

FILES_mtce-hostw = " \
	${sysconfdir}/mtc/hostwd.conf \
	${sysconfdir}/logrotate.d/hostw.logrotate \
	${systemd_system_unitdir}/hostw.service \
	${sysconfdir}/init.d/hostw \
	${bindir}/hostwd \
	"

FILES_mtce-lmon= " \
	${bindir}/lmond \
	${sysconfdir}/mtc/lmond.conf \
	${sysconfdir}/logrotate.d/lmon.logrotate \
	${sysconfdir}/pmon.d/lmon.conf \
	${sysconfdir}/init.d/lmon \
	${systemd_system_unitdir}/lmon.service \
	"

FILES_mtce = " \
	${bindir}/mtcAgent \
	${bindir}/mtcClient \
	${bindir}/fsmond \
	${bindir}/hbsAgent \
	${bindir}/wipedisk \
	${bindir}/hbsClient \
	${bindir}/mtcalarmd \
	${bindir}/mtclogd \
	${sbindir}/fsync \
	${sbindir}/dmemchk.sh \
	${libdir}/ocf/resource.d/platform/mtcAgent \
	${libdir}/libamon${SOLIBS} \
	${systemd_system_unitdir}/mtcalarm.service \
	${systemd_system_unitdir}/goenabled.service \
	${systemd_system_unitdir}/mtclog.service \
	${systemd_system_unitdir}/mtcClient.service \
	${systemd_system_unitdir}/fsmon.service \
	${systemd_system_unitdir}/hbsClient.service \
	${systemd_system_unitdir}/hwclock.service \
	${systemd_system_unitdir}/runservices.service \
	${sysconfdir}/pmon.d/nslcd.conf \
	${sysconfdir}/pmon.d/mtclogd.conf \
	${sysconfdir}/pmon.d/mtcalarm.conf \
	${sysconfdir}/pmon.d/syslog-ng.conf \
	${sysconfdir}/pmon.d/acpid.conf \
	${sysconfdir}/pmon.d/sshd.conf \
	${sysconfdir}/pmon.d/fsmon.conf \
	${sysconfdir}/pmon.d/hbsClient.conf \
	${sysconfdir}/pmon.d/mtcClient.conf \
	${sysconfdir}/init.d/runservices \
	${sysconfdir}/init.d/goenabled \
	${sysconfdir}/init.d/mtcClient \
	${sysconfdir}/init.d/hwclock.sh \
	${sysconfdir}/init.d/mtclog \
	${sysconfdir}/init.d/mtcalarm \
	${sysconfdir}/init.d/hbsClient \
	${sysconfdir}/init.d/fsmon \
	${sysconfdir}/mtc.conf \
	${sysconfdir}/bmc/server_profiles.d/sensor_hp380_v1_ilo_v4.profile \
	${sysconfdir}/bmc/server_profiles.d/sensor_hp360_v1_ilo_v4.profile \
	${sysconfdir}/bmc/server_profiles.d/sensor_quanta_v1_ilo_v4.profile \
	${sysconfdir}/services.d/worker/mtcTest \
	${sysconfdir}/services.d/controller/mtcTest \
	${sysconfdir}/services.d/storage/mtcTest \
	${sysconfdir}/mtc/fsmond.conf \
	${sysconfdir}/mtc/tmp/ \
	${sysconfdir}/mtc.ini \
	${sysconfdir}/logrotate.d/fsmon.logrotate \
	${sysconfdir}/logrotate.d/mtcalarm.logrotate \
	${sysconfdir}/logrotate.d/mtce.logrotate \
	"
