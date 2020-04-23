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

SUMMARY = "Monitor Helm charts"
DESCRIPTION = "Monitor Helm charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += " \
    helm-native \
    monitor-helm-elastic \
"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV_helm-charts = "92b6289ae93816717a8453cfe62bad51cbdb8ad0"
SRCREV_monitor-armada-app = "e5ee6b3a07b74479b93fe90eff0662cf81890f73"

SRC_URI = " \
    git://github.com/helm/charts;protocol=${PROTOCOL};name=helm-charts \
    git://opendev.org/starlingx/monitor-armada-app.git;protocol=${PROTOCOL};branch=${BRANCH};name=monitor-armada-app;destsuffix=monitor-armada-app \
"

S = "${WORKDIR}/git"

inherit allarch

patch_folder = "${WORKDIR}/monitor-armada-app/monitor-helm/files"
helm_folder = "${nonarch_libdir}/helm"
helmchart_version = "0.1.0"

do_patch () {
	cd ${S}
	git am ${patch_folder}/0001-Add-Makefile-for-helm-charts.patch
	git am ${patch_folder}/0002-kibana-workaround-checksum-for-configmap.yaml.patch
	git am ${patch_folder}/0003-helm-chart-changes-for-stx-monitor.patch
	git am ${patch_folder}/0004-ipv6-helm-chart-changes.patch
	git am ${patch_folder}/0005-decouple-config.patch
	git am ${patch_folder}/0006-add-system-info.patch
	git am ${patch_folder}/0007-three-masters.patch
	git am ${patch_folder}/0008-Update-stx-monitor-for-kubernetes-API-1.16.patch
	git am ${patch_folder}/0009-add-curator-as-of-2019-10-10.patch
	git am ${patch_folder}/0010-Update-kube-state-metrics-1.8.0-to-commit-09daf19.patch
	git am ${patch_folder}/0011-update-init-container-env-to-include-node-name.patch
	git am ${patch_folder}/0012-Add-imagePullSecrets.patch
	git am ${patch_folder}/0013-removed-unused-images.patch
}

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

	mkdir -p ${helm_home}
	mkdir ${helm_home}/repository
	mkdir ${helm_home}/repository/cache
	mkdir ${helm_home}/repository/local
	mkdir ${helm_home}/plugins
	mkdir ${helm_home}/starters
	mkdir ${helm_home}/cache
	mkdir ${helm_home}/cache/archive

	# Stage a repository file that only has a local repo
	install -m 0644 ${patch_folder}/repositories.yaml ${helm_home}/repository/repositories.yaml

	# Stage a local repo index that can be updated by the build
	install -m 0644 ${patch_folder}/index.yaml ${helm_home}/repository/local/index.yaml

	# Host a server for the charts
	helm serve --repo-path . &
	helm repo rm local
	helm repo add local http://localhost:8879/charts

	# Create the tgz files
	cd stable
	make filebeat
	make metricbeat
	make kube-state-metrics
	make kibana
	make nginx-ingress
	make logstash
	make elasticsearch-curator

	# terminate helm server
	pid=`/bin/pidof helm`
	kill ${pid}
	rm -rf ${helm_home}
}

do_install () {
	install -d -m 755 ${D}${helm_folder}
	install -p -D -m 755 ${S}/stable/*.tgz ${D}${helm_folder}
}

FILES_${PN} = "${helm_folder}"

RDEPENDS_${PN} = " \
    helm \
    monitor-helm-elastic \
"
