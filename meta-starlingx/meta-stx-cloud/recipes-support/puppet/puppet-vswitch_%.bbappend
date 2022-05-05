
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
do_install_append() {
	install -d -m 0755 ${D}/${datadir}/puppet/modules/vswitch
	tar -C ${S} -cf - --exclude "patches" --exclude "*.gem*" . | tar --no-same-owner -xf - -C ${D}/${datadir}/puppet/modules/vswitch
}

FILES_${PN} += " ${datadir}"

inherit openssl10
RDEPENDS_${PN}_append = " perl"
