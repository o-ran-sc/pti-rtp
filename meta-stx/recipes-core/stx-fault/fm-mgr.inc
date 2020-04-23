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

PACKAGES += " fm-mgr"

###################
# DEPENDS = " fm-common"
###################

do_compile_append() {
	cd ${S}/fm-mgr/sources/
	oe_runmake -e \
		LDFLAGS="${LDFLAGS} -L${S}/fm-common/sources" \
		CCFLAGS="${CXXFLAGS}" \
		INCLUDES="-I. -I${S}/fm-common/sources" \
		build
}

do_install_append () {
	cd ${S}/fm-mgr/sources/
	install -d -m0755 ${D}/${systemd_system_unitdir} 
	oe_runmake -e DESTDIR=${D} BINDIR=${bindir} \
		LIBDIR=${libdir} UNITDIR=${systemd_system_unitdir} \
			SYSCONFDIR=${sysconfdir} \
			install
	rm -rf ${D}/usr/lib/systemd

	# fix the path for init scripts
	sed -i -e 's|rc.d/||' ${D}/${systemd_system_unitdir}/*.service

	# fix the path for binaries
	sed -i -e 's|/usr/local/bin/|${bindir}/|' ${D}${sysconfdir}/init.d/fminit
}

FILES_fm-mgr = "  \
	${bindir}/fmManager \
	${systemd_system_unitdir}/fminit.service \
	${sysconfdir}/init.d/fminit \
	${sysconfdir}/logrotate.d/fm.logrotate \
	"
