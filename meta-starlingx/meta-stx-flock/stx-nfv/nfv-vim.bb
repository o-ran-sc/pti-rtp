require nfv-common.inc

SUBPATH0 = "nfv/nfv-vim"

inherit setuptools

do_configure_prepend() {
       sed -i -e 's|@SYSCONFDIR@|${sysconfdir}|g' \
               scripts/vim \
               scripts/vim-api \
               scripts/vim-webserver \
               nfv_vim/config.ini
       sed -i -e 's|@PYTHONROOT@|${libdir}/python2.7/site-packages|g' nfv_vim/config.ini
}

do_install_append() {
	install -d -m 755 ${D}/usr/lib/ocf/resource.d/nfv
	install -p -D -m 755 scripts/vim ${D}/usr/lib/ocf/resource.d/nfv/vim
	install -p -D -m 755 scripts/vim-api ${D}/usr/lib/ocf/resource.d/nfv/vim-api
	install -p -D -m 755 scripts/vim-webserver ${D}/usr/lib/ocf/resource.d/nfv/vim-webserver
	install -d -m 755 ${D}/${sysconfdir}/nfv/
	install -d -m 755 ${D}/${sysconfdir}/nfv/vim/
	install -p -D -m 600 nfv_vim/config.ini ${D}/${sysconfdir}/nfv/vim/config.ini
	install -p -D -m 600 nfv_vim/debug.ini ${D}/${sysconfdir}/nfv/vim/debug.ini

}

FILES_${PN}_append = " \
	${libdir}/ocf/resource.d/nfv/vim \
	${libdir}/ocf/resource.d/nfv/vim-api \
	${libdir}/ocf/resource.d/nfv/vim-webserver \
	"
