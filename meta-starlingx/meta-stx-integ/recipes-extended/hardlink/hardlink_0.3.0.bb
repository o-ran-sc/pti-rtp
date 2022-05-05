
DESCRIPTION = "Hardlink is a tool which replaces multiple copies of a file with hardlinks"

HOMEPAGE = "https://jak-linux.org/projects/hardlink"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://hardlink.1;md5=c7a9a3326f28fa218d9af0251a5e1ae4"

SRC_URI = "https://jak-linux.org/projects/hardlink/hardlink_0.3.0.tar.xz"
SRC_URI[md5sum] = "72f1a460adb6874c151deab766e434ad"
SRC_URI[sha256sum] = "e8c93dfcb24aeb44a75281ed73757cb862cc63b225d565db1c270af9dbb7300f"

DEPENDS = " attr"
inherit pkgconfig

do_install() {
	cd ${S}
	oe_runmake -e DESTDIR=${D} BINDIR=${bindir} MANDIR=${datadir} install
}

FILES_${PN}-doc = "${datadir}/"
