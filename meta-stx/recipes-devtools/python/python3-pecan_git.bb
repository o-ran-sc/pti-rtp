#
# Copyright (C) 2019 Wind River Systems, Inc.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

DESCRIPTION = "WSGI object-dispatching web framework"

HOMEPAGE = "https://pypi.python.org/pypi/pecan/"
SECTION = "devel/python"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d846877d24bbb3d7a00a985c90378e8c"

SRCREV = "da15e06d783e2cf569b39ba506e68e4e1e85568d"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/pecan/pecan.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


inherit setuptools3 distutils3

RDEPENDS_${PN} = "python3-mako \
		  python3-six \
		  python3-logutils \
		  python3-webtest  \
		  "

do_install_append() {
        mv ${D}/${bindir}/gunicorn_pecan ${D}/${bindir}/gunicorn_pecan3
        mv ${D}/${bindir}/pecan ${D}/${bindir}/pecan3
}
