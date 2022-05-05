do_install_append() {
	rm -f ${D}/etc/network/interfaces
	touch ${D}/etc/network/interfaces
	cat >  ${D}/etc/network/interfaces << EOF
auto lo
iface lo inet loopback
EOF
}

RDEPENDS_${PN} += "ifupdown"
