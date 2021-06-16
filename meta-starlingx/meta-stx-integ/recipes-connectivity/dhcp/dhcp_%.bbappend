inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "dhcp-config"


do_install_append () { 
	install -m 0755 ${STX_METADATA_PATH}/files/dhclient-enter-hooks ${D}/${sysconfdir}/dhcp/dhclient-enter-hooks
	install -m 0755 ${STX_METADATA_PATH}/files/dhclient.conf ${D}/${sysconfdir}/dhcp/dhclient.conf
	ln -rs ${D}/${sysconfdir}/dhcp/dhclient-enter-hooks ${D}/${sysconfdir}/dhclient-enter-hooks
}

FILES_${PN}-client_append = " \
	/etc/dhclient-enter-hooks \
	/etc/dhcp/dhclient-enter-hooks \
	"

RDEPENDS_${PN}-client_append = " bash"
