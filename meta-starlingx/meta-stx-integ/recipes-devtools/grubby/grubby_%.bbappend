
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://1000-Generic-name-for-Titanium.patch \
	file://1001-Add-support-for-updating-grub-cfg-with-multiboot-2.patch \
	file://1002-Install-into-libdir-instead-of-hard-coding.patch \
	"

FILES_${PN}_append = " ${libdir}"
