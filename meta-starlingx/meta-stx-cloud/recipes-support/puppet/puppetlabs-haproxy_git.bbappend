inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "config/puppet-modules/puppet-haproxy-${PV}/centos/patches"

SRC_URI_STX += " \
	file://0001-Roll-up-TIS-patches.patch \
	file://0002-disable-config-validation-prechecks.patch \
	file://0003-Fix-global_options-log-default-value.patch \
	file://0004-Stop-invalid-warning-message \
	"

inherit openssl10
