DESCRIPTION = " \
A client library in Python for Ceph Mgr RESTful plugin providing REST API \
access to the cluster over an SSL-secured connection. Python API is compatible \
with the old Python Ceph client at \
https://github.com/dmsimard/python-cephclient that no longer works in Ceph \
mimic because Ceph REST API component was removed. \
"

require utilities-common.inc
SUBPATH0 = "ceph/python-cephclient/python-cephclient"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=41687b590435621fc0676ac02c51154f"

RDEPENDS_${PN}_append = " \
	python \
	python-ipaddress \
	python-six \
	python-requests \
	"

inherit setuptools

do_configure_prepend() {
	rm -rf .pytest_cache
	rm -rf python_cephclient.egg-info
	rm -f requirements.txt
}
