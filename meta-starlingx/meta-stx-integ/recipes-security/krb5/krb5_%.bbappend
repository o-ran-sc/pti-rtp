

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

#SRC_URI_append = "file://soname_majversion.diff"
DEPENDS += "libverto"

PACKAGES_remove = "libverto"

RDEPENDS_${PN} += "\
        libverto \
"
RDEPENDS_krb5-admin-server += "\
        libverto \
"
RDEPENDS_krb5-kdc += "\
        libverto \
"
