DESCRIPTION = "cloud-provider-openstack"

STABLE = "starlingx/master"
PROTOCOL = "https"
BRANCH = "master"
SRCREV = "70609a3d55e5b7d2be82667fc35792505f9013c4"
S = "${WORKDIR}/git"
PV = "19.05"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://opendev.org/starlingx/config.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

RDEPENDS_${PN} += " bash"

inherit systemd
SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_SERVICE_${PN} = "${PN}.service"
# SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {

	cd ${S}/filesystem/cloud-provider-openstack/
	install -d -m755 ${D}/${bindir}
	install -d -m755 ${D}/${systemd_system_unitdir}

	install -D -m644 files/cloud-provider-openstack.sh ${D}/${bindir}
	install -D -m644 files/cloud-provider-openstack.service ${D}/${systemd_system_unitdir}


}
