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

DESCRIPTION = " \
A shell script to tell if your system is vulnerable against the several \
\"speculative execution\" CVEs that were made public since 2018. \
"

SUMMARY = "Spectre and Meltdown Checker"
HOMEPAGE = "https://github.com/speed47/spectre-meltdown-checker"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://spectre-meltdown-checker.sh;beginline=1;endline=5;md5=0113e62a200ec9a5f5ebdd7ad4329133"

SRCREV = "3d21dae16864f8e8262d7a35bd4de300452b274d"
SRCNAME = "spectre-meltdown-checker"
BRANCH = "master"
PROTOCOL = "https"
PV = "v0.43+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/speed47/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

do_install() {
	install -d -p -m 0755 ${D}/${sbindir} 
	install -m 0644 ${S}/${SRCNAME}.sh ${D}/${sbindir}/${SRCNAME}.sh
}

FILES_${PN} = "${sbindir}/"
