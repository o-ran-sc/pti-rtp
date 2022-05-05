
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://puppet-oslo/0001-Remove-log_dir-from-conf-files.patch \
	file://puppet-oslo/0002-add-psycopg2-drivername-to-postgresql-settings.patch \
	"

do_install_append () {
	# fix the name of python-memcached
	sed -i -e 's/python-memcache\b/python-memcached/' ${D}/${datadir}/puppet/modules/oslo/manifests/params.pp
}

RDEPENDS_${PN} += "python-memcached"

inherit openssl10
