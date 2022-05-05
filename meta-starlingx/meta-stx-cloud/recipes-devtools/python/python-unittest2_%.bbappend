
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

DEPENDS_append = " \
	${PYTHON_PN}-traceback2-native \
	${PYTHON_PN}-six-native \
	"

SRC_URI += " \
	file://python-unittest2/0001-port-unittest2-argparse-is-part-of-stdlib.patch \
	"
