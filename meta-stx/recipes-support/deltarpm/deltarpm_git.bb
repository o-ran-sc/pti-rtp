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

DESCRIPTION = "Makedeltarpm creates a deltarpm from two rpms. The deltarpm can \
		later be used to recreate the new rpm from either filesystem data \
		or the old rpm. Use the -v option to make makedeltarpm more verbose \
		about its work (use it twice to make it even more verbose).\
		"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.BSD;md5=574af071cf0d60a71cb781844bbe2d76"

SRCREV = "c5e0ca7482e2cfea5e4d902ffe488e0a71ed3e67"
# SRCREV = "8660d976f5d2b73adf1088d67341be9c3646f2f2"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/rpm-software-management/deltarpm.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
		file://0001-Makefile-patch-fix-build-errors.patch \
		"

DEPENDS += " python perl bzip2 rpm zlib python xz zstd"

do_compile_append () {
	cd ${S}
	oe_runmake -e DESTDIR=${D} bindir=${bindir} mandir=${mandir} python

}

do_install () {
	cd ${S}
	oe_runmake -e DESTDIR=${D} bindir=${bindir} mandir=${mandir} install
}

FILES_${PN}_append += " \
	/usr/lib/python2.7/site-packages/deltarpm.py \
	/usr/lib/python2.7/site-packages/_deltarpmmodule.so \
	"
