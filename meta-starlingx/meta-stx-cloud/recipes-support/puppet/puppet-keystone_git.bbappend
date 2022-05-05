
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://${PN}/0001-pike-rebase-squash-titanium-patches.patch \
	file://${PN}/0002-remove-the-Keystone-admin-app.patch \
	file://${PN}/0003-remove-eventlet_bindhost-from-Keystoneconf.patch \
	file://${PN}/0004-escape-special-characters-in-bootstrap.patch \
	file://${PN}/0005-Add-support-for-fernet-receipts.patch \
	file://${PN}/0007-puppet-keystone-specify-full-path-to-openrc.patch \
	file://${PN}/0008-params.pp-fix-the-service-name-of-openstack-keystone.patch \
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
