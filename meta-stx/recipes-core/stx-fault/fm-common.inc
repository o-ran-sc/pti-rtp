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

PACKAGES += " fm-common"
RDEPENDS_fm-common = " python"

do_configure_append () {
	cd ${S}/fm-common/sources

	# fix the hardcoded path
	sed -i -e 's|/usr/local/bin|${bindir}|' fmConstants.h

	distutils_do_configure
} 

do_compile_append() {
	cd ${S}/fm-common/sources
	oe_runmake -e BINDIR=${bindir} \
			INCLUDES="-I./ " \
			EXTRACCFLAGS=" " \
			CCFLAGS="${CXXFLAGS} ${CCSHARED}" \
			LIBDIR=${libdir} INCDIR=${includedir} \
			CGCS_DOC_DEPLOY=${cgcs_doc_deploy}
	distutils_do_compile
}

do_install_append() {
	cd ${S}/fm-common/sources
	oe_runmake -e DESTDIR=${D} BINDIR=${bindir} \
		LIBDIR=${libdir} INCDIR=${includedir} \
		CGCS_DOC_DEPLOY=${cgcs_doc_deploy} install
	distutils_do_install
}


FILES_fm-common = " \
	${cgcs_doc_deploy}/fmAlarm.h \
	${bindir}/fm_db_sync_event_suppression.py \
	${bindir}/fmClientCli \
	${libdir}/python2.7/site-packages/fm_core*.egg-info \
	${libdir}/python2.7/site-packages/fm_core.so \
	${libdir}/libfmcommon.so.1.0 \
	${libdir}/libfmcommon.so.1 \
	${includedir}/fmConfig.h \
	${includedir}/fmLog.h \
	${includedir}/fmThread.h \
	${includedir}/fmDbAPI.h \
	${includedir}/fmAPI.h \
	${includedir}/fmAlarm.h \
	"
