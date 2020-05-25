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

SUMMARY_python2-rpm = "Python bindings for apps which will manupulate RPM packages"
DESCRIPTION_python2-rpm = "The python2-rpm package contains a module that permits applications \
written in the Python programming language to use the interface \
supplied by the RPM Package Manager libraries."

HOMEPAGE = "http://www.rpm.org"

# libraries are also LGPL - how to express this?
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=c0bf017c0fd1920e6158a333acabfd4a"

SRC_URI = "git://github.com/rpm-software-management/rpm;branch=rpm-4.14.x \
           file://0001-Do-not-add-an-unsatisfiable-dependency-when-building.patch \
           file://0001-Do-not-read-config-files-from-HOME.patch \
           file://0001-When-cross-installing-execute-package-scriptlets-wit.patch \
           file://0001-Do-not-reset-the-PATH-environment-variable-before-ru.patch \
           file://0002-Add-support-for-prefixing-etc-from-RPM_ETCCONFIGDIR-.patch \
           file://0001-Do-not-hardcode-lib-rpm-as-the-installation-path-for.patch \
           file://0001-Fix-build-with-musl-C-library.patch \
           file://0001-Add-a-color-setting-for-mips64_n32-binaries.patch \
           file://0011-Do-not-require-that-ELF-binaries-are-executable-to-b.patch \
           file://0001-Split-binary-package-building-into-a-separate-functi.patch \
           file://0002-Run-binary-package-creation-via-thread-pools.patch \
           file://0003-rpmstrpool.c-make-operations-over-string-pools-threa.patch \
           file://0004-build-pack.c-remove-static-local-variables-from-buil.patch \
           file://0001-perl-disable-auto-reqs.patch \
           file://0001-rpm-rpmio.c-restrict-virtual-memory-usage-if-limit-s.patch \
           "

PE = "1"
SRCREV = "753f6941dc32e94047b7cfe713ddd604a810b4db"

S = "${WORKDIR}/git"

DEPENDS = "nss libarchive db file popt xz bzip2 dbus elfutils python"
DEPENDS_append_class-native = " file-replacement-native bzip2-replacement-native"

inherit autotools gettext pkgconfig pythonnative
export PYTHON_ABI

# OE-core patches autoreconf to additionally run gnu-configize, which fails with this recipe
EXTRA_AUTORECONF_append = " --exclude=gnu-configize"

EXTRA_OECONF_append = " --without-lua --enable-python"
EXTRA_OECONF_append_libc-musl = " --disable-nls"

# --sysconfdir prevents rpm from attempting to access machine-specific configuration in sysroot/etc; we need to have it in rootfs
#
# --localstatedir prevents rpm from writing its database to native sysroot when building images
#
# Disable dbus for native, so that rpm doesn't attempt to inhibit shutdown via session dbus even when plugins support is enabled.
# Also disable plugins by default for native.
EXTRA_OECONF_append_class-native = " --sysconfdir=/etc --localstatedir=/var --disable-plugins"
EXTRA_OECONF_append_class-nativesdk = " --sysconfdir=/etc --localstatedir=/var --disable-plugins"

BBCLASSEXTEND = "native nativesdk"

PACKAGECONFIG ??= ""
PACKAGECONFIG[imaevm] = "--with-imaevm,,ima-evm-utils"

ASNEEDED = ""

do_compile_append () {
	cd python
	cp -r ../../git/python/* ./
	python setup.py build
}

do_install_append () {
	sed -i -e 's:${HOSTTOOLS_DIR}/::g' \
	    ${D}/${libdir}/rpm/macros

	sed -i -e 's|/usr/bin/python|${USRBINPATH}/env ${PYTHON_PN}|' \
	    ${D}${libdir}/rpm/pythondistdeps.py \
	    ${D}${libdir}/rpm/python-macro-helper

	# remove all contents except python2-rpm
	rm -r ${D}/var
	rm -r ${D}/usr/share
	rm -r ${D}/usr/include
	rm -r ${D}/usr/lib/librpm*
	rm -r ${D}/usr/lib/pkgconfig
	# rm -r ${D}/usr/src
	rm -r ${D}/usr/lib/rpm
	rm -r ${D}/usr/lib/rpm-plugins
	# rm -r ${D}/usr/lib/.debug
	rm -r ${D}/usr/bin

	cd python
	python setup.py install \
		--root=${D} --prefix=/usr \
		--install-lib=${PYTHON_SITEPACKAGES_DIR}/ --install-data=${datadir}
}

PACKAGES = "python2-rpm rpm2-dbg"
PROVIDES = "python2-rpm rpm2-dbg"
FILES_python2-rpm = " \
	${PYTHON_SITEPACKAGES_DIR}/rpm/ \
	${PYTHON_SITEPACKAGES_DIR}/rpm-${PV}-py${PYTHON_BASEVERSION}.egg-info \
	"

# rpm 5.x was packaging the rpm build tools separately
#RPROVIDES_${PN} += "rpm-build"

RDEPENDS_${PN} = "bash perl python-core"
RDEPENDS_python2-rpm = "rpm"
DEPENDS_python2-rpm = "rpm"
