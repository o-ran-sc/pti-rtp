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

SUMMARY = "StarlingX Platform Helm charts"
DESCRIPTION = "StarlingX Platform Helm charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += " \
    helm-native \
    openstack-helm \
    openstack-helm-infra \
"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV_platform-armada-app = "c67d1eeb605ea1da4ebb2a1219a6f54f05e3eb5e"
SRCREV_helm-charts = "c01426a2500269fbf1a781214a361de0796297d1"

SRC_URI = " \
    git://opendev.org/starlingx/platform-armada-app.git;protocol=${PROTOCOL};branch=${BRANCH};name=platform-armada-app \
    git://opendev.org/starlingx/helm-charts.git;protocol=${PROTOCOL};branch=${BRANCH};name=helm-charts;destsuffix=helm-charts \
"

S = "${WORKDIR}/git/stx-platform-helm/stx-platform-helm"

inherit allarch

toolkit_version = "0.1.0"
helm_folder = "${RECIPE_SYSROOT}${nonarch_libdir}/helm"
helm_repo = "stx-platform"

app_name = "platform-integ-apps"
app_staging = "${B}/staging"
app_tarball = "${app_name}.tgz"
app_folder = "/usr/local/share/applications/helm"

do_configure () {
	:
}

do_compile () {
	# initialize helm and build the toolkit
	# helm init --client-only does not work if there is no networking
	# The following commands do essentially the same as: helm init
	export HOME="${B}/${USER}"
	export helm_home="${B}/${USER}/.helm"
	rm -rf ${helm_home}

	mkdir  -p ${helm_home}
	mkdir  ${helm_home}/repository
	mkdir  ${helm_home}/repository/cache
	mkdir  ${helm_home}/repository/local
	mkdir  ${helm_home}/plugins
	mkdir  ${helm_home}/starters
	mkdir  ${helm_home}/cache
	mkdir  ${helm_home}/cache/archive

	# Stage a repository file that only has a local repo
	cp ${S}/files/repositories.yaml ${helm_home}/repository/repositories.yaml

	# Stage a local repo index that can be updated by the build
	cp ${S}/files/index.yaml ${helm_home}/repository/local/index.yaml

	# Stage helm-toolkit in the local repo
	cp ${helm_folder}/helm-toolkit-${toolkit_version}.tgz ${S}/helm-charts/

	# Host a server for the charts
	helm serve --repo-path . &
	helm repo rm local
	helm repo add local http://localhost:8879/charts

	# Make the charts. These produce a tgz file
	cp -rf ${WORKDIR}/helm-charts/node-feature-discovery/node-feature-discovery/helm-charts/node-feature-discovery/ \
		${S}/helm-charts/
	cd ${S}/helm-charts
	make rbd-provisioner
	make ceph-pools-audit
	make node-feature-discovery
	cd -

	# Terminate helm server
	pid=`/bin/pidof helm`
	kill ${pid}
	rm -rf ${helm_home}

	# Create a chart tarball compliant with sysinv kube-app.py
	# Setup staging
	mkdir -p ${app_staging}
	cp ${S}/files/metadata.yaml ${app_staging}
	cp ${S}/manifests/manifest.yaml ${app_staging}

	mkdir -p ${app_staging}/charts
	cp ${S}/helm-charts/*.tgz ${app_staging}/charts
	cd ${app_staging}

	# Populate metadata
	sed -i 's/@APP_NAME@/${app_name}/g' ${app_staging}/metadata.yaml
	sed -i 's/@APP_VERSION@/${version}-${tis_patch_ver}/g' ${app_staging}/metadata.yaml
	sed -i 's/@HELM_REPO@/${helm_repo}/g' ${app_staging}/metadata.yaml

	# package it up
	find . -type f ! -name '*.md5' -print0 | xargs -0 md5sum > checksum.md5
	tar -zcf ${B}/${app_tarball} -C ${app_staging}/ .

	# Cleanup staging
	rm -fr ${app_staging}
}

do_install () {
	install -d -m 755 ${D}/${app_folder}
	install -p -D -m 755 ${B}/${app_tarball} ${D}/${app_folder}
	install -d -m 755 ${D}/opt/extracharts
	install -p -D -m 755 ${S}/helm-charts/node-feature-discovery-*.tgz ${D}/opt/extracharts
}

FILES_${PN} = " \
    /opt/extracharts \
    ${app_folder} \
"

RDEPENDS_${PN} = " \
    helm \
    openstack-helm \
    openstack-helm-infra \
"
