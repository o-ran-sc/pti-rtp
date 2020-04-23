#
## Copyright (C) 2019 Wind River Systems, Inc.
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

DESCRIPTION = "YAML 1.2 loader/dumper package for Python"
HOMEPAGE = "https://pypi.org/project/ruamel.yaml/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=84e9d7d823d2abac052e70de2051ca1c"

SRC_URI[md5sum] = "d53315f8ccb93748d00ccca39486ac78"
SRC_URI[sha256sum] = "350496f6fdd8c2bb17a0fa3fd2ec98431280cf12d72dae498b19ac0119c2bbad"

inherit setuptools pypi python-dir

DEPENDS += " \
	${PYTHON_PN}-native \
	${PYTHON_PN}-cryptography-native \
	"
RDEPENDS_${PN}_append  = " \
	${PYTHON_PN}-ruamel.ordereddict \
	"

do_install_prepend() {
	export RUAMEL_NO_PIP_INSTALL_CHECK=1
}

