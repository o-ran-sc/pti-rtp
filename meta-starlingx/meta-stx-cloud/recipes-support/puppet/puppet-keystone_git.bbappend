inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "config/puppet-modules/openstack/${BP}/centos"

SRC_URI_STX += " \
	file://patches/0001-pike-rebase-squash-titanium-patches.patch \
	file://patches/0002-remove-the-Keystone-admin-app.patch \
	file://patches/0003-remove-eventlet_bindhost-from-Keystoneconf.patch \
	file://patches/0004-escape-special-characters-in-bootstrap.patch \
	file://patches/0005-Add-support-for-fernet-receipts.patch \
	file://patches/0006-update-Barbican-admin-secret-s-user-project-IDs-duri.patch \
	file://patches/0007-update-for-openstackclient-Train-upgrade.patch \
	"

SRC_URI += " \
	file://${BPN}/puppet-keystone-specify-full-path-to-openrc.patch \
	file://${BPN}/puppet-keystone-params.pp-fix-the-service-name.patch \
	"

do_install_append () {
	# fix the name of python-memcached
	sed -i -e 's/python-memcache\b/python-memcached/' ${D}/${datadir}/puppet/modules/keystone/manifests/params.pp
}

RDEPENDS_${PN} += " \
	python-memcached \
	python-ldappool \
	"

inherit openssl10
