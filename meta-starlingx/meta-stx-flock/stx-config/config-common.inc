DESCRIPTION = "stx-config"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV = "b51e4ef738e0020f11f164fd3f86399872caf3c6"
SRCREV_opendev = "b51e4ef738e0020f11f164fd3f86399872caf3c6"
DESTSUFFIX = "${PN}-${PV}"
PV = "1.0.0"

SRC_URI = "\
	git://opendev.org/starlingx/config.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0};name=opendev \
	"

DEPENDS = "\
	puppet \
	python \
	python-pbr-native \
	"

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"
