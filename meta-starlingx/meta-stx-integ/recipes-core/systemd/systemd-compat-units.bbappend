SYSTEMD_DISABLED_SYSV_SERVICES_remove += " networking"

pkg_postinst_${PN}_append () {

	if [ -n "$D" ]; then
		OPT="-f -r $D"
	else
		OPT="-f"
	fi

	if [ -f "$D${sysconfdir}/init.d/networking" ]; then
		update-rc.d $OPT networking defaults
	fi
}

RDEPENDS_${PN} += "update-rc.d"
