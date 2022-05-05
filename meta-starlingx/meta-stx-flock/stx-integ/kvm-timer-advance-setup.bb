DESCRIPTION = "StarlingX KVM Timer Advance Package"

STABLE = "starlingx/master"
PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCNAME = "integ"
SRCREV = "0bf4b546df8c7fdec8cfc6cb6f71b9609ee54306"
S = "${WORKDIR}/git"
PV = "1.0.0"

LICENSE = "Apache-2.0 & GPL-2.0"
LIC_FILES_CHKSUM = "file://virt/kvm-timer-advance/files/LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

RDEPENDS_${PN}_append = " \
	systemd \
	bash \
	"


SRC_URI = "git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	"
inherit setuptools systemd
SYSTEMD_PACKAGES += " ${PN}"
SYSTEMD_SERVICE_${PN} = "kvm_timer_advance_setup.service"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
	
# kvm-timer-advance

	install -p -D -m 0755 ${S}/virt/kvm-timer-advance/files/setup_kvm_timer_advance.sh \
			${D}/${bindir}/setup_kvm_timer_advance.sh
	install -p -D -m 444 ${S}/virt/kvm-timer-advance/files/kvm_timer_advance_setup.service \
			${D}/${systemd_system_unitdir}/kvm_timer_advance_setup.service

}
