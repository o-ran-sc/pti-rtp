
DESCRIPTION = "stx-utilities"

PROTOCOL = "https"
SRCNAME = "utilities"
BRANCH = "r/stx.3.0"
SRCREV = "cbad6b792157d066dd601f0f9ce62dc177d4c848"
SRCREV_opendev = "cbad6b792157d066dd601f0f9ce62dc177d4c848"
DESTSUFFIX = "${PN}-${PV}"
PV = "1.0.0"

SRC_URI = " \
   git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=${DESTSUFFIX};subpath=${SUBPATH0};name=opendev \
   "

#git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

DEPENDS = " \
	python-pip \
	python-pbr-native \
	systemd \
"

## Skip tpm2-openssl-engine2
