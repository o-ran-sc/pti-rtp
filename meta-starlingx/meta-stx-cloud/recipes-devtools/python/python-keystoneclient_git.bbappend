
SRC_URI = "\
	git://github.com/openstack/python-keystoneclient.git;branch=stable/train \
	"

PV = "3.21.0+git${SRCPV}"
SRCREV = "79f150f962a2300f4644ba735b4f28e337035251"
DEPENDS += " \
        python-pip \
        python-pbr \
        "

RDEPENDS_${PN}_append = " \
	python-keystone \
	keystone-setup \
	keystone-cronjobs \
	keystone \
	"
