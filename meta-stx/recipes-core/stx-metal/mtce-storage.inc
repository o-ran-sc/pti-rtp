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

PACKAGES += " mtce-storage"

RDEPENDS_mtce-storage_append = " \
	bash \
	systemd \
	"

do_configure_prepend () {
	:
} 

do_compile_prepend () {
	:
}

do_install_prepend () {
	cd ${S}/mtce-storage/src/
	oe_runmake buildroot=${D} \
		_sysconfdir=${sysconfdir} _unitdir=${systemd_system_unitdir} _datarootdir=${datadir} \
		install

}

pkg_postinst_ontarget_mtce-storage() { 
	${base_bindir}/systemctl enable goenabled-storage.service
}

FILES_mtce-storage = " \
	${datadir}/licenses/mtce-storage-1.0/LICENSE \
	${systemd_system_unitdir}/goenabled-storage.service \
	${sysconfdir}/init.d/goenabledStorage \
	"
