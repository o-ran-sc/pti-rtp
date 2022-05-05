
DESCRIPTION = "Non-interactive ssh password auth"
HOMEPAGE = "http://sshpass.sourceforge.net/"
SECTION = "console/network"
LICENSE = "GPLv2"

# SRC_URI = "${SOURCEFORGE_MIRROR}/sshpass/sshpass-${PV}.tar.gz"
SRC_URI = "https://fossies.org/linux/privat/sshpass-${PV}.tar.gz"

INC_PR = "r0"

inherit autotools
