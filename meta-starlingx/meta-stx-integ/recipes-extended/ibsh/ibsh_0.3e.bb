
SUMMARY = "ibsh Iron Bar Shell"
DESCRIPTION = "CGCS add default users types"
SECTION = "base/shell"
HOMEPAGE = "https://sourceforge.net/projects/ibsh/"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://COPYRIGHT;md5=e5ef2fe07fcba90ea59f9f1a61f7526b \
"

SRC_URI[md5sum] = "41ea08a03c6bd805dff1a5ff25ea1d5b"
SRC_URI[sha256sum] = "4c40d0841527c76fc75ccc27d32b575543d02d661973fc3561004efb6033206d"
SRC_URI = "https://downloads.sourceforge.net/project/ibsh/ibsh/ibsh-0.3e/ibsh-0.3e.tar.gz"

do_install() {
	cd ${S}
	install -d -m0755 ${D}/${base_bindir}
	install -d -m0755 ${D}/${sysconfdir}/ibsh/cmds/
	install -d -m0755 ${D}/${sysconfdir}/ibsh/xtns/
	install -m0755 ibsh ${D}/${base_bindir}
	install -m0644 globals.cmds ${D}/${sysconfdir}/ibsh
	install -m0644 globals.xtns ${D}/${sysconfdir}/ibsh
}
