FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH0 = "filesystem-scripts"
STX_SUBPATH1 = "nfs-utils-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/${STX_SUBPATH0}/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://${STX_METADATA_PATH}/${STX_SUBPATH0}/filesystem-scripts-1.0/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	file://${STX_METADATA_PATH}/${STX_SUBPATH1}/centos/nfs-utils-config.spec;beginline=1;endline=10;md5=bbfb66ff81fec36fc2b2c9d98e01b1d8 \
	"

inherit systemd

PACKAGES =+ "${PN}-config"
SYSTEMD_PACKAGES += "${PN}-config"
SYSTEMD_SERVICE_${PN}-config = "uexportfs.service nfscommon.service nfsserver.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
SYSTEMD_AUTO_ENABLE_${PN}-config = "enable"

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

do_install_append() {
	mv ${D}/${sbindir}/sm-notify ${D}/${sbindir}/nfs-utils-client_sm-notify
	install -D -m 755 ${STX_METADATA_PATH}/${STX_SUBPATH0}/filesystem-scripts-1.0/uexportfs ${D}/${sysconfdir}/init.d/uexportfs

	# install nfs.conf and enable udp proto
	install -m 0755 ${S}/nfs.conf ${D}${sysconfdir}
	sed -i -e 's/#\(\[nfsd\]\)/\1/' -e 's/#\( udp=\).*/\1y/' ${D}${sysconfdir}/nfs.conf

	# add initial exports file
	echo "# Initial exports for nfs" > ${D}${sysconfdir}/exports

	# Libdir here is hardcoded in other scripts.
	install -d -m 0755 ${D}/usr/lib/ocf/resource.d/platform/
	install -D -m 755 ${STX_METADATA_PATH}/${STX_SUBPATH0}/filesystem-scripts-1.0/nfsserver-mgmt \
		${D}/usr/lib/ocf/resource.d/platform/nfsserver-mgmt
	
	install -p -D -m 755 ${STX_METADATA_PATH}/${STX_SUBPATH0}/filesystem-scripts-1.0/nfs-mount ${D}/${bindir}/nfs-mount
	install -D -m 755 ${STX_METADATA_PATH}/${STX_SUBPATH0}/filesystem-scripts-1.0/uexportfs.service \
			${D}/${systemd_system_unitdir}/uexportfs.service


	install -d ${D}/${sysconfdir}/init.d
	install -d ${D}/${systemd_system_unitdir}

	install -m 755 -p -D ${STX_METADATA_PATH}/${STX_SUBPATH1}/files/nfscommon   	 ${D}/${sysconfdir}/init.d
        install -m 644 -p -D ${STX_METADATA_PATH}/${STX_SUBPATH1}/files/nfscommon.service  	 ${D}/${systemd_system_unitdir}/

        install -m 755 -p -D ${STX_METADATA_PATH}/${STX_SUBPATH1}/files/nfsserver            ${D}/${sysconfdir}/init.d
        install -m 644 -p -D ${STX_METADATA_PATH}/${STX_SUBPATH1}/files/nfsserver.service    ${D}/${systemd_system_unitdir}
        install -m 644 -p -D ${STX_METADATA_PATH}/${STX_SUBPATH1}/files/nfsmount.conf        ${D}/${sysconfdir}/nfsmount.conf

}

FILES_${PN}-config = "\
	${systemd_system_unitdir}/nfscommon.service \
	${systemd_system_unitdir}/nfsserver.service \
	${systemd_system_unitdir}/uexportfs.service \
	${sysconfdir}/nfsmount.conf \
	"
FILES_${PN}_append = " usr/lib/ocf/resource.d"
