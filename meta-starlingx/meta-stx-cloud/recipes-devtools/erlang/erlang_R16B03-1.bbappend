
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# erlang < 20.0 is not compatibel with OpenSSL 1.1.x
inherit openssl10

SRC_URI += " \
	file://fix-install-ownership.patch \
	"

EXTRA_OECONF = '--with-ssl=${STAGING_DIR_TARGET}/usr --without-krb5 --without-zlib'

do_configure_prepend () {
    export erl_xcomp_sysroot="${STAGING_DIR_HOST}/usr"
    export erl_xcomp_isysroot="${STAGING_DIR_NATIVE}"

    sed -i -e 's/opensslconf.h/opensslconf-64.h/' \
        ${STAGING_INCDIR}/openssl10/openssl/rc4.h \
        ${STAGING_INCDIR}/openssl10/openssl/rc2.h
}

do_install_append () {
    # Fix the do_package_qa issue
    chown -R root:root ${D}
}
