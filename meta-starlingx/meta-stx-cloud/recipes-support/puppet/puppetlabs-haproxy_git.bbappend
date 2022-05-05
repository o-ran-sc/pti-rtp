
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://puppetlabs-haproxy/0001-Roll-up-TIS-patches.patch \
	file://puppetlabs-haproxy/0002-disable-config-validation-prechecks.patch \
	file://puppetlabs-haproxy/0003-Fix-global_options-log-default-value.patch \
	file://puppetlabs-haproxy/0004-Stop-invalid-warning-message \
	"

inherit openssl10
