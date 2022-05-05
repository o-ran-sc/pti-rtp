DESCRIPTION = "stx-fault"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV_opendev = "2025f585c5b92890c8cb32c480b0151c7c1cb545"
DESTSUFFIX = "${PN}-${PV}"
SRCREV = "2025f585c5b92890c8cb32c480b0151c7c1cb545"
PV = "1.0.0"

LICENSE = "Apache-2.0"

DEPENDS = " \
        util-linux \
        postgresql \
        python \
        python-pbr-native \
        python-six \
        python-oslo.i18n \
        python-oslo.utils \
        python-requests \
        bash \
        net-snmp \
"

RDEPENDS_${PN} += " bash"


SRC_URI = " \
   git://opendev.org/starlingx/fault.git;protocol=${PROTOCOL};rev=${SRCREV};destsuffix=${DESTSUFFIX};branch=${BRANCH};subpath=${SUBPATH0};name=opendev \
   "
