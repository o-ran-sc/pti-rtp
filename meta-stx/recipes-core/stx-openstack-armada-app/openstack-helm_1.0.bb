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

SUMMARY = "Openstack Helm charts"
DESCRIPTION = "Openstack Helm charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += " \
    helm-native \
    openstack-helm-infra \
"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV_openstack-helm = "82c72367c85ca94270f702661c7b984899c1ae38"
SRCREV_openstack-armada-app = "863f4b9733d3d4f4fd490606a94b84cfdaf2df2c"

SRC_URI = " \
    git://github.com/openstack/openstack-helm;protocol=${PROTOCOL};name=openstack-helm \
    git://opendev.org/starlingx/openstack-armada-app;protocol=${PROTOCOL};branch=${BRANCH};name=openstack-armada-app;destsuffix=openstack-armada-app \
"

S = "${WORKDIR}/git"

inherit allarch

patch_folder = "${WORKDIR}/openstack-armada-app/openstack-helm/files"
helm_folder = "${nonarch_libdir}/helm"
toolkit_version = "0.1.0"
helmchart_version = "0.1.0"

do_patch () {
	cd ${S}
	git am ${patch_folder}/0001-Ceilometer-chart-add-the-ability-to-publish-events-t.patch
	git am ${patch_folder}/0002-Remove-stale-Apache2-service-pids-when-a-POD-starts.patch
	git am ${patch_folder}/0003-Nova-console-ip-address-search-optionality.patch
	git am ${patch_folder}/0004-Nova-chart-Support-ephemeral-pool-creation.patch
	git am ${patch_folder}/0005-Nova-Add-support-for-disabling-Readiness-Liveness-pr.patch
	git am ${patch_folder}/0006-Add-Placement-Chart.patch
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

	# Stage helm-toolkit in the local repo
	cp ${RECIPE_SYSROOT}${helm_folder}/helm-toolkit-${toolkit_version}.tgz .

	# Host a server for the charts
	helm serve --repo-path . &
	helm repo rm local
	helm repo add local http://localhost:8879/charts

	# Make the charts. These produce a tgz file
	make aodh
	make barbican
	make ceilometer
	make cinder
	make glance
	make heat
	make horizon
	make ironic
	make keystone
	make magnum
	make neutron
	make nova
	make panko
	make placement

	# terminate helm server
	pid=`/bin/pidof helm`
	kill ${pid}
	rm -rf ${helm_home}

	# Remove the helm-toolkit tarball
	rm helm-toolkit-${toolkit_version}.tgz
}

do_install () {
	install -d -m 755 ${D}${helm_folder}
	install -p -D -m 755 ${B}/*.tgz ${D}${helm_folder}
}

FILES_${PN} = "${helm_folder}"

RDEPENDS_${PN} = " \
    helm \
    openstack-helm-infra \
"
