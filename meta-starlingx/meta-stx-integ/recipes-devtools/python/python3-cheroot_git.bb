
SUMMARY = "Cheroot is the high-performance, pure-Python HTTP server used by CherryPy."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=beeffd9dfcc746ed5a91921f1acc2746"

SRCREV = "c7ca7ff0bcebb53e1bed783280a3bb5db35f900f"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "7.0.0"

SRC_URI = "git://github.com/cherrypy/cheroot.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " \
	python3 \
	python3-setuptools-scm-native \
	python3-setuptools-scm-git-archive-native \
	"

RDEPENDS_${PN} += " \
	python3-email \
	python3-fcntl \
	python3-io \
	python3-logging \
	python3-unixadmin \
	python3-pyopenssl \
	python3-six \
	"

inherit setuptools3 distutils3 pkgconfig
