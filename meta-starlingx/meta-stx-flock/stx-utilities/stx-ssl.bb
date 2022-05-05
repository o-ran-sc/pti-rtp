DESCRIPTION = " Wind River Security"

require utilities-common.inc
SUBPATH0 = "security/stx-ssl/"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"
RDEPENDS_${PN}_append = " bash"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {

	openssl req -new -x509 -sha256 \
		-keyout self-signed-server-cert.pem \
		-out self-signed-server-cert.pem \
		-days 365 -nodes \
		-config server-csr.conf

	install -p -d -m0755 ${D}/${sysconfdir}/ssl/private/
	install -m0400 self-signed-server-cert.pem \
		${D}/${sysconfdir}/ssl/private/self-signed-server-cert.pem 

	install -p -d -m0755 ${D}/${sbindir}
	install -m0700 ${S}/files/tpmdevice-setup ${D}/${sbindir}/tpmdevice-setup

}
