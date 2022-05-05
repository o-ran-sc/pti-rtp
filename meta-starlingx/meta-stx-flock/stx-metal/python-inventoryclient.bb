require metal-common.inc

SUBPATH0 = "python-inventoryclient/inventoryclient"


LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

RDEPENDS_${PN}_append = " \
	python \
	python-pbr \
	python-six \
	python-oslo.i18n \
	python-oslo.utils \
	python-requests \
	bash \
	bash-completion \
	"


do_install_append () {
	
	install -d -m 755 ${D}/${sysconfdir}/bash_completion.d
	install -p -D -m 664 tools/inventory.bash_completion ${D}/${sysconfdir}/bash_completion.d

}

_FILES_${PN}_append = " \
	${bindir}/inventory \
	${sysconfdir}/bash_completion.d/inventory.bash_completion \
	${libdir}/python2.7/site-packages/inventoryclient-*.egg-info/ \
	${libdir}/python2.7/site-packages/inventoryclient/ \
	"
