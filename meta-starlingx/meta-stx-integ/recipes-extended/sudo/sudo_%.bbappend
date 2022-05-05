FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

DEPENDS += " \
	openldap \
	libgcrypt \
	"

SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "sudo-config"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

SRC_URI += " \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
	file://sudo-1.6.7p5-strip.patch \
	file://sudo-1.7.2p1-envdebug.patch \
	file://sudo-1.8.23-sudoldapconfman.patch \
	file://sudo-1.8.23-legacy-group-processing.patch \
	file://sudo-1.8.23-ldapsearchuidfix.patch \
	file://sudo-1.8.6p7-logsudouser.patch \
	file://sudo-1.8.23-nowaitopt.patch \
	file://sudo-1.8.23-fix-double-quote-parsing-for-Defaults-values.patch \
	"

EXTRA_OECONF += " \
	--with-pam-login \
	--with-editor=${base_bindir}/vi \
	--with-env-editor \
	--with-ignore-dot \
	--with-tty-tickets \
	--with-ldap \
	--with-ldap-conf-file="${sysconfdir}/sudo-ldap.conf" \
	--with-passprompt="[sudo] password for %Zp: " \
	--with-sssd \
	"

do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/${DSTSUFX0}/files/LICENSE ${S}/stx-configfiles-LICENSE
}

do_install_append () {
	install -m755 -d ${D}/${sysconfdir}/openldap/schema
	install -m644 ${S}/doc/schema.OpenLDAP  ${D}/${sysconfdir}/openldap/schema/sudo.schema
	install -m 440 ${WORKDIR}/${DSTSUFX0}/files/sysadmin.sudo  ${D}/${sysconfdir}/sudoers.d/sysadmin
}

# This means sudo package only owns files
# to avoid install conflict with openldap on
# /etc/openldap. Sure there is a better way.
DIRFILES = "1"
