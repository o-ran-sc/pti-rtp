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

DEPENDS += " \
	openldap \
	libgcrypt \
	"

SRC_URI += " \
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

do_install_append () {
	install -m755 -d ${D}/${sysconfdir}/openldap/schema
	install -m644 ${S}/doc/schema.OpenLDAP  ${D}/${sysconfdir}/openldap/schema/sudo.schema
}

# This means sudo package only owns files
# to avoid install conflict with openldap on
# /etc/openldap. Sure there is a better way.
DIRFILES = "1"
