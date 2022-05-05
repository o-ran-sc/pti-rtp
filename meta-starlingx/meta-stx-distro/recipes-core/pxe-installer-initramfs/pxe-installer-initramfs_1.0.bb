SUMMARY = "Provides initramfs used for pxe installer"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

do_install[depends] += "core-image-anaconda-initramfs:do_image_complete"

do_install() {
    install -d ${D}/boot
    install -m 0644  ${DEPLOY_DIR_IMAGE}/core-image-anaconda-initramfs-${MACHINE}.cpio.gz ${D}/boot/installer-initrd_1.0
    ln -s installer-initrd_1.0 ${D}/boot/installer-initrd
}

FILES_${PN} = "/boot"
