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

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCNAME = "integ"
SRCREV = "0bf4b546df8c7fdec8cfc6cb6f71b9609ee54306"

SRC_URI += "git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH};destsuffix=stx-patches;subpath=base/cgcs-users/cgcs-users-1.0"

do_patch_append () {
    bb.build.exec_func('stx_do_patch', d)
}

stx_do_patch () {
	cd ${S}
	patch -p1 < ${WORKDIR}/stx-patches/ibsh-0.3e.patch
	patch -p1 < ${WORKDIR}/stx-patches/ibsh-0.3e-cgcs.patch
	patch -p1 < ${WORKDIR}/stx-patches/ibsh-0.3e-cgcs-copyright.patch
}

do_install_append() {
	cp ${WORKDIR}/stx-patches/admin.cmds ${D}/${sysconfdir}/ibsh/cmds/
	cp ${WORKDIR}/stx-patches/admin.xtns ${D}/${sysconfdir}/ibsh/xtns/
	cp ${WORKDIR}/stx-patches/operator.cmds ${D}/${sysconfdir}/ibsh/cmds/
	cp ${WORKDIR}/stx-patches/operator.xtns ${D}/${sysconfdir}/ibsh/xtns/
	cp ${WORKDIR}/stx-patches/secadmin.cmds ${D}/${sysconfdir}/ibsh/cmds/
	cp ${WORKDIR}/stx-patches/secadmin.xtns ${D}/${sysconfdir}/ibsh/xtns/
}

