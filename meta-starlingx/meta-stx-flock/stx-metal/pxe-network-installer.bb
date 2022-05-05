require metal-common.inc
SUBPATH0 = "installer/pxe-network-installer/pxe-network-installer"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS += " syslinux"

RDEPENDS_${PN}_append += " \
	bash \
	syslinux \
	syslinux-chain \
	syslinux-misc \
	syslinux-pxelinux \
	"

do_compile[noexec] = "1"

do_unpack_append() {
    bb.build.exec_func('do_restore_files', d)
}

do_restore_files() {
	cd ${S}
	git reset ${SRCREV} bsp-files/
	git checkout bsp-files
}

do_install() {
	install -d -m 0755 ${D}/pxeboot
	install -d -m 0755 ${D}/pxeboot/pxelinux.cfg.files
	install -d -m 0755 ${D}/pxeboot/rel-${STX_REL}
	install -d -m 0755 ${D}/pxeboot/EFI

	install -d -m 0755 ${D}/pxeboot/EFI/poky-stx
	ln -fs poky-stx ${D}/pxeboot/EFI/centos
	ln -fs ${libdir}/grub/x86_64-efi ${D}/pxeboot/EFI/poky-stx/

	install -d -m 0755 ${D}/${sbindir}
	install -m 755 pxeboot-update.sh ${D}/${sbindir}/pxeboot-update-${STX_REL}.sh

	install -m 644 bsp-files/kickstarts/post_clone_iso_ks.cfg ${D}/pxeboot/post_clone_iso_ks.cfg

	install -m 644 default ${D}/pxeboot/pxelinux.cfg.files/default
	install -m 644 default.static ${D}/pxeboot/pxelinux.cfg.files/default.static
	install -m 644 centos-pxe-controller-install ${D}/pxeboot/pxelinux.cfg.files/pxe-controller-install-${STX_REL}
	install -m 644 centos-pxe-worker-install ${D}/pxeboot/pxelinux.cfg.files/pxe-worker-install-${STX_REL}
	install -m 644 centos-pxe-smallsystem-install ${D}/pxeboot/pxelinux.cfg.files/pxe-smallsystem-install-${STX_REL}
	install -m 644 centos-pxe-storage-install ${D}/pxeboot/pxelinux.cfg.files/pxe-storage-install-${STX_REL}
	install -m 644 centos-pxe-worker_lowlatency-install ${D}/pxeboot/pxelinux.cfg.files/pxe-worker_lowlatency-install-${STX_REL}
	install -m 644 centos-pxe-smallsystem_lowlatency-install ${D}/pxeboot/pxelinux.cfg.files/pxe-smallsystem_lowlatency-install-${STX_REL}

	# UEFI support
	install -m 644 pxe-grub.cfg ${D}/pxeboot/pxelinux.cfg.files/grub.cfg
	install -m 644 pxe-grub.cfg.static ${D}/pxeboot/pxelinux.cfg.files/grub.cfg.static

	install -m 644 efi-centos-pxe-controller-install ${D}/pxeboot/pxelinux.cfg.files/efi-pxe-controller-install-${STX_REL}
	install -m 644 efi-centos-pxe-worker-install ${D}/pxeboot/pxelinux.cfg.files/efi-pxe-worker-install-${STX_REL}
	install -m 644 efi-centos-pxe-smallsystem-install ${D}/pxeboot/pxelinux.cfg.files/efi-pxe-smallsystem-install-${STX_REL}
	install -m 644 efi-centos-pxe-storage-install ${D}/pxeboot/pxelinux.cfg.files/efi-pxe-storage-install-${STX_REL}
	install -m 644 efi-centos-pxe-worker_lowlatency-install ${D}/pxeboot/pxelinux.cfg.files/efi-pxe-worker_lowlatency-install-${STX_REL}
	install -m 644 efi-centos-pxe-smallsystem_lowlatency-install ${D}/pxeboot/pxelinux.cfg.files/efi-pxe-smallsystem_lowlatency-install-${STX_REL}

	sed -i -e "s/xxxSW_VERSIONxxx/${STX_REL}/g" \
		-e "s/inst.ks/ks/g" \
		-e "s/\(xxxAPPEND_OPTIONSxxx\)/LABEL=initrd-install \1/" \
		${D}/pxeboot/pxelinux.cfg.files/pxe-* ${D}/pxeboot/pxelinux.cfg.files/efi-pxe-*

	# Copy Titanium grub.cfg. It will be used to create ISO on the Controller.
	install -m 0644 bsp-files/grub.cfg ${D}/pxeboot/EFI/ 

	# UEFI bootloader expect the grub.cfg file to be in /pxeboot/ so create a symlink for it
	ln -fs pxelinux.cfg/grub.cfg ${D}/pxeboot/grub.cfg
}

pkg_postinst_pxe-network-installer() {
        install -m 0644 $D${datadir}/syslinux/menu.c32 $D/pxeboot
        install -m 0644 $D${datadir}/syslinux/vesamenu.c32 $D/pxeboot
        install -m 0644 $D${datadir}/syslinux/chain.c32 $D/pxeboot
        install -m 0644 $D${datadir}/syslinux/ldlinux.c32 $D/pxeboot
        install -m 0644 $D${datadir}/syslinux/linux.c32 $D/pxeboot
        install -m 0644 $D${datadir}/syslinux/libutil.c32 $D/pxeboot
        install -m 0644 $D${datadir}/syslinux/reboot.c32 $D/pxeboot
        install -m 0644 $D${datadir}/syslinux/pxechn.c32 $D/pxeboot
        install -m 0644 $D${datadir}/syslinux/pxelinux.0 $D/pxeboot
}

FILES_${PN}_append  = " \
	/pxeboot \
	${sbindir}/pxeboot-update-${STX_REL}.sh \
	"
