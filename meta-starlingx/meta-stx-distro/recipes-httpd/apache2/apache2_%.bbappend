

APACHE_PORT_NUM ?= "81"

do_install_append () {
	sed -i -e 's/80/${APACHE_PORT_NUM}/' ${D}/${sysconfdir}/${BPN}/httpd.conf
}

inherit useradd

USERADD_PACKAGES = "${PN}"

USERADD_PARAM_${PN} = "-c 'Apache' -u 48 -g apache -s /sbin/nologin -r -d /usr/share/httpd apache"
GROUPADD_PARAM_${PN} = "-g 48 -r apache"

SYSTEMD_AUTO_ENABLE_${PN} = "disable"
