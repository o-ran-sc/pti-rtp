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

DEPENDS += "\
    python-native \
    "

EXTRA_OECMAKE_PY2 = " \
    -DPYTHON_INSTALL_DIR=${libdir}/python2.7/site-packages \
    -DPYTHON_DESIRED:FILEPATH=${STAGING_BINDIR_NATIVE}/python-native/python \
    -DWITH_MAN=OFF \
    ${@bb.utils.contains('GI_DATA_ENABLED', 'True', '-DWITH_GIR=ON', '-DWITH_GIR=OFF', d)} \
    -DWITH_TESTS=OFF \
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
}

