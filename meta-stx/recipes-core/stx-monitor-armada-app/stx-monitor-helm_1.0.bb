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

SUMMARY = "StarlingX Monitor Application Armada Helm Charts"
DESCRIPTION = "StarlingX Monitor Application Armada Helm Charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += " \
    monitor-helm \
    monitor-helm-elastic \
"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV = "e5ee6b3a07b74479b93fe90eff0662cf81890f73"

SRC_URI = "git://opendev.org/starlingx/monitor-armada-app.git;protocol=${PROTOCOL};branch=${BRANCH}"

S = "${WORKDIR}/git/stx-monitor-helm/stx-monitor-helm"

inherit allarch

helm_folder = "${nonarch_libdir}/helm"
armada_folder = "${nonarch_libdir}/armada"
app_folder = "${nonarch_libdir}/application"

do_configure () {
	:
}

do_compile () {
	:
}

do_install () {
	install -d -m 755 ${D}${armada_folder}
	install -p -D -m 755 ${S}/manifests/*.yaml ${D}${armada_folder}
	install -d -m 755 ${D}${app_folder}
	install -p -D -m 755 ${S}/files/metadata.yaml ${D}${app_folder}/monitor_metadata.yaml
}

FILES_${PN} = " \
    ${app_folder} \
    ${armada_folder} \
"

RDEPENDS_${PN} = " \
    helm \
    monitor-helm \
    monitor-helm-elastic \
"
