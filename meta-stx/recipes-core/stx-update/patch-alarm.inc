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

PACKAGES += " patch-alarm"
DESCRIPTION_patch-alarm = "TIS Platform Patching"
SUMMARY_patch-alarm = "Patch alarm management"

inherit setuptools

RDEPENDS_patch-alarm_append = " \
	bash \
	python \
	"

do_configure_append () {
	cd ${S}/patch-alarm/patch-alarm
	distutils_do_configure
} 

do_compile_append () {
	cd ${S}/patch-alarm/patch-alarm
	distutils_do_compile
}

do_install_append () {
	cd ${S}/patch-alarm/patch-alarm
	distutils_do_install

	cd ${S}/patch-alarm/

	install -m 755 -d ${D}/${bindir}
	install -m 755 -d ${D}/${sysconfdir}/init.d

	install -m 700 scripts/bin/patch-alarm-manager ${D}/${bindir}/
	install -m 700 scripts/init.d/patch-alarm-manager ${D}/${sysconfdir}/init.d/
}

FILES_patch-alarm = " \
	${libdir}/python2.7/site-packages/patch_alarm \
	${libdir}/python2.7/site-packages/patch_alarm*.egg-info \
	${bindir}/patch-alarm-manager \
	${sysconfdir}/init.d/patch-alarm-manager \
	"
