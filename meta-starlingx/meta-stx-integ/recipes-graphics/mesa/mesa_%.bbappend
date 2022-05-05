
inherit selinux
DEPENDS += " libselinux"
RDEPENDS_${PN}_append = " libselinux"
