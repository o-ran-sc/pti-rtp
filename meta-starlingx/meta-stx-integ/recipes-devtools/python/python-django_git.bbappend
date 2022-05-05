
do_install_append() {
    install -d ${D}/${sysconfdir}/bash_completion.d
    install -m 755 ${S}/extras/django_bash_completion ${D}/${sysconfdir}/bash_completion.d 
}

PACKAGES =+ "${PN}-bash-completion"

FILES_${PN}-bash-completion = "${sysconfdir}/bash_completion.d/*"
