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

TRANSFORM_NAME = "s,grub,grub2,"
EXTRA_OECONF += "--program-transform-name=${TRANSFORM_NAME}"

do_install_append() {
    for file in ${D}${bindir}/grub2-* ${D}${sbindir}/grub2-*; do
        ln -sf $(basename ${file}) $(echo ${file}|sed 's/grub2/grub/')
    done
}

FILES_${PN}-editenv = "${bindir}/grub2-editenv"

pkg_postinst_ontarget_${PN}() {
	grub-mkconfig -o /boot/grub/grub.cfg
}
