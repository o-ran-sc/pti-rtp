
TRANSFORM_NAME = "s,grub,grub2,"
EXTRA_OECONF += "--program-transform-name=${TRANSFORM_NAME}"

do_install_append() {
    for file in ${D}${bindir}/grub2-* ${D}${sbindir}/grub2-*; do
        ln -sf $(basename ${file}) $(echo ${file}|sed 's/grub2/grub/')
    done
    
    sed -i -e 's/ ro / rw /' ${D}${sysconfdir}/grub.d/10_linux
}

FILES_${PN}-editenv = "${bindir}/grub2-editenv"
