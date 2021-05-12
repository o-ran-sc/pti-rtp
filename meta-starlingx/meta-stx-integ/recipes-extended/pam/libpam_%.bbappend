FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "pam-config"

LICENSE_append = " & Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/centos/pam-config.spec;beginline=1;endline=10;md5=fa5ec877c97a75a09b397bccc95b2b87 \
	"

SRC_URI += " \
	file://util-linux-pam-postlogin.patch \
	"

do_install_append () {

    # From util-linux-config patch
    install -m 644 ${S}/stx.postlogin ${D}/${sysconfdir}/pam.d/postlogin

    install  -m 644 ${STX_METADATA_PATH}/files/sshd.pam        ${D}/${sysconfdir}/pam.d/sshd.pam
    install  -m 644 ${STX_METADATA_PATH}/files/common-account  ${D}/${sysconfdir}/pam.d/common-account
    install  -m 644 ${STX_METADATA_PATH}/files/common-auth     ${D}/${sysconfdir}/pam.d/common-auth
    install  -m 644 ${STX_METADATA_PATH}/files/common-password ${D}/${sysconfdir}/pam.d/common-password
    install  -m 644 ${STX_METADATA_PATH}/files/common-session  ${D}/${sysconfdir}/pam.d/common-session

    install  -m 644 ${STX_METADATA_PATH}/files/common-session-noninteractive  \
		${D}/${sysconfdir}/pam.d/common-session-noninteractive

    install  -m 644 ${STX_METADATA_PATH}/files/system-auth.pamd ${D}/${sysconfdir}/pam.d/system-auth
	# A better place is the autoconfig from kickstarter
	sed -i -e '/password .*pam_ldap.so/,/session .*revoke/ s/^$/password required pam_deny.so\n/g' \
		${D}/${sysconfdir}/pam.d/system-auth
}

RRECOMMENDS_${PN}_append = " \
    nss-pam-ldapd \
    libpwquality \
    pam-plugin-access \
    pam-plugin-cracklib \
    pam-plugin-debug \
    pam-plugin-deny \
    pam-plugin-echo \
    pam-plugin-env \
    pam-plugin-exec \
    pam-plugin-faildelay \
    pam-plugin-filter \
    pam-plugin-ftp \
    pam-plugin-group \
    pam-plugin-issue \
    pam-plugin-keyinit \
    pam-plugin-lastlog \
    pam-plugin-limits \
    pam-plugin-listfile \
    pam-plugin-localuser \
    pam-plugin-loginuid \
    pam-plugin-mail \
    pam-plugin-mkhomedir \
    pam-plugin-motd \
    pam-plugin-namespace \
    pam-plugin-nologin \
    pam-plugin-permit \
    pam-plugin-pwhistory \
    pam-plugin-rhosts \
    pam-plugin-rootok \
    pam-plugin-securetty \
    pam-plugin-shells \
    pam-plugin-stress \
    pam-plugin-succeed-if \
    pam-plugin-tally \
    pam-plugin-stress \
    pam-plugin-succeed-if \
    pam-plugin-tally \
    pam-plugin-tally2 \
    pam-plugin-time \
    pam-plugin-timestamp \
    pam-plugin-umask \
    pam-plugin-unix \
    pam-plugin-warn \
    pam-plugin-wheel \
    pam-plugin-xauth \
    "
