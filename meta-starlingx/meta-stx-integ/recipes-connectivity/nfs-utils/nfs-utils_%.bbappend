SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "filesystem-scripts"
DSTSUFX0 = "stx-configfiles"
SUBPATH1 = "nfs-utils-config"
DSTSUFX1 = "stx-nfs-utils"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://stx-nfs-utils-LICENSE;beginline=1;endline=10;md5=bbfb66ff81fec36fc2b2c9d98e01b1d8 \
	"

SRC_URI += " \
        git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
        git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX1};branch="r/stx.3.0";subpath=${SUBPATH1};name=opendev \
        file://nfsserver-remove-the-f-option-for-rpc.mountd.patch;patchdir=../${DSTSUFX1};striplevel=2 \
"

do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -pf ${WORKDIR}/${DSTSUFX0}/LICENSE ${S}/stx-configfiles-LICENSE
    cp -pf ${WORKDIR}/${DSTSUFX0}/filesystem-scripts-1.0/LICENSE ${S}/stx-filesystem-scripts-LICENSE
    cp -pf ${WORKDIR}/${DSTSUFX1}/centos/nfs-utils-config.spec ${S}/stx-nfs-utils-LICENSE
}

inherit systemd

PACKAGES =+ "${PN}-config"
SYSTEMD_PACKAGES += "${PN}-config"
SYSTEMD_SERVICE_${PN}-config = "uexportfs.service nfscommon.service nfsserver.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
SYSTEMD_AUTO_ENABLE_${PN}-config = "enable"

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

do_install_append() {
	mv ${D}/${sbindir}/sm-notify ${D}/${sbindir}/nfs-utils-client_sm-notify
	install -D -m 755 ${WORKDIR}/${DSTSUFX0}/filesystem-scripts-1.0/uexportfs ${D}/${sysconfdir}/init.d/uexportfs 

	# install nfs.conf and enable udp proto
	install -m 0755 ${S}/nfs.conf ${D}${sysconfdir}
	sed -i -e 's/#\(\[nfsd\]\)/\1/' -e 's/#\( udp=\).*/\1y/' ${D}${sysconfdir}/nfs.conf

	# add initial exports file
	echo "# Initial exports for nfs" > ${D}${sysconfdir}/exports

	# Libdir here is hardcoded in other scripts.
	install -d -m 0755 ${D}/usr/lib/ocf/resource.d/platform/
	install -D -m 755 ${WORKDIR}/${DSTSUFX0}/filesystem-scripts-1.0/nfsserver-mgmt \
		${D}/usr/lib/ocf/resource.d/platform/nfsserver-mgmt
	
	install -p -D -m 755 ${WORKDIR}/${DSTSUFX0}/filesystem-scripts-1.0/nfs-mount ${D}/${bindir}/nfs-mount
	install -D -m 755 ${WORKDIR}/${DSTSUFX0}/filesystem-scripts-1.0/uexportfs.service \
			${D}/${systemd_system_unitdir}/uexportfs.service


	install -d ${D}/${sysconfdir}/init.d
	install -d ${D}/${systemd_system_unitdir}

	install -m 755 -p -D ${WORKDIR}/${DSTSUFX1}/files/nfscommon   	 ${D}/${sysconfdir}/init.d
        install -m 644 -p -D ${WORKDIR}/${DSTSUFX1}/files/nfscommon.service  	 ${D}/${systemd_system_unitdir}/

        install -m 755 -p -D ${WORKDIR}/${DSTSUFX1}/files/nfsserver            ${D}/${sysconfdir}/init.d
        install -m 644 -p -D ${WORKDIR}/${DSTSUFX1}/files/nfsserver.service    ${D}/${systemd_system_unitdir}
        install -m 644 -p -D ${WORKDIR}/${DSTSUFX1}/files/nfsmount.conf        ${D}/${sysconfdir}/nfsmount.conf

}

FILES_${PN}-config = "\
	${systemd_system_unitdir}/nfscommon.service \
	${systemd_system_unitdir}/nfsserver.service \
	${systemd_system_unitdir}/uexportfs.service \
	${sysconfdir}/nfsmount.conf \
	"
FILES_${PN}_append = " usr/lib/ocf/resource.d"
