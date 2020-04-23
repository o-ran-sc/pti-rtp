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

PACKAGES += " build-info"

DESCRIPTION_build-info  = "Build Info"
SUMMARY_update-motd  = "Build Info"

SRC_URI += "file://build.info"

do_install_append() {
	install -d ${D}/${sysconfdir}
	install -m 644 ${WORKDIR}/build.info ${D}/${sysconfdir}
	sed -i -e "s/@OS@/${DISTRO}/" \
	       -e "s/@STX_RELEASE@/${STX_REL}/" \
	       -e "s/@STX_ID@/${STX_ID}/" \
	       -e "s/@BUILD_DATE@/${STX_BUILD_DATE}/" \
	       ${D}/${sysconfdir}/build.info

}

do_install[vardepsexclude] += "STX_BUILD_DATE"

FILES_build-info = "${sysconfdir}/build.info"
