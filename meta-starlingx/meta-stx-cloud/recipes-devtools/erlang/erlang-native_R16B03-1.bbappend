
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# erlang < 20.0 is not compatibel with OpenSSL 1.1.x
inherit openssl10
DEPENDS_append = " openssl-native"

SRC_URI += "file://erts-configure.in-avoid-RPATH-warning.patch"

EXTRA_OECONF = '--with-ssl=${STAGING_DIR_NATIVE}/usr --without-krb5 --without-zlib'
