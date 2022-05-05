
SUMMARY = "Monitor Helm Elastic charts"
DESCRIPTION = "Monitor Helm Elastic charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += " \
    helm-native \
    stx-openstack-helm \
"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV_helm-charts-elastic = "2bd7616ceddbdf2eee88965e2028ee37d304c79c"

# Patches pulled from:
# SRCREV_monitor-armada-app = "e5ee6b3a07b74479b93fe90eff0662cf81890f73"
# git://opendev.org/starlingx/monitor-armada-app.git

SRC_URI = " \
    git://github.com/elastic/helm-charts;protocol=${PROTOCOL};name=helm-charts-elastic \
    file://0001-add-makefile.patch \
    file://0002-Add-compatibility-for-k8s-1.16.patch \
    file://0003-use-oss-image.patch \
    file://0004-Update-to-Elastic-7.4.0-Release.patch \
    file://0005-set-initial-masters-to-master-0.patch \
    file://index.yaml \
    file://repositories.yaml \
    "

PATCHTOOL = "git"
PATCH_COMMIT_FUNCTIONS = "1"

S = "${WORKDIR}/git"

inherit allarch

helm_folder = "${nonarch_libdir}/helm"
helmchart_version = "0.1.0"

do_configure[noexec] = "1"

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
	install -m 0644 ${WORKDIR}/repositories.yaml \
		${helm_home}/repository/repositories.yaml

	# Stage a local repo index that can be updated by the build
	install -m 0644 ${WORKDIR}/index.yaml ${helm_home}/repository/local/index.yaml

	# Host a server for the charts
	helm serve --repo-path . &
	sleep 1
	helm repo rm local
	helm repo add local http://localhost:8879/charts

	# Create the tgz files
	rm -f elasticsearch/Makefile
	make elasticsearch

	# terminate helm server (the last backgrounded task)
	kill $!
	rm -rf ${helm_home}
}

do_install () {
	install -d -m 755 ${D}${helm_folder}
	install -p -D -m 755 ${B}/*.tgz ${D}${helm_folder}
}

FILES_${PN} = "${helm_folder}"

RDEPENDS_${PN} = " \
    helm \
    stx-platform-helm \
    stx-openstack-helm \
"
