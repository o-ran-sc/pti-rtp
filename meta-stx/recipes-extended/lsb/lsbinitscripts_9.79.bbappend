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

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://service-redirect-to-restart-for-reload.patch"

DEPENDS += "\
    gettext-native \
    glib-2.0 \
    popt \
"

unset do_configure[noexec]
unset do_compile[noexec]

do_install_append() {
	install -m 0755 -d ${D}/${sysconfdir}/profile.d/
	install -m 0644 ${S}/lang.sh  ${D}${sysconfdir}/profile.d/lang.sh
	install -m 0755 -d ${D}/${base_sbindir}
	install -m 0755 ${S}/src/consoletype ${D}/${base_sbindir}

	install -m 0755 -d ${D}/${bindir}
	install -m 0755 ${S}/service ${D}/${bindir}
	sed -i -e 's|${bindir}|${base_bindir}|' ${D}/${bindir}/service
}

FILES_${PN}_append = "${sysconfdir}/profile.d/lang.sh"
