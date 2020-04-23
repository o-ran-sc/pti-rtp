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

PACKAGES += " mtce-common"



RDEPENDS_mtce-common = " \
	util-linux \
	bash \
	systemd \
	dpkg \
	time \
	libevent \
	expect \
	json-c \
	python-rtslib-fb \
	fm-common \
	"

do_configure_prepend () {
	:
} 

do_compile_prepend () {
	cd ${S}/mtce-common/src/
	oe_runmake clean
	oe_runmake -e build VER=0 VER_MJR=1 \
		CCFLAGS="${CXXFLAGS} -DBUILDINFO=\"\\\"\$\$(date)\\\"\"" 
}

do_install_prepend () {

	cd ${S}/mtce-common/src

	install -m 755 -d ${D}/${libdir}

	install -m 644 -p -D daemon/libdaemon.a ${D}/${libdir}
	install -m 644 -p -D common/libcommon.a ${D}/${libdir}
	install -m 644 -p -D common/libthreadUtil.a ${D}/${libdir}
	install -m 644 -p -D common/libbmcUtils.a ${D}/${libdir}
	install -m 644 -p -D common/libpingUtil.a ${D}/${libdir}
	install -m 644 -p -D common/libnodeBase.a ${D}/${libdir}
	install -m 644 -p -D common/libregexUtil.a ${D}/${libdir}
	install -m 644 -p -D common/libhostUtil.a ${D}/${libdir}

	# mtce-common headers required to bring in nodeBase.h
	install -m 755 -d ${D}/${includedir}
	install -m 755 -d ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/fitCodes.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/logMacros.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/returnCodes.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/nodeTimers.h ${D}/${includedir}/mtce-common

	# mtce-common headers required to build mtce-guest
	install -m 644 -p -D common/hostClass.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/httpUtil.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/jsonUtil.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/msgClass.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/nodeBase.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/nodeEvent.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/nodeMacro.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/nodeUtil.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/timeUtil.h ${D}/${includedir}/mtce-common

	# mtce-daemon headers required to build mtce-guest
	install -m 755 -d ${D}/${includedir}/mtce-daemon
	install -m 644 -p -D daemon/daemon_ini.h ${D}/${includedir}/mtce-daemon
	install -m 644 -p -D daemon/daemon_common.h ${D}/${includedir}/mtce-daemon
	install -m 644 -p -D daemon/daemon_option.h ${D}/${includedir}/mtce-daemon

	# remaining mtce-common headers required to build mtce
	install -m 644 -p -D common/alarmUtil.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/hostUtil.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/ipmiUtil.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/redfishUtil.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/bmcUtil.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/nlEvent.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/pingUtil.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/regexUtil.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/threadUtil.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/tokenUtil.h ${D}/${includedir}/mtce-common
	install -m 644 -p -D common/secretUtil.h ${D}/${includedir}/mtce-common
}

# Headers, and static devs go into stx-mtce-dev 
# and stx-mtce-staticdev packages respecitively
FILES_mtce-common = " "
