require metal-common.inc
SUBPATH0 = "bsp-files/"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://kickstart/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI += " \
	file://kickstarts-adjustment-and-fixes-or-poky-stx.patch;striplevel=2 \
	file://kickstarts-add-setting-for-debain-style-networking.patch;striplevel=2 \
	file://kickstarts-add-vlan-setting-for-debain-style-network.patch;striplevel=2 \
	"

PACKAGES += " \
	${PN}-pxeboot \
	${PN}-extracfgs \
	"

feed_dir = "/www/pages/feed/rel-${STX_REL}"

DEPENDS += "perl-native"

inherit deploy

do_unpack_append() {
    bb.build.exec_func('do_restore_files', d)
}

do_restore_files() {
	cd ${S}
	git reset ${SRCREV} kickstart/LICENSE
	git checkout kickstart/LICENSE
}

do_compile () {
	cd ${S}
	./centos-ks-gen.pl --release ${STX_REL}
}

do_install_prepend () {
	cd ${S}
	install -d -m 0755 ${D}${feed_dir}
	install -m 0444 generated/* ${D}${feed_dir}/

	install -d -m 0755 ${D}/pxeboot
	install -D -m 0444 pxeboot/* ${D}/pxeboot

	install -d -m 0755 ${D}/extra_cfgs
	install -D -m 0444 extra_cfgs/* ${D}/extra_cfgs
}

do_deploy () {
	mkdir -p ${DEPLOYDIR}/stx-kickstarts
	cp -f ${S}/generated/* ${DEPLOYDIR}/stx-kickstarts
}

addtask do_deploy after do_compile before do_build

FILES_${PN} = "${feed_dir}"
FILES_${PN}-pxeboot = "/pxeboot"
FILES_${PN}-extracfgs = "/extra_cfgs"
