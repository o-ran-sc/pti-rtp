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
#

SUMMARY = " CherryPy is a pythonic, object-oriented HTTP framework"
DESCRIPTION = "\
It allows building web applications in much the same way one would build any \
other object-oriented program. This design results in less and more readable \
code being developed faster. It's all just properties and methods. \
It is now more than ten years old and has proven fast and very stable. \
It is being used in production by many sites, from the simplest to the most \
demanding. \
"

HOMEPAGE = "https://www.cherrypy.org/"
AUTHOR = "CherryPy Team"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://cherrypy/LICENSE.txt;md5=c187ff3653a0878075713adef2c545c3"

SRC_URI = "https://pypi.python.org/packages/source/C/CherryPy/CherryPy-${PV}.tar.gz"
SRC_URI[md5sum] = "c1b1e9577f65f9bb88bfd1b15b93b911"
SRC_URI[sha256sum] = "dc5a88562795c2ee462dac5b37aba1cf4f34f3e27281ec11049227039308b691"

S = "${WORKDIR}/CherryPy-${PV}"

inherit setuptools

FILES_${PN} += "\
    ${datadir}/cherrypy \
"
