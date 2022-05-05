require metal-common.inc

SUBPATH0 = "mtce-common/src/"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI += " \
	file://mtce-common-do-not-use-which-to-check-CC.patch \
	"

RDEPENDS_${PN}_append = " \
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

DEPENDS_append = " \
	fm-common \
	libevent \
	openssl \
	json-c \
	"

EXTRA_OEMAKE = ' -e build VER=0 VER_MJR=1 CCFLAGS="${CXXFLAGS} -DBUILDINFO=\"\\\"\$\$(date)\\\"\"" '

do_install_append() {

	cd ${S}/

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
