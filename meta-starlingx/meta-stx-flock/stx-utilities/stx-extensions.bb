DESCRIPTION  = "TIS Extensions to thirdparty pkgs"
SUMMARY  = "TIS Extensions to thirdparty pkgs"

require utilities-common.inc

SUBPATH0 = "utilities/stx-extensions/files"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

RDEPENDS_${PN}  += " systemd"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {

	install -p -d -m 0755 ${D}/${sysconfdir}/sysctl.d
	install -m 0644 coredump-sysctl.conf ${D}/${sysconfdir}/sysctl.d/50-coredump.conf

	# Fix the systemd unitdir and the arguments for kernel.core_pattern
	sed -i -e 's|${nonarch_libdir}/systemd|${systemd_unitdir}|' \
	       -e 's/%p/%P/' -e 's/%e/%c %h %e/' \
	       ${D}/${sysconfdir}/sysctl.d/50-coredump.conf

	install -p -d -m 0755 ${D}/${sysconfdir}/systemd/coredump.conf.d
	install -m 0644 coredump.conf ${D}/${sysconfdir}/systemd/coredump.conf.d/coredump.conf

	install -p -d -m 0755 ${D}/${sysconfdir}/modules-load.d
	install -m 0644 modules-load-vfio.conf ${D}/${sysconfdir}/modules-load.d/vfio.conf

}
