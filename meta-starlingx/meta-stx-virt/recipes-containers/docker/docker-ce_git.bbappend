SRCREV_FORMAT = "opendev"
SRCREV_opendev = "d778e862571957ece3c404c0c37d325769772fde"
SUBPATH0 = "docker-config"
DSTSUFX0 = "stx-configfiles"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://stx-configfiles-LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

SRC_URI += " \
	git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX0};branch="r/stx.3.0";subpath=${SUBPATH0};name=opendev \
	"

RDEPENDS_${PN}_append = " logrotate"

do_unpack_append() {
    bb.build.exec_func('do_copy_config_files', d)
}

do_copy_config_files () {
    cp -f ${WORKDIR}/${DSTSUFX0}/files/LICENSE ${S}/stx-configfiles-LICENSE
}

do_install_append () {
    rm -f ${D}${sysconfdir}/docker
    install -d -m 0755 ${D}${sysconfdir}/docker
    install -d -m 0755 ${D}/${sysconfdir}/pmon.d 
    install -d -m 0755 ${D}/${sysconfdir}/systemd/system/docker.service.d 
    
    install -D -m 644 ${WORKDIR}/${DSTSUFX0}/files/docker-pmond.conf ${D}/${sysconfdir}/pmon.d/docker.conf
    
    install -D -m 644 ${WORKDIR}/${DSTSUFX0}/files/docker-stx-override.conf \
    	${D}/${sysconfdir}/systemd/system/docker.service.d/docker-stx-override.conf
    install -D -m 644 ${WORKDIR}/${DSTSUFX0}/files/docker.logrotate ${D}/${sysconfdir}/logrotate.d/docker.logrotate
}
