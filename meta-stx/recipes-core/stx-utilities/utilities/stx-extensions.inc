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

PACKAGES += " stx-extensions"

RDEPENDS_stx-extensions  += " systemd"
DESCRIPTION_stx-extensions  = "TIS Extensions to thirdparty pkgs"
SUMMARY_stx-extensions  = "TIS Extensions to thirdparty pkgs"

inherit setuptools distutils

do_configure_append() {
	:
}
do_compile_append() {
	:
}

do_install_append() {
	cd ${S}/utilities/stx-extensions/files


	install -p -d -m0755 ${D}/${sysconfdir}/sysctl.d
	install -m0755 coredump-sysctl.conf ${D}/${sysconfdir}/sysctl.d/50-coredump.conf

	install -p -d -m0755 ${D}/${sysconfdir}/systemd/coredump.conf.d
	install -m0755 coredump.conf ${D}/${sysconfdir}/systemd/coredump.conf.d/coredump.conf

	install -p -d -m0755 ${D}/${sysconfdir}/modules-load.d
	install -m0644 modules-load-vfio.conf ${D}/${sysconfdir}/modules-load.d/vfio.conf

}

FILES_stx-extensions = "  \
	${sysconfdir}/sysctl.d/50-coredump.conf \
	${sysconfdir}/systemd/coredump.conf.d/coredump.conf \
	${sysconfdir}/modules-load.d/vfio.conf \
	"
