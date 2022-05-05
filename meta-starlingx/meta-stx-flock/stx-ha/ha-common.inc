DESCRIPTION = "stx-ha"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCNAME = "ha"
SRCREV = "a7b7d35b9922a3f2a8462492b7f1958f135a612d"
SRCREV_opendev = "a7b7d35b9922a3f2a8462492b7f1958f135a612d"
DESTSUFFIX = "${PN}-${PV}"
PV = "1.0.0"

SRC_URI = " \
	git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0};name=opendev \
	"

inherit systemd

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

DEPENDS += " \
	fm-common \
	sqlite3 \
	python \
	python-pbr-native \
	"
