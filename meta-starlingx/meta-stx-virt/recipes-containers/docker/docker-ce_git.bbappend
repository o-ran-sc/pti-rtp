inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "docker-config"

LICENSE_append = "& Apache-2.0"
LIC_FILES_CHKSUM += "\
	file://${STX_METADATA_PATH}/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
	"

RDEPENDS_${PN}_append = " logrotate"

do_install_append () {
    rm -f ${D}${sysconfdir}/docker
    install -d -m 0755 ${D}${sysconfdir}/docker
    install -d -m 0755 ${D}/${sysconfdir}/pmon.d 
    install -d -m 0755 ${D}/${sysconfdir}/systemd/system/docker.service.d 
    
    install -D -m 644 ${STX_METADATA_PATH}/files/docker-pmond.conf ${D}/${sysconfdir}/pmon.d/docker.conf
    
    install -D -m 644 ${STX_METADATA_PATH}/files/docker-stx-override.conf \
    	${D}/${sysconfdir}/systemd/system/docker.service.d/docker-stx-override.conf
    install -D -m 644 ${STX_METADATA_PATH}/files/docker.logrotate ${D}/${sysconfdir}/logrotate.d/docker.logrotate
}
