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

SYSROOT_DIRS_BLACKLIST = " ${bindir} ${sysconfdir}"

SYSROOT_PREPROCESS_FUNCS += "openssl10_avoid_conflict"

openssl10_avoid_conflict () {

       # For libaries remove statics and symlinks to avoid conflict

	rm ${SYSROOT_DESTDIR}${libdir}/libssl.so
	rm ${SYSROOT_DESTDIR}${libdir}/libcrypto.so
	rm ${SYSROOT_DESTDIR}${libdir}/libssl.a
	rm ${SYSROOT_DESTDIR}${libdir}/libcrypto.a
	#mv ${SYSROOT_DESTDIR}${libdir}/pkgconfig/libcrypto.pc ${SYSROOT_DESTDIR}${libdir}/pkgconfig/libcrypto10.pc 
	#mv ${SYSROOT_DESTDIR}${libdir}/pkgconfig/libssl.pc ${SYSROOT_DESTDIR}${libdir}/pkgconfig/libcrypto10.pc 
	#mv ${SYSROOT_DESTDIR}${libdir}/pkgconfig/openssl.pc ${SYSROOT_DESTDIR}${libdir}/pkgconfig/openssl10.pc 
	rm -rf ${SYSROOT_DESTDIR}${libdir}/pkgconfig
	rm -rf ${SYSROOT_DESTDIR}${libdir}/engines 
	# For headers
	mkdir -p ${SYSROOT_DESTDIR}${includedir}/openssl10
	mv ${SYSROOT_DESTDIR}${includedir}/openssl ${SYSROOT_DESTDIR}${includedir}/openssl10
}
