inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "config/puppet-modules/openstack/${BP}/centos/patches"

SRC_URI_STX += " \
	file://0001-Remove-log_dir-from-conf-files.patch \
	file://0002-add-psycopg2-drivername-to-postgresql-settings.patch \
	"

do_install_append () {
	# fix the name of python-memcached
	sed -i -e 's/python-memcache\b/python-memcached/' ${D}/${datadir}/puppet/modules/oslo/manifests/params.pp
}

RDEPENDS_${PN} += "python-memcached"

inherit openssl10
