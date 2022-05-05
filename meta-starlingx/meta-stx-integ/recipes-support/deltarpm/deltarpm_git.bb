
DESCRIPTION = "Makedeltarpm creates a deltarpm from two rpms. The deltarpm can \
		later be used to recreate the new rpm from either filesystem data \
		or the old rpm. Use the -v option to make makedeltarpm more verbose \
		about its work (use it twice to make it even more verbose).\
		"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.BSD;md5=574af071cf0d60a71cb781844bbe2d76"

SRCREV = "c5e0ca7482e2cfea5e4d902ffe488e0a71ed3e67"
# SRCREV = "8660d976f5d2b73adf1088d67341be9c3646f2f2"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "3.6.2+git${SRCPV}"

SRC_URI = "git://github.com/rpm-software-management/deltarpm.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
		file://0001-Makefile-patch-fix-build-errors.patch \
		"

DEPENDS += " python perl bzip2 rpm zlib python xz zstd"

do_compile_append () {
	cd ${S}
	oe_runmake -e DESTDIR=${D} bindir=${bindir} mandir=${mandir} python

}

do_install () {
	cd ${S}
	oe_runmake -e DESTDIR=${D} bindir=${bindir} mandir=${mandir} install
}

FILES_${PN}_append += " \
	/usr/lib/python2.7/site-packages/deltarpm.py \
	/usr/lib/python2.7/site-packages/_deltarpmmodule.so \
	"
