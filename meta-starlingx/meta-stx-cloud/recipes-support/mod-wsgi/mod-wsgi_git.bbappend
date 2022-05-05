
do_install_append() {
	sed -i -e 's|${libdir}|${libexecdir}|' ${D}/etc/apache2/modules.d/wsgi.load
}
