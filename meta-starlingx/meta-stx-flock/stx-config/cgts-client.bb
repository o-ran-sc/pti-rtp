require config-common.inc

SUBPATH0 = "sysinv/cgts-client/cgts-client"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

DEPENDS += " \
	python-pbr-native \
	"

RDEPENDS_${PN}_append = " \
	python-prettytable \
	bash-completion \
	python-neutronclient \
	python-keystoneclient \
	python-six \
	python-httplib2 \
	"

inherit setuptools

do_install_append() {
       install -d -m 755 ${D}/${sysconfdir}/bash_completion.d
       install -p -D -m 664 tools/system.bash_completion ${D}/${sysconfdir}/bash_completion.d/system.bash_completion
}
