inherit autotools
inherit setuptools

require fault-common.inc

SRC_URI += "file://0001-fm-common-add-LDFLAGS.patch"

SUBPATH0 = "fm-common/sources"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

EXTRA_OEMAKE = '-e INCLUDES="-I./ " \
               EXTRACCFLAGS=" " \
               CCFLAGS="${CXXFLAGS} ${CCSHARED}" \
               LDFLAGS="${LDFLAGS} -shared" \
               LIBDIR=${libdir} \
               INCDIR=${includedir} \
               CGCS_DOC_DEPLOY=${docdir} \
               DESTDIR=${D} \
               BINDIR=${bindir} \
              '

do_configure_prepend () {
    sed -i -e 's|/usr/local/bin|${bindir}|' ${S}/fmConstants.h
    cd ${S}
} 


# need to build fm-common library first then setup.py can run
do_compile_prepend() {
	cd ${S}
	autotools_do_compile
}

do_install_prepend() {
	cd ${S}
	autotools_do_install
}
