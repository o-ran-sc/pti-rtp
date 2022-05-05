DESCRIPTION = "Build Info"
SUMMARY  = "Build Info"

SRC_URI += " \
	file://build.info \
	file://0001-build_info_license.patch \
	"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install_append() {
	install -d ${D}/${sysconfdir}
	install -m 644 ${WORKDIR}/build.info ${D}/${sysconfdir}
	sed -i -e "s/@OS@/${DISTRO}/" \
	       -e "s/@STX_RELEASE@/${STX_REL}/" \
	       -e "s/@STX_ID@/${STX_ID}/" \
	       -e "s/@BUILD_DATE@/${STX_BUILD_DATE}/" \
	       ${D}/${sysconfdir}/build.info

}

do_install[vardepsexclude] += "STX_BUILD_DATE"
