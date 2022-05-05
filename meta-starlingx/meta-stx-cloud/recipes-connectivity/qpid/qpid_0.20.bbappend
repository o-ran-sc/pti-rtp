
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://fix-compile-through-disable-cflag-werror.patch"


EXTRA_OEMAKE += " CPPFLAGS='-std=gnu++98 -Wno-error' LDFLAGS='-std=gnu++98 -Wno-error'"
TARGET_CC_ARCH += "${LDFLAGS}"
RDEPENDS_${PN} += " bash"
