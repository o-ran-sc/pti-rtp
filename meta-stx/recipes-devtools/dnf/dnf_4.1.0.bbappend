#
# Copyright (C) 2019 Wind River Systems, Inc.
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

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += "\
    file://0001-const-add-os-release-and-lsb-release-into-the-search.patch \
    "

DEPENDS += "\
    python-native \
    "

EXTRA_OECMAKE_PY2 = " \
    -DWITH_MAN=0 \
    -DPYTHON_INSTALL_DIR=${libdir}/python2.7/site-packages \
    -DPYTHON_DESIRED:FILEPATH=${STAGING_BINDIR_NATIVE}/python-native/python \
    "

RDEPENDS_${PN}_class-target += " \
    python-codecs \
    python-compression \
    python-core \
    python-curses \
    python-distutils \
    python-email \
    python-fcntl \
    python-iniparse \
    python-json \
    python-logging \
    python-misc \
    python-netclient \
    python-pygpgme \
    python-pyliblzma \
    python-shell \
    python-sqlite3 \
    python-threading \
    python2-rpm \
    "

do_configure_append() {
	rm -rf ${B}/build-py2
	mkdir -p ${B}/build-py2
	cd ${B}/build-py2

	cmake \
	  ${OECMAKE_GENERATOR_ARGS} \
	  $oecmake_sitefile \
	  ${OECMAKE_SOURCEPATH} \
	  -DCMAKE_INSTALL_PREFIX:PATH=${prefix} \
	  -DCMAKE_INSTALL_SYSCONFDIR:PATH=${sysconfdir} \
	  -DCMAKE_INSTALL_SO_NO_EXE=0 \
	  -DCMAKE_TOOLCHAIN_FILE=${WORKDIR}/toolchain.cmake \
	  -DCMAKE_NO_SYSTEM_FROM_IMPORTED=1 \
	  ${EXTRA_OECMAKE_PY2} \
	  -Wno-dev
}

cmake_runcmake_build_py2() {
	bbnote ${DESTDIR:+DESTDIR=${DESTDIR} }VERBOSE=1 cmake --build '${B}/build-py2' "$@" -- ${EXTRA_OECMAKE_BUILD}
	eval ${DESTDIR:+DESTDIR=${DESTDIR} }VERBOSE=1 cmake --build '${B}/build-py2' "$@" -- ${EXTRA_OECMAKE_BUILD}
}

do_compile_append() {
	cd ${B}/build-py2
	cmake_runcmake_build_py2 --target ${OECMAKE_TARGET_COMPILE}
}

do_install_append() {
	cd ${B}/build-py2
	DESTDIR='${D}' cmake_runcmake_build_py2 --target ${OECMAKE_TARGET_INSTALL}

	# add a symlink for yum
	lnr ${D}/${bindir}/dnf-2 ${D}/${bindir}/yum
}

