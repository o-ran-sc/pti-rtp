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

PACKAGES += " pxe-network-installer"

DEPENDS += " syslinux"
RDEPENDS_pxe-network-installer += " \
	bash \
	syslinux \
	syslinux-chain \
	syslinux-misc \
	syslinux-pxelinux \
	"

do_configure_prepend () {
	:
} 

do_compile_prepend () {
	:
}

do_install_prepend () {
	cd ${S}/installer/pxe-network-installer/pxe-network-installer
	install -d -m 0755 ${D}/pxeboot
	install -d -m 0755 ${D}/pxeboot/pxelinux.cfg.files
	install -d -m 0755 ${D}/pxeboot/rel-${STX_REL}
	install -d -m 0755 ${D}/pxeboot/EFI
	install -d -m 0755 ${D}/pxeboot/EFI/poky-stx
	ln -fs poky-stx ${D}/pxeboot/EFI/centos
	ln -fs ${libdir}/grub/x86_64-efi ${D}/pxeboot/EFI/poky-stx/

	install -d -m 0755 ${D}/${sbindir}
	install -m 755 pxeboot-update.sh ${D}/${sbindir}/pxeboot-update-${STX_REL}.sh

	install -m 644 ${S}/bsp-files/kickstarts/post_clone_iso_ks.cfg ${D}/pxeboot/post_clone_iso_ks.cfg
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
	install -m 0644 ${S}/bsp-files/grub.cfg ${D}/pxeboot/EFI/ 

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

FILES_pxe-network-installer = " \
	/pxeboot \
	${sbindir}/pxeboot-update-${STX_REL}.sh \
	"
