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

FILESEXTRAPATHS_prepend := "${THISDIR}/files/python-pynacl:"
DESCRIPTION = "Python binding to libsodiom"

HOMEPAGE = "https://pypi.org/project/PyNaCl/"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8cc789b082b3d97e1ccc5261f8594d3f"

SRCREV = "4881c878c9a33f4684337f650355bdf7f031d77d"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = " \
	git://github.com/pyca/pynacl.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://${PN}/0001-Enable-cross-compile.patch \
	"


inherit setuptools distutils

DEPENDS += " libsodium python-cffi-native libsodium-native"
RDEPENDS_${PN} = " libsodium"

do_compile_prepend() {
	export PYNACL_CROSS_BUILD="--build=${BUILD_SYS}"
	export PYNACL_CROSS_HOST="--host=${HOST_SYS}"
	export PYNACL_CROSS_TARGET="--target=${TARGET_SYS}"
	export SODIUM_INSTALL="system"
}

do_install_prepend() {
	export SODIUM_INSTALL="system"
}
