
do_install_append () {
	cp ${B}/examples/24-nfs-server.conf \
		${B}/examples/99-nfs-client.conf \
		${B}/examples/gssproxy.conf ${D}/etc/gssproxy
}
