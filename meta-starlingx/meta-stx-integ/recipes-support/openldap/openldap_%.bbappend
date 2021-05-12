FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "openldap-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

SRC_URI += " \
	file://rootdn-should-not-bypass-ppolicy.patch \
	file://0021-openldap-and-stx-source-and-config-files.patch \
	"
RRECOMMENDS_openldap += " \
        openldap-backend-shell \
        openldap-backend-passwd \
        openldap-backend-null \
        openldap-backend-monitor \
        openldap-backend-meta \
        openldap-backend-ldap \
        openldap-backend-dnssrv \
        openldap-staticdev \
        openldap-locale \
        openldap-overlay-proxycache \
        openldap-slapd \
        openldap-slurpd \
        openldap-bin \
        "

inherit pkgconfig useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "-r -g ldap -u 55 -d / -s /sbin/nologin -c 'OpenLDAP server' ldap"
GROUPADD_PARAM_${PN} = "-r -g 55 ldap"

inherit systemd
SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_SERVICE_${PN}_append = "slapd.service"
SYSETMD_AUTO_ENABLE_${PN} = "enable"
# Needed for stx init.d/openldap
DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

PACKAGECONFIG_CONFARGS_remove = "--with-tls=gnutls "
DEPENDS += " \
	openssl \
	glibc \
	mariadb \
	mariadb-native \
	libtirpc \
	"

RDEPENDS_${PN}_append = " bash"

# Do not remove libtool la files slapd.conf uses ppolicy.la 
REMOVE_LIBTOOL_LA = "0"


# Defaults:
#	--enable-bdb=no
#	--enable-hdb=no
#	--enable-bdb=no
# 	--enable-monitor=mod 
######
# Stx :
#	--enable-wrappers=yes
#	--enable-moznss-compatibility=yes

#################
# TODO:
#	mysql_config: native command missing

EXTRA_OECONF += " \
		--enable-syslog \
		--enable-proctitle \
		--enable-ipv6 \
		--enable-local \
		--enable-slapd \
		--enable-dynacl \
		--enable-aci \
		--enable-cleartext \
		--enable-crypt \
		--enable-lmpasswd \
		--enable-modules \
		--enable-rewrite \
		--enable-rlookups \
		--disable-slp \
		--enable-wrappers=no \
		--enable-backends=mod \
		--enable-bdb=yes \
		--enable-hdb=yes \
		--enable-mdb=yes \
		--enable-monitor=yes \
		--disable-ndb \
		--enable-overlays=mod \
		--disable-static \
		--enable-shared \
		--with-cyrus-sasl \
		--without-fetch \
		--with-tls=openssl \
		"
#	--enable-moznss-compatibility=no 
# NEW:
# --enable-lmpasswd 
# --enable-slapi
# --enable-wrappers
# --enable-moznss-compatibility=yes

do_configure_append () {
   cd ${S}
   ln -f -s ${S}/contrib/slapd-modules/smbk5pwd/smbk5pwd.c servers/slapd/overlays
   ln -f -s ${S}/contrib/slapd-modules/allop/allop.c servers/slapd/overlays
   ln -f -s ${S}/contrib/slapd-modules/passwd/sha2/sha2.c servers/slapd/overlays
   ln -f -s ${S}/contrib/slapd-modules/passwd/sha2/sha2.h servers/slapd/overlays
   ln -f -s ${S}/contrib/slapd-modules/passwd/sha2/slapd-sha2.c servers/slapd/overlays
}


do_install_append () {
	
	# For this we need to build ltb-project-openldap
	#install -m 755 check_password.so.%{check_password_version} %{buildroot}%{_libdir}/openldap/

	cd ${S}/stx-sources
	install -m 0755 -d ${D}/var/run/openldap
	install -m 0755 -d ${D}/${sysconfdir}/tmpfiles.d
	install -m 0755 ${S}/stx-sources/slapd.tmpfiles ${D}/${sysconfdir}/tmpfiles.d/slapd.conf 
	install -m 0755 ${S}/stx-sources/openldap.tmpfiles ${D}/${sysconfdir}/tmpfiles.d/openldap.conf 
	install -m 0755 ${S}/stx-sources/ldap.conf ${D}/${sysconfdir}/tmpfiles.d/ldap.conf 

	# The database directory MUST exist prior to running slapd AND
	# should only be accessible by the slapd and slap tools.
	# Mode 700 recommended.
	echo "d /var/lib/openldap-data 0700 ldap ldap -" >> ${D}/${sysconfdir}/tmpfiles.d/slapd.conf

	install -m 0644 libexec-functions ${D}/${libexecdir}/openldap/functions
	install -m 0755 libexec-convert-config.sh ${D}/${libexecdir}/openldap/convert-config.sh
	install -m 0755 libexec-check-config.sh ${D}/${libexecdir}/openldap/check-config.sh
	install -m 0755 libexec-upgrade-db.sh ${D}/${libexecdir}/openldap/upgrade-db.sh

	install -m 0755 libexec-create-certdb.sh ${D}/${libexecdir}/openldap/create-certdb.sh
	install -m 0755 libexec-generate-server-cert.sh ${D}/${libexecdir}/openldap/generate-server-cert.sh
	install -m 0755 libexec-update-ppolicy-schema.sh ${D}/${libexecdir}/openldap/update-ppolicy-schema.sh

	install -m 0755 -d ${D}/${sysconfdir}/sysconfig
	install -m 0644 slapd.sysconfig ${D}/${sysconfdir}/sysconfig/slapd
	install -m 0755 -d ${D}/${datadir}/openldap-servers
	install -m 0644 slapd.ldif ${D}/${datadir}/openldap-servers/slapd.ldif
	install -m 0750 -d ${D}/${sysconfdir}/openldap/slapd.d
	rm -rf ${D}/var/run

	sed -i -e 's:\(/sbin/runuser\):/usr\1:g' ${D}/usr/libexec/openldap/functions 

	install -m 755 ${STX_METADATA_PATH}/files/initscript ${D}/${sysconfdir}/init.d/openldap
	install -m 600 ${STX_METADATA_PATH}/files/slapd.conf ${D}/${sysconfdir}/openldap/slapd.conf

	install -m 600 ${STX_METADATA_PATH}/files/initial_config.ldif ${D}/${sysconfdir}/openldap/initial_config.ldif

	install -D -m 644 ${STX_METADATA_PATH}/files/slapd.service ${D}/${systemd_system_unitdir}/slapd.service
	sed -i -e 's|/var/run|/run|' ${D}/${systemd_system_unitdir}/slapd.service

	install -m 644 ${STX_METADATA_PATH}/files/slapd.sysconfig ${D}/${sysconfdir}/sysconfig/slapd

}


FILES_${PN}_append = " \
	${datadir}/openldap-servers/ \
	${libexecdir}/openldap/ \
	${sysconfdir}/sysconfig \
	${sysconfdir}/tmpfiles.d \
	${systemd_system_unitdir}/slapd.service  \
	${sysconfdir}/openldap/initial_config.ldif \
	"

# *.la are openldap modules. 
FILES_${PN}-dev = " \
	${includedir} \
	${FILES_SOLIBSDEV} \
	${libdir}/*.la \
	${libexecdir}/openldap/*${SOLIBSDEV} \
	"

