
do_install_append() {
	install -d ${D}/${baselib}/security/
	ln -fs ${libdir}/security/pam_pwquality.so ${D}/${baselib}/security/
}

FILES_${PN} += "/${baselib}/security/"
INSANE_SKIP_${PN} += "dev-so"
