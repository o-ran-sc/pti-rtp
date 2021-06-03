inherit stx-metadata

STX_REPO = "upstream"
STX_SUBPATH = "openstack/python-horizon/centos/files"

SRC_URI_STX += " \
	file://0001-Remove-the-hard-coded-internal-URL-for-keystone.patch \
	"

do_configure_prepend () {
	cd ${S}

	# STX remove troublesome files introduced by tox
	rm -f openstack_dashboard/test/.secret_key_store
	rm -f openstack_dashboard/test/*.secret_key_store.lock
	rm -f openstack_dashboard/local/.secret_key_store
	rm -f openstack_dashboard/local/*.secret_key_store.lock
	rm -rf horizon.egg-info

	# drop config snippet
	cp -p ${STX_METADATA_PATH}/openstack-dashboard-httpd-logging.conf .
	cp -p ${STX_METADATA_PATH}/guni_config.py .

	# customize default settings
	# WAS [PATCH] disable debug, move web root
	sed -i "/^DEBUG =.*/c\DEBUG = False" openstack_dashboard/local/local_settings.py.example
	sed -i "/^WEBROOT =.*/c\WEBROOT = '/dashboard/'" openstack_dashboard/local/local_settings.py.example
	sed -i "/^.*ALLOWED_HOSTS =.*/c\ALLOWED_HOSTS = ['horizon.example.com', 'localhost']" openstack_dashboard/local/local_settings.py.example
	sed -i "/^.*LOCAL_PATH =.*/c\LOCAL_PATH = '/tmp'" openstack_dashboard/local/local_settings.py.example
	sed -i "/^.*POLICY_FILES_PATH =.*/c\POLICY_FILES_PATH = '/etc/openstack-dashboard'" openstack_dashboard/local/local_settings.py.example

	sed -i "/^BIN_DIR = .*/c\BIN_DIR = '/usr/bin'" openstack_dashboard/settings.py
	sed -i "/^COMPRESS_PARSER = .*/a COMPRESS_OFFLINE = True" openstack_dashboard/settings.py

	# set COMPRESS_OFFLINE=True
	sed -i 's:COMPRESS_OFFLINE.=.False:COMPRESS_OFFLINE = True:' openstack_dashboard/settings.py

	# STX: MANIFEST needs .eslintrc files for angular
	echo "include .eslintrc"   >> MANIFEST.in

	# MANIFEST needs to include json and pot files under openstack_dashboard
	echo "recursive-include openstack_dashboard *.json *.pot .eslintrc"   >> MANIFEST.in

	# MANIFEST needs to include pot files  under horizon
	echo "recursive-include horizon *.pot .eslintrc"   >> MANIFEST.in
}

do_install_append () {
	cd ${S}

	# STX
	install -d -m 755 ${D}/opt/branding
	mkdir -p ${D}${sysconfdir}/rc.d/init.d
	install -m 755 -D -p ${STX_METADATA_PATH}/horizon.init ${D}${sysconfdir}/rc.d/init.d/horizon
	install -m 755 -D -p ${STX_METADATA_PATH}/horizon.init ${D}${sysconfdir}/init.d/horizon
	install -m 755 -D -p ${STX_METADATA_PATH}/horizon-clearsessions ${D}/${bindir}/horizon-clearsessions
	install -m 755 -D -p ${STX_METADATA_PATH}/horizon-patching-restart ${D}/${bindir}/horizon-patching-restart
	install -m 755 -D -p ${STX_METADATA_PATH}/horizon-assets-compress ${D}/${bindir}/horizon-assets-compress

	# drop httpd-conf snippet
	install -m 0644 -D -p ${STX_METADATA_PATH}/openstack-dashboard-httpd-2.4.conf ${D}${sysconfdir}/httpd/conf.d/openstack-dashboard.conf
	install -d -m 755 ${D}${datadir}/openstack-dashboard
	install -d -m 755 ${D}${sysconfdir}/openstack-dashboard

	# create directory for systemd snippet
	mkdir -p ${D}${systemd_system_unitdir}/httpd.service.d/
	cp ${STX_METADATA_PATH}/python-django-horizon-systemd.conf ${D}${systemd_system_unitdir}/httpd.service.d/openstack-dashboard.conf

	# Copy everything to /usr/share
	mv ${D}${libdir}/python2.7/site-packages/openstack_dashboard \
	   ${D}${datadir}/openstack-dashboard
	cp manage.py ${D}${datadir}/openstack-dashboard

	# STX
	cp guni_config.py ${D}${datadir}/openstack-dashboard
	rm -rf ${D}${libdir}/python2.7/site-packages/openstack_dashboard

	# remove unnecessary .po files
	find ${D} -name django.po -exec rm '{}' \;
	find ${D} -name djangojs.po -exec rm '{}' \;

	# Move config to /etc, symlink it back to /usr/share
	mv ${D}${datadir}/openstack-dashboard/openstack_dashboard/local/local_settings.py.example ${D}${sysconfdir}/openstack-dashboard/local_settings

	mv ${D}${datadir}/openstack-dashboard/openstack_dashboard/conf/*.json ${D}${sysconfdir}/openstack-dashboard
	cp -a  ${S}/openstack_dashboard/conf/nova_policy.d ${D}${sysconfdir}/openstack-dashboard

	# copy static files to ${datadir}/openstack-dashboard/static
	mkdir -p ${D}${datadir}/openstack-dashboard/static
	cp -a openstack_dashboard/static/* ${D}${datadir}/openstack-dashboard/static
	cp -a horizon/static/* ${D}${datadir}/openstack-dashboard/static

	# create /var/run/openstack-dashboard/ and /var/log/horizon
	install -m 0755 -d ${D}/${sysconfdir}/tmpfiles.d
	echo "d ${localstatedir}/run/openstack-dashboard 0755 root root -" >> ${D}/${sysconfdir}/tmpfiles.d/openstack-dashboard.conf
	echo "d ${localstatedir}/log/horizon 0755 root root -" >> ${D}/${sysconfdir}/tmpfiles.d/openstack-dashboard.conf

	# place logrotate config:
	mkdir -p ${D}${sysconfdir}/logrotate.d
	cp -a ${STX_METADATA_PATH}/python-django-horizon-logrotate.conf ${D}${sysconfdir}/logrotate.d/openstack-dashboard

	chown -R root:root ${D}
}

FILES_${PN} += "\
	${datadir}/openstack-dashboard \
	${systemd_system_unitdir} \
	${localstatedir} \
	/opt \
	"

RPROVIDES_${PN} = "openstack-dashboard"
