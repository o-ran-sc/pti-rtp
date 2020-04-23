#
## Copyright (C) 2019 Wind River Systems, Inc.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://rootdn-should-not-bypass-ppolicy.patch \
	file://0021-openldap-and-stx-source-and-config-files.patch \
	file://stx-slapd.service \
	"

inherit pkgconfig useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "-r -g ldap -u 55 -d / -s /sbin/nologin -c 'OpenLDAP server' ldap"
GROUPADD_PARAM_${PN} = "-r -g 55 ldap"

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


# If liblmdb is needed, then patch the Makefile
#do_compile_append () {
#   cd ${S}/ltb-project-openldap-ppolicy-check-password-1.1
#   oe_runmake
#}

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

	install -m 0644  ${S}/../stx-slapd.service ${D}/${systemd_system_unitdir}/stx-slapd.service
	install -m 0755 -d ${D}/${sysconfdir}/sysconfig
	install -m 0644 slapd.sysconfig ${D}/${sysconfdir}/sysconfig/slapd
	install -m 0755 -d ${D}/${datadir}/openldap-servers
	install -m 0644 slapd.ldif ${D}/${datadir}/openldap-servers/slapd.ldif
	install -m 0750 -d ${D}/${sysconfdir}/openldap/slapd.d
	rm -rf ${D}/var/run

	#cd ${S}/
	#oe_runmake -e -C servers/slapd/overlays  DESTDIR=${D} install
	sed -i -e 's:\(/sbin/runuser\):/usr\1:g' ${D}/usr/libexec/openldap/functions

}

#pkg_postinst_ontarget_libldap-2.4_append () {
#	cp /usr/share/starlingx/slapd.service ${systemd_system_unitdir}/slapd.service
#	chmod 644 ${systemd_system_unitdir}/slapd.service
#	cp ${datadir}/starlingx/slapd.sysconfig ${sysconfdir}/sysconfig/slapd
#	systemctl daemon-reload
#	chmod 755 /etc/openldap
#	chmod 755 /etc/openldap/slapd.d
#}

FILES_${PN}_append = " \
	${datadir}/openldap-servers/ \
	${libexecdir}/openldap/ \
	${sysconfdir}/sysconfig \
	${sysconfdir}/tmpfiles.d \
	${systemd_system_unitdir}/stx-slapd.service \
	"

# *.la are openldap modules, so re-define
# to remove the *.la from -dev package
FILES_${PN}-dev = " \
	${includedir} \
	${FILES_SOLIBSDEV} \
	${libdir}/*.la \
	${libexecdir}/openldap/*${SOLIBSDEV} \
	"
