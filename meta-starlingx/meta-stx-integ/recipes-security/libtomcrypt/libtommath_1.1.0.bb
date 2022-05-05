
SUMMARY = "A portable number theoretic multiple-precision integer library"
DESCRIPTION = " \
  A free open source portable number theoretic multiple-precision integer \
  library written entirely in C. (phew!). The library is designed to provide \
  a simple to work with API that provides fairly efficient routines that \
  build out of the box without configuration. \
"
HOMEPAGE = "https://github.com/libtom/libtommath"

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=23e7e0a32e53a2b1d35f5fd9ef053402"

DEPENDS = "libtool-cross"

SRC_URI = "https://github.com/libtom/libtommath/releases/download/v${PV}/ltm-${PV}.tar.xz"

SRC_URI[md5sum] = "b2da4488c9024976d36870132f4b8a42"
SRC_URI[sha256sum] = "90466c88783d1fe9f5c2364a69f5479f10d73ed616011be6196f35f7f1537ead"

EXTRA_OEMAKE = " \
	LIBTOOL=${STAGING_BINDIR_CROSS}/${HOST_SYS}-libtool \
	LIBPATH=${libdir} \
	INCPATH=${includedir} \
	-f makefile.shared \
	"

do_install() {
	oe_runmake install DESTDIR=${D}
}
