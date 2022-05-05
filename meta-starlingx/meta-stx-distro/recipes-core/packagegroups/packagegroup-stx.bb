
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
	lighttpd \
	lighttpd-module-proxy \
	lighttpd-module-setenv \
	dnsmasq \
	shadow \
	openldap \
	ntp \
	haproxy \
	syslog-ng \ 
	sudo \
	docker-ce \
	openvswitch \
	systemd \
	nfs-utils \
	nfs-utils-config \
	iptables \
	logrotate \
	mlx4-init \
	initscripts \
	procps \
	iscsi-initiator-utils \
	memcached \
	net-snmp \
	net-snmp-server-snmpd \
	net-snmp-server-snmptrapd \
	libpam-runtime \
	rabbitmq-server \
	rsync \
	base-files \
	audit \
	auditd \
	audit-python \
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
	platform-kickstarts \
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
	mtce-guest\
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
	python-cherrypy \
	python-lefthandclient \
	python-setuptools \
	python-ryu \
	spectre-meltdown-checker \
	kvm-timer-advance-setup \
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
