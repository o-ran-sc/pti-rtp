
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
