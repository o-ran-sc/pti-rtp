DESCRIPTION = "StarlingX KVM Timer Advance Package"

inherit stx-metadata

STX_REPO = "integ"
STX_SUBPATH = "virt/kvm-timer-advance"

PV = "1.0.0"

LICENSE = "Apache-2.0 & GPL-2.0"
LIC_FILES_CHKSUM = "file://${STX_METADATA_PATH}/files/LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

RDEPENDS_${PN}_append = " \
	systemd \
	bash \
	"

inherit setuptools systemd
SYSTEMD_PACKAGES += " ${PN}"
SYSTEMD_SERVICE_${PN} = "kvm_timer_advance_setup.service"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
	install -p -D -m 0755 ${STX_METADATA_PATH}/files/setup_kvm_timer_advance.sh \
			${D}/${bindir}/setup_kvm_timer_advance.sh
	install -p -D -m 444 ${STX_METADATA_PATH}/files/kvm_timer_advance_setup.service \
			${D}/${systemd_system_unitdir}/kvm_timer_advance_setup.service
}
