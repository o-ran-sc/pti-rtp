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

PACKAGES += " update-motd"

RDEPENDS_update-motd  += " cronie bash"
DESCRIPTION_update-motd  = "dynamic MOTD generation"
SUMMARY_update-motd  = "dynamic MOTD generation"

inherit setuptools distutils

do_configure_append() {
	:
}
do_compile_append() {
	:
}

do_install_append() {
	cd ${S}/utilities/update-motd/files


	install -d ${D}/${sbindir}
	install -m 700 motd-update ${D}/${sbindir}/motd-update

	install -m0755 -d ${D}/${sysconfdir}
	install -m0755 -d ${D}/${sysconfdir}/motd.d

	install -m 755 motd-header ${D}/${sysconfdir}/motd.d/00-header
	install -m 755 motd-footer ${D}/${sysconfdir}/motd.d/99-footer
	install -m 644 motd.head ${D}/${sysconfdir}/motd.d/motd.head

	install -m0755  -d ${D}/${sysconfdir}/cron.d
	install -m 600 motd-update.cron ${D}/${sysconfdir}/cron.d/motd-update
	install -m 700 customize-banner ${D}/${sbindir}/customize-banner
	install -m 700 apply_banner_customization ${D}/${sbindir}/apply_banner_customization
	install -m 700 install_banner_customization ${D}/${sbindir}/install_banner_customization

}

FILES_update-motd = "  \
	${sbindir}/motd-update \
	${sysconfdir}/motd.d/00-header \
	${sysconfdir}/motd.d/99-footer \
	${sysconfdir}/motd.d/motd.head \
	${sysconfdir}/cron.d/motd-update \
	${sbindir}/customize-banner \
	${sbindir}/apply_banner_customization \
	${sbindir}/install_banner_customization \
	"
