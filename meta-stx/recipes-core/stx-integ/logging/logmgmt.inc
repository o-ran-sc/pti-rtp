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

PACKAGES += " logmgmt"

do_configure_append () {
	cd ${S}/logging/logmgmt/logmgmt
	distutils_do_configure
} 

do_compile_append () {
	cd ${S}/logging/logmgmt/logmgmt
	distutils_do_compile
}

do_install_append () {
	cd ${S}/logging/logmgmt/logmgmt
	distutils_do_install
	cd ${S}/logging/logmgmt/
	install -d -m 755 ${D}/${bindir}
	install -p -D -m 700 scripts/bin/logmgmt ${D}/${bindir}/logmgmt
	install -p -D -m 700 scripts/bin/logmgmt_postrotate ${D}/${bindir}/logmgmt_postrotate
	install -p -D -m 700 scripts/bin/logmgmt_prerotate ${D}/${bindir}/logmgmt_prerotate

	install -d -m 755 ${D}/${sysconfdir}/init.d
	install -p -D -m 700 scripts/init.d/logmgmt ${D}/${sysconfdir}/init.d/logmgmt

	install -d -m 755 ${D}/${sysconfdir}/pmon.d/
	install -p -D -m 644 scripts/pmon.d/logmgmt ${D}/${sysconfdir}/pmon.d/logmgmt

	install -p -D -m 664 scripts/etc/systemd/system/logmgmt.service ${D}/${systemd_system_unitdir}/logmgmt.service
}


#pkg_postinst_ontarget_logmgmt () { 
#	/usr/bin/systemctl enable logmgmt.service >/dev/null 2>&1
#}

FILES_logmgmt_append = " \
	${bindir}/logmgmt \
	${bindir}/logmgmt_postrotate \
	${bindir}/logmgmt_prerotate \
	${sysconfdir}/init.d/logmgmt \
	${sysconfdir}/pmon.d/logmgmt \
	${systemd_system_unitdir}/logmgmt.service \
	${libdir}/python2.7/site-packages/logmgmt-1.0.0-py2.7.egg-info \
	${libdir}/python2.7/site-packages/logmgmt \
	${libdir}/python2.7/site-packages/logmgmt-1.0.0-py2.7.egg-info/PKG-INFO \
	${libdir}/python2.7/site-packages/logmgmt-1.0.0-py2.7.egg-info/top_level.txt \
	${libdir}/python2.7/site-packages/logmgmt-1.0.0-py2.7.egg-info/dependency_links.txt \
	${libdir}/python2.7/site-packages/logmgmt-1.0.0-py2.7.egg-info/SOURCES.txt \
	${libdir}/python2.7/site-packages/logmgmt/prepostrotate.pyc \
	${libdir}/python2.7/site-packages/logmgmt/__init__.pyc \
	${libdir}/python2.7/site-packages/logmgmt/logmgmt.pyc \
	${libdir}/python2.7/site-packages/logmgmt/__init__.py \
	${libdir}/python2.7/site-packages/logmgmt/logmgmt.py \
	${libdir}/python2.7/site-packages/logmgmt/prepostrotate.py \
	"
