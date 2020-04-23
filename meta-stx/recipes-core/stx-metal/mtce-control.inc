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

PACKAGES += " mtce-control"

RDEPENDS_mtce-control += " \
	bash \
	systemd \
	lighttpd \
	qemu \
	"

do_configure_prepend () {
	:
} 

do_compile_prepend () {
	:
}

do_install_prepend () {
	cd ${S}/mtce-control/src/
	oe_runmake buildroot=${D} \
		_sysconfdir=${sysconfdir} _unitdir=${systemd_system_unitdir} _datarootdir=${datadir} \
		install

}

#pkg_postinst_ontarget_mtce-control () {
#	# /usr/bin/systemctl enable lighttpd.service
#	# /usr/bin/systemctl enable qemu_clean.service
#	/usr/bin/systemctl enable hbsAgent.service
#}

FILES_mtce-control = " \
	${datadir}/licenses/mtce-control-1.0/LICENSE \
	${systemd_system_unitdir}/hbsAgent.service \
	${sysconfdir}/pmon.d/hbsAgent.conf \
	${sysconfdir}/init.d/hbsAgent \
	${sysconfdir}/init.d/goenabledControl \
	"
