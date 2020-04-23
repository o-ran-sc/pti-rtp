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

SUMMARY = "StarlingX stx packages"

PR = "r0"

#
# packages which content depend on MACHINE_FEATURES need to be MACHINE_ARCH
#

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES = " \
	packagegroup-stx-upstream \
	packagegroup-stx-puppet \
	packagegroup-stx-fault \
	packagegroup-stx-metal \
	packagegroup-stx-nfv \
	packagegroup-stx-monitoring \
	packagegroup-stx-ha \
	packagegroup-stx-config \
	packagegroup-stx-config-files \
	packagegroup-stx-distributedcloud \
	packagegroup-stx-update \
	packagegroup-stx-integ \
	packagegroup-stx-utilities \
	packagegroup-stx-armada-app \
	"

RDEPENDS_packagegroup-stx-puppet = "\
	stx-puppet \
	puppet-dcdbsync \
	puppet-dcmanager \
	puppet-dcorch \
	puppet-fm \
	puppet-mtce \
	puppet-nfv \
	puppet-patching \
	puppet-smapi \
	puppet-sshd \
	puppet-sysinv \
	puppet-manifests \
	"

RDEPENDS_packagegroup-stx-config = " \
	config-gate-worker \
	config-gate \
	controllerconfig \
	cgts-client \
	sysinv-agent \
	sysinv \
	workerconfig-subfunction \
	tsconfig \
	"

RDEPENDS_packagegroup-stx-config-files  = " \
	audit-config \
	dhclient-config \
	dnsmasq-config \
	docker-config \
	initscripts-config \
	filesystem-scripts \
	haproxy-config \
	ioscheduler-config \
	iptables-config \
	iscsi-initiator-utils-config \
	lighttpd-config \
	logrotate-config \
	memcached-custom \
	mlx4-config \
	net-snmp-config \
	nfs-utils-config \
	ntp-config \
	openldap-config \
	openssh-config \
	openvswitch-config \
	pam-config \
	rabbitmq-server-config \
	rsync-config \
	setup-config \
	shadow-utils-config \
	sudo-config \
	syslog-ng-config \
	systemd-config \
	util-linux-config \
	"

RDEPENDS_packagegroup-stx-fault = " \
        fm-api \
        fm-common \
        fm-doc \
        fm-mgr \
        fm-rest-api \
        python-fmclient \
        snmp-audittrail \
        snmp-ext \
        "

RDEPENDS_packagegroup-stx-ha = " \
        sm-common-libs \
        libsm-common \
        sm \
        sm-db \
        sm-api \
        sm-client \
        sm-tools \
	sm-eru \
        stx-ocf-scripts \
        "

RDEPENDS_packagegroup-stx-metal = " \
	inventory \
	mtce \
	mtce-pmon \
	mtce-hwmon \
	mtce-hostw \
	mtce-lmon \
	mtce-compute \
	mtce-control \
	mtce-storage \
	python-inventoryclient \
	pxe-network-installer \
	"

RDEPENDS_packagegroup-stx-monitoring = " \
	collectd-extensions \
	influxdb-extensions \
	monitor-tools \
	vm-topology \
	"

RDEPENDS_packagegroup-stx-distributedcloud = " \
	distributedcloud-dcmanager \
	distributedcloud-dcorch \
	distributedcloud-dcdbsync \
	distributedcloud-ocf \
	"

RDEPENDS_packagegroup-stx-nfv = " \
	nfv-common \
	nfv-plugins \
	nfv-tools \
	nfv-vim \
	nfv-client \
	"

RDEPENDS_packagegroup-stx-upstream = " \
	barbican \
	python-neutronclient \
	python-aodhclient \
	python-barbican \
	python-barbicanclient \
	python-cinderclient \
	python-glanceclient \
	python-gnocchiclient \
	python-django-horizon \
	python-heatclient \
	python-ironicclient \
	python-keystoneauth1 \
	python-keystoneclient \
	python-magnumclient \
	python-muranoclient \
	python-novaclient \
	python-openstackclient \
	python-openstacksdk \
	python-pankoclient \
	openstack-ras \
	"

RDEPENDS_packagegroup-stx-update = " \
	cgcs-patch \
	cgcs-patch-agent \
	cgcs-patch-controller \
	enable-dev-patch \
	patch-alarm \
	"

RDEPENDS_packagegroup-stx-integ = " \
	dpkg \
	dtc \
	ibsh \
	python-redfishtool \
	puppet-boolean \
	puppetlabs-create-resources \
	puppet-dnsmasq \
	puppet-drbd \
	puppet-filemapper \
	puppet-ldap \
	puppetlabs-lvm \
	puppet-network \
	puppet-nslcd \
	puppetlabs-postgresql \
	puppet-puppi \
	mariadb \
	drbd-utils \
	docker-distribution \
        docker-forward-journald \
	etcd \
	kubernetes \
	ldapscripts \
	python-3parclient \
	python-lefthandclient \
	python-setuptools \
	python-ryu \
	spectre-meltdown-checker \
	kvm-timer-advance \
	ceph \
	lldpd \
        lvm2 \
        tzdata \
	"

RDEPENDS_packagegroup-stx-utilities = " \
	build-info \
	python-cephclient \
	ceph-manager \
	stx-ssl \
	collector \
	collect-engtools \
	logmgmt \
	namespace-utils \
	nfscheck \
	stx-extensions \
	worker-utils \
	update-motd \
	platform-util \
	pci-irq-affinity \
	"

RDEPENDS_packagegroup-stx-armada-app = "\
	monitor-helm \
	monitor-helm-elastic \
	openstack-helm \
	openstack-helm-infra \
	stx-monitor-helm \
	stx-openstack-helm \
	stx-platform-helm \
	"
