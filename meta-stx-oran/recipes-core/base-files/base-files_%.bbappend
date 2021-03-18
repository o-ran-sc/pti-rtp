#
## Copyright (C) 2021 Wind River Systems, Inc.
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

SUBPATH1 = "centos-release-config"
DSTSUFX1 = "stx-release-config"

SRC_URI += " \
        git://opendev.org/starlingx/config-files.git;protocol=https;destsuffix=${DSTSUFX1};branch="r/stx.3.0";subpath=${SUBPATH1};name=opendev \
        "

do_install_append() {
    install -m 644 ${WORKDIR}/${DSTSUFX1}/files/issue ${D}/${sysconfdir}/issue
    install -m 644 ${WORKDIR}/${DSTSUFX1}/files/issue.net ${D}/${sysconfdir}/issue.net

    sed -i -e 's/@PLATFORM_RELEASE@/${ORAN_REL}/' ${D}${sysconfdir}/issue*
}
