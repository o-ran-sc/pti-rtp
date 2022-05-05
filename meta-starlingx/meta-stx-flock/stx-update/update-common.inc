
DESCRIPTION = "stx-update"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCNAME = "update"
SRCREV = "2542c5539bab060830009d02cbb257cc8bf4a376"
SRCREV_opendev = "2542c5539bab060830009d02cbb257cc8bf4a376"
DESTSUFFIX = "${PN}-${PV}"
PV = "1.0.0"


SRC_URI = " \
   git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0};name=opendev \
   "

DEPENDS = " \
	python \
	python-pbr-native \
	"
