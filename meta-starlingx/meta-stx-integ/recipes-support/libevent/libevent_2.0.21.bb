SUMMARY = "An asynchronous event notification library"
HOMEPAGE = "http://libevent.org/"
BUGTRACKER = "https://github.com/libevent/libevent/issues"
SECTION = "libs"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=45c5316ff684bcfe2f9f86d8b1279559"

SRC_URI = " \
    https://github.com/downloads/libevent/libevent/${BP}-stable.tar.gz \
    file://libevent-obsolete_automake_macros.patch \
    file://libevent-disable_tests.patch \
    file://libevent-ipv6-client-socket.patch \
"

SRC_URI[md5sum] = "b2405cc9ebf264aa47ff615d9de527a2"
SRC_URI[sha256sum] = "22a530a8a5ba1cb9c080cba033206b17dacd21437762155c6d30ee6469f574f5"

S = "${WORKDIR}/${BP}-stable"

inherit openssl10

PACKAGECONFIG ??= "openssl"
PACKAGECONFIG[openssl] = "--enable-openssl,--disable-openssl,openssl10"

inherit autotools

# Needed for Debian packaging
LEAD_SONAME = "libevent-2.0.so"

BBCLASSEXTEND = "native nativesdk"
