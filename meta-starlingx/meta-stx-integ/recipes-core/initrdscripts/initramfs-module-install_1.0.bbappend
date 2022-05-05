
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

do_install_append () {
    sed -i -e 's/@STX_ID@/${STX_ID}/' ${D}/init.d/install.sh
}

RDEPENDS_${PN} += "\
    util-linux-mount \
    util-linux-uuidgen \
    lvm2 \
"
