
SUMMARY = "Openstack-Helm-Infra charts"
DESCRIPTION = "Openstack-Helm-Infra charts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "helm-native"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV_openstack-helm-infra = "c9d6676bf9a5aceb311dc31dadd07cba6a3d6392"
SRCREV_openstack-armada-app = "863f4b9733d3d4f4fd490606a94b84cfdaf2df2c"

# Patches pulled from:
# SRCREV_openstack-armada-app = "863f4b9733d3d4f4fd490606a94b84cfdaf2df2c"
# git://opendev.org/starlingx/openstack-armada-app

SRC_URI = " \
    git://github.com/openstack/openstack-helm-infra;protocol=${PROTOCOL};name=openstack-helm-infra \
    file://0001-Allow-multiple-containers-per-daemonset-pod.patch \
    file://0002-Add-imagePullSecrets-in-service-account.patch \
    file://0003-Set-Min-NGINX-handles.patch \
    file://0004-Partial-revert-of-31e3469d28858d7b5eb6355e88b6f49fd6.patch \
    file://0005-Add-TLS-support-for-Gnocchi-public-endpoint.patch \
    file://0006-Fix-pod-restarts-on-all-workers-when-worker-added-re.patch \
    file://0007-Add-io_thread_pool-for-rabbitmq.patch \
    file://0008-Enable-override-of-rabbitmq-probe-parameters.patch \
    file://repositories.yaml \
    "

PATCHTOOL = "git"
PATCH_COMMIT_FUNCTIONS = "1"

S = "${WORKDIR}/git"

inherit allarch

helm_folder = "${nonarch_libdir}/helm"

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

	# Host a server for the charts
	tmpdir=`mktemp -d ${B}/charts-XXXXXX`
	helm serve ${tmpdir} --address localhost:8879 --url http://localhost:8879/charts &
	sleep 1
	helm repo rm local
	helm repo add local http://localhost:8879/charts

	# Make the charts. These produce tgz files
	make helm-toolkit
	make gnocchi
	make ingress
	make libvirt
	make mariadb
	make memcached
	make openvswitch
	make rabbitmq
	make ceph-rgw

	# terminate helm server (the last backgrounded task)
	kill $!
	rm -rf ${helm_home}
}

do_install () {
	install -d -m 755 ${D}${helm_folder}
	install -p -D -m 755 ${B}/*.tgz ${D}${helm_folder}
}

FILES_${PN} = "${helm_folder}"

RDEPENDS_${PN} = "helm"
