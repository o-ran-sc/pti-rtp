inherit setuptools

require fault-common.inc
SUBPATH0 = "fm-rest-api/fm"

LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"


do_install_append() {
	install -d -m 755 ${D}/${systemd_system_unitdir}
	install -p -D -m 644 scripts/fm-api.service ${D}/${systemd_system_unitdir}
	install -p -D -m 755 scripts/fm-api ${D}/${sysconfdir}/init.d/fm-api
	install -p -D -m 644 fm-api-pmond.conf ${D}/${sysconfdir}/pmon.d/fm-api.conf
	
	# fix the path for init scripts
	sed -i -e 's|rc.d/||' ${D}/${systemd_system_unitdir}/*.service
}

inherit systemd
SYSTEMD_PACKAGES += "fm-rest-api"
SYSTEMD_SERVICE_${PN} = "fm-api.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"


# For fm.conf
RDEPENDS_${PN} += " python-oslo.config"

pkg_postinst_ontarget_${PN}() {

cat > /etc/fm/config-generator.conf << EOF
[DEFAULT]
output_file = fm.conf.sample
wrap_width = 79
namespace = fm.api.conf
namespace = keystonemiddleware.auth_token
namespace = oslo.middleware
namespace = oslo.log
namespace = oslo.policy
namespace = oslo.db
EOF

	oslo-config-generator --config-file /etc/fm/config-generator.conf --output-file /etc/fm/fm.conf.sample
	mv /etc/fm/fm.conf.sample /etc/fm/fm.conf
	rm /etc/fm/config-generator.conf
}

