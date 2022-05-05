
do_install_append() {
	mv ${D}/${bindir}/mako-render ${D}/${bindir}/mako3-render 
}
