DESCRIPTION = "stx-metal"

STABLE = "starlingx/master"
PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCNAME = "metal"
SRCREV = "be3cf4eeb50eef55910cf9c73ea47c168005ad64"
SRCREV_opendev = "be3cf4eeb50eef55910cf9c73ea47c168005ad64"
DESTSUFFIX = "${PN}-${PV}"
PV = "1.0.0"

SRC_URI = " \
	git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0};name=opendev \
	"

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"
