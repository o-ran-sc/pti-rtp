FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "dhcp-config"
DSTSUFX0 = "stx-configfiles"

SRC_URI += "\
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
"

do_install_append () { 
	install -m 0755 ${WORKDIR}/${DSTSUFX0}/files/dhclient-enter-hooks ${D}/${sysconfdir}/dhcp/dhclient-enter-hooks
	install -m 0755 ${WORKDIR}/${DSTSUFX0}/files/dhclient.conf ${D}/${sysconfdir}/dhcp/dhclient.conf
	ln -rs ${D}/${sysconfdir}/dhcp/dhclient-enter-hooks ${D}/${sysconfdir}/dhclient-enter-hooks
}

FILES_${PN}-client_append = " \
	/etc/dhclient-enter-hooks \
	/etc/dhcp/dhclient-enter-hooks \
	"

RDEPENDS_${PN}-client_append = " bash"
