# Overview
The purpose of the contained Ansible playbook and roles is to deploy an O-RAN compliant O-Cloud instance.

Currently supported Kubernetes platforms and infrastructure targets are:

## Supported Versions

This automation targets the following component versions:

- **OKD**: 4.22.0-okd-scos.1
- **Stolostron**: 2.16
- **CentOS Stream CoreOS (SCOS)**: 10.0
- **OKD CLI**: stable-4.22
- **oran-o2ims**: release-4.21

## Platform
- [OKD](https://www.okd.io/)

## Infrastructure
- KVM/libvirtd virtual machine
- Bare metal (x86_64 architecture); see [Requirements for installing OpenShift on a single node](https://docs.okd.io/latest/installing/installing_sno/install-sno-preparing-to-install-sno.html#install-sno-requirements-for-installing-on-a-single-node_install-sno-preparing) for hardware minimum resource requirements

# Prerequisites
The following prerequisites must be installed on the host where the playbook will be run (localhost, by default):

## Deployer

A Linux deployer host is required from which to execute Ansible playbooks. By default, localhost is used as the deployer.
A minimum of 2GB must be available in /tmp for generation of the installer boot image.

## Packages

Several packages are required by Ansible modules or deployment scripts that are invoked by Ansible roles, including:

- ansible & ansible collections
- make
- nmstate
- pip
- wget
- python development headers/libraries
- libvirt development headers/libraries

Following are examples of how to install these packages on common distributions:

**Fedora Linux**

```
dnf install https://dl.fedoraproject.org/pub/epel/epel{,-next}-release-latest-9.noarch.rpm
dnf group install "Development Tools"
dnf install python3-devel python3-libvirt python3-netaddr python3-virtualenv ansible pip pkgconfig libvirt-devel python-lxml nmstate wget make ansible-collection-ansible-posix ansible-collection-ansible-utils ansible-collection-containers-podman ansible-collection-community-crypto ansible-collection-community-general ansible-collection-community-libvirt
```

**Ubuntu Linux**

```
apt-get install libpython3-dev python3-libvirt python3-netaddr python3-virtualenv ansible python3-pip wget make
```

## Ansible

Install Ansible per [Installing Ansible on specific operating systems](https://docs.ansible.com/ansible/latest/installation_guide/installation_distros.html) documentation.

## libvirt/KVM

If deploying the O-Cloud as a virtual machine, the host (as defined in the 'kvm' inventory group) must be configured as a libvirt/KVM host.
Instructions for doing so vary by Linux distribution, for example:

- [Fedora](https://docs.fedoraproject.org/en-US/quick-docs/virtualization-getting-started/)
- [Ubuntu](https://ubuntu.com/server/docs/virtualization-libvirt)

Ensure that the 'libvirt-devel' package is installed, as it is a dependency for the 'libvirt-python' module.

Ensure that the images filesystem (as defined by the 'ocloud_infra_vm_disk_dir' variable) has enough space to accomodate the size of
the VM disk image (as defined by the 'ocloud_infra_vm_disk_gb' variable).

## Python Modules

Install required python modules by installing via the package manager (e.g. yum, dnf, apt) or running:

```
pip install -r requirements.txt
```

## Ansible Collections

Install required Ansible collections by running:

```
ansible-galaxy collection install -r requirements.yml
```

## DNS
To enable network access to cluster services, DNS address records must be defined for the following endpoints:

* api.<cluster>.<domain> (e.g. api.ocloud.example.com)
* api-int.<cluster>.<domain> (e.g. api-int.ocloud.example.com)
* *.apps.<cluster>.<domain> (e.g. *.apps.ocloud.example.com)

In the case of all-in-one topology clusters, all addresses must resolve to the machine network IP assigned to the node.

The okd/playbooks/deploy_dns.yml playbook can be used to deploy dnsmasq if a DNS server needs to be configured.

## HTTP
An HTTP server is required for bare metal deployment in order to provide a source from which to mount the
installer image via virtual media. The okd/playbooks/deploy_http_store.yml playbook can be used to deploy one if needed.

The following inventory variables must be defined for the 'http_store' host if the okd/playbooks/deploy_http_store.yml playbook
is being used (see okd/inventory/host_vars/http_store/ for example):
- ansible_host: hostname/IP of the HTTP store that will serve the agent-based installer ISO image
- http_store_dir: document root on the HTTP store where thet agent-based installer ISO image will be copied
- http_port: port on which the HTTP store listens

## Ansible Variables

### General

Customize one of the inventories under the 'inventory-examples' directory to match the desired infrastructure
and deployment topology for your cluster. The 'ocloud' host group determines the hosts that will comprise
the cluster. The 'ocloud-vm-okd-aio' sample inventory can be used without modification to deploy to a single-node,
VM-based cluster. For bare metal deployment, populate the 'ocloud' group with the hostname(s) of the baremetal
server(s) and create a directory for each host under host_vars/ containing required variables as defined under
[Infrastructure / Bare Metal](#infrastructure--bare-metal) below.

#### Optional
The following variables can be set to override deployment defaults:
- ocloud_infra [default="vm"]: infrastructure target (supported values: "vm", "baremetal")
- ocloud_platform [default="okd"]: platform target
- ocloud_topology [default="aio"]: O-Cloud cluster topology (supported values: "aio", "multinode")
- ocloud_cluster_name [default="ocloud-{{ ocloud_infra }}-{{ ocloud_platform }}-{{ ocloud_topology }}"]: O-Cloud cluster name
- ocloud_domain_name [default="example.com"]: O-Cloud domain name
- ocloud_net_cidr [default="192.168.123.0/24"]: O-Cloud machine network CIDR

### Infrastructure / VM

#### Required
- role: cluster role of the node (supported values: "master")

#### Optional
The following variables can be set to override defaults for deploying to a VM infrastructure target:

- ocloud_infra_vm_cpus [default=16]: Number of vCPUs to allocate to the VM
- ocloud_infra_vm_mem_gb [default=64]: Amount of RAM to allocate to the VM in GB
- ocloud_infra_vm_disk_gb [default=250]: Amount of disk space to allocate to the VM in GB
- ocloud_infra_vm_disk_dir [default="/var/lib/libvirt/images"]: directory where VM images are stored
- ocloud_net_name [default="ocloud"]: virtual network name
- ocloud_infra_vm_net_mode [default="nat"]: Network mode for VM deployment. To attach bridge network with extenal NIC use `bridge`.
- ocloud_infra_vm_net_bridge [default="ocloud-br"]: virtual network bridge name
- ocloud_net_mac_prefix [default="52:54:00:01:23"]: virtual network MAC prefix
- ocloud_dns_servers: list of DNS resolvers to configure (see okd/playbooks/deploy_dns.yml if a DNS server needs to be deployed)
- ocloud_ntp_servers: list of NTP servers to configure (see okd/playbooks/deploy_ntp.yml if an NTP server needs to be deployed)

### Infrastructure / Bare Metal
#### Required
The following variables must be set for deploying to a bare metal infrastructure target (see okd/inventory/host_vars/master-0-baremetal/ for example):
- bmc_address: hostname/IP of the node's out-of-band management interface (e.g. BMC, iDRAC, iLO)
- bmc_user: username used to authenticate to the node's out-of-band management interface
- bmc_password: password used to authenticate to the node's out-of-band management interface
- installation_disk_path: disk device to which OKD will be installed (reference the `deviceName` subfield in [About root device hints](https://docs.openshift.com/container-platform/4.16/installing/installing_with_agent_based_installer/preparing-to-install-with-agent-based-installer.html#root-device-hints_preparing-to-install-with-agent-based-installer)
- mac_addresses: dictionary of interface names and corresponding MAC addresses
- network_config: node network configuration in [nmstate](https://nmstate.io/) format
- ocloud_dns_servers: list of DNS resolvers to configure (see okd/playbooks/deploy_dns.yml if a DNS server needs to be deployed)
- ocloud_net_cidr: must be set to the subnet corresponding to the node's IP assigned in 'network_config'
- ocloud_ntp_servers: list of NTP servers to configure (see okd/playbooks/deploy_ntp.yml if an NTP server needs to be deployed)
- role: cluster role of the node (supported values: "master", "worker")

#### Optional

### Platform / OKD

#### Required
The following Ansible variables must be defined in group_vars/all.yml:

- ocloud_platform_okd_ssh_pubkey: the SSH public key that will be embedded in the OKD install image and used to access deployed nodes

#### Optional
Optionally, the following variables can be set to override default settings:

- ocloud_platform_okd_release [default=4.22.0-okd-scos.1]: OKD release, as defined in [OKD releases](https://github.com/okd-project/okd/releases)
- ocloud_platform_okd_pull_secret [default=None]: pull secret for use with non-public image registries
- ocloud_platform_okd_api_vips [default=None]: list of virtual IPs to use for OKD API access (required if deploying a multi-node cluster)
- ocloud_platform_okd_ingress_vips [default=None]: list of virtual IPs to use for ingress (required if deploying a multi-node cluster)
- ocloud_platform_okd_airgapped_enabled [default=false]: Custom image mirrors for installation
- ocloud_platform_okd_airgapped_registry []: Custom image mirror registry address
- ocloud_platform_okd_airgapped_org []: Custom image mirror org or username
- ocloud_platform_okd_airgapped_repo []: Custom image mirror repo

# Installation

Execute the playbook from the base directory as follows:

```
ansible-playbook -i inventory playbooks/ocloud.yml
```

This will deploy the O-Cloud up through the bootstrap and installation phases, as well as deploying
[Stolostron](https://github.com/stolostron/stolostron) and the [oran-o2ims](https://github.com/openshift-kni/oran-o2ims)
operator.

# Validation

## OKD

Set the KUBECONFIG variable to point to the config generated by the agent-based installer, for example:

```
export KUBECONFIG=~/ocloud.2026-06-16.nli5xjff/cfg/auth/kubeconfig
```

Verify the installation completion by running the 'oc get nodes', 'oc get clusteroperators',
and 'oc get clusterversion' commands and confirm all nodes are ready and all cluster operators
are available, for example:

```
$ oc get nodes
NAME               STATUS   ROLES                         AGE   VERSION
master-0-ocloud   Ready    control-plane,master,worker   25d   v1.35.3

$ oc get clusteroperators
NAME                                       VERSION             AVAILABLE   PROGRESSING   DEGRADED   SINCE   MESSAGE
authentication                             4.22.0-okd-scos.1   True        False         False      40h     
baremetal                                  4.22.0-okd-scos.1   True        False         False      25d     
cloud-controller-manager                   4.22.0-okd-scos.1   True        False         False      25d     
cloud-credential                           4.22.0-okd-scos.1   True        False         False      25d     
cluster-autoscaler                         4.22.0-okd-scos.1   True        False         False      25d     
config-operator                            4.22.0-okd-scos.1   True        False         False      25d     
console                                    4.22.0-okd-scos.1   True        False         False      25d     
control-plane-machine-set                  4.22.0-okd-scos.1   True        False         False      25d     
csi-snapshot-controller                    4.22.0-okd-scos.1   True        False         False      25d     
dns                                        4.22.0-okd-scos.1   True        False         False      25d     
etcd                                       4.22.0-okd-scos.1   True        False         False      25d     
image-registry                             4.22.0-okd-scos.1   True        False         False      25d     
ingress                                    4.22.0-okd-scos.1   True        False         False      25d     
insights                                   4.22.0-okd-scos.1   True        False         False      25d     
kube-apiserver                             4.22.0-okd-scos.1   True        False         False      25d     
kube-controller-manager                    4.22.0-okd-scos.1   True        False         False      25d     
kube-scheduler                             4.22.0-okd-scos.1   True        False         False      25d     
kube-storage-version-migrator              4.22.0-okd-scos.1   True        False         False      25d     
machine-api                                4.22.0-okd-scos.1   True        False         False      25d     
machine-approver                           4.22.0-okd-scos.1   True        False         False      25d     
machine-config                             4.22.0-okd-scos.1   True        False         False      25d     
marketplace                                4.22.0-okd-scos.1   True        False         False      25d     
monitoring                                 4.22.0-okd-scos.1   True        False         False      25d     
network                                    4.22.0-okd-scos.1   True        False         False      25d     
node-tuning                                4.22.0-okd-scos.1   True        False         False      25d     
olm                                        4.22.0-okd-scos.1   True        False         False      25d     
openshift-apiserver                        4.22.0-okd-scos.1   True        False         False      25d     
openshift-controller-manager               4.22.0-okd-scos.1   True        False         False      24d     
openshift-samples                          4.22.0-okd-scos.1   True        False         False      25d     
operator-lifecycle-manager                 4.22.0-okd-scos.1   True        False         False      25d     
operator-lifecycle-manager-catalog         4.22.0-okd-scos.1   True        False         False      25d     
operator-lifecycle-manager-packageserver   4.22.0-okd-scos.1   True        False         False      25d     
service-ca                                 4.22.0-okd-scos.1   True        False         False      25d     
storage                                    4.22.0-okd-scos.1   True        False         False      25d     

$ oc get clusterversion
NAME      VERSION             AVAILABLE   PROGRESSING   SINCE   STATUS
version   4.22.0-okd-scos.1   True        False         25d     Cluster version is 4.22.0-okd-scos.1
```

## Stolostron

Verify the Stolostron deployment by checking the status of resources in the `open-cluster-management`
and `multicluster-engine` namespaces, including the MultiClusterHub and MultiClusterEngine:
```
$ oc get all -n open-cluster-management
Warning: apps.openshift.io/v1 DeploymentConfig is deprecated in v4.14+, unavailable no sooner than v6.0+
NAME                                                                  READY   STATUS    RESTARTS       AGE
pod/acm-cli-downloads-5fc6bc8d85-cph68                                1/1     Running   2              25d
pod/acm-cli-downloads-5fc6bc8d85-wx2dl                                1/1     Running   2              25d
pod/cluster-permission-6f67f6c954-rnrl4                               1/1     Running   2              25d
pod/console-chart-console-v2-96f486b75-6nbnk                          1/1     Running   2              25d
pod/console-chart-console-v2-96f486b75-vnt6q                          1/1     Running   2              25d
pod/grc-policy-addon-controller-649bb46476-ck6fm                      1/1     Running   2              25d
pod/grc-policy-addon-controller-649bb46476-rkfn7                      1/1     Running   2              25d
pod/grc-policy-propagator-7f789d5846-kppt8                            1/1     Running   11             25d
pod/grc-policy-propagator-7f789d5846-qpl22                            1/1     Running   14 (40h ago)   25d
pod/insights-client-74d678547b-9bmhq                                  1/1     Running   2              25d
pod/insights-metrics-7f84b87758-zwzjp                                 2/2     Running   4              25d
pod/klusterlet-addon-controller-v2-568f8dc46c-4wnhs                   1/1     Running   11             25d
pod/klusterlet-addon-controller-v2-568f8dc46c-fkbw8                   1/1     Running   14 (40h ago)   25d
pod/multicluster-integrations-57d6f95869-tm548                        3/3     Running   9              25d
pod/multicluster-observability-operator-795c987898-pfp6b              1/1     Running   26 (40h ago)   25d
pod/multicluster-operators-application-7b77dcf77f-tq254               3/3     Running   8              25d
pod/multicluster-operators-channel-758d6f6fbf-6nbv2                   1/1     Running   3              25d
pod/multicluster-operators-hub-subscription-7fccf4d4b6-f5v64          1/1     Running   3              25d
pod/multicluster-operators-standalone-subscription-654749cb55-ktt74   1/1     Running   2              25d
pod/multicluster-operators-subscription-report-659c567d65-gfpff       1/1     Running   2              25d
pod/multiclusterhub-operator-5bb66b594f-lxndn                         1/1     Running   2              25d
pod/multiclusterhub-operator-5bb66b594f-pzw78                         1/1     Running   2              25d
pod/search-api-66b44dc74-5qrsj                                        1/1     Running   3 (40h ago)    25d
pod/search-collector-945dd564c-b7tzx                                  1/1     Running   2              25d
pod/search-indexer-76758f69c-qk5xx                                    1/1     Running   2              25d
pod/search-postgres-5d8bc68dc7-wb6rv                                  1/1     Running   2              25d
pod/search-v2-operator-controller-manager-d7948f6b5-cr29d             2/2     Running   11 (40h ago)   25d
pod/siteconfig-controller-manager-567676944c-fkv4h                    1/1     Running   22 (40h ago)   25d
pod/submariner-addon-bb8f698fd-bvlmr                                  1/1     Running   2              25d
pod/volsync-addon-controller-678595df9c-6fzfd                         1/1     Running   2              25d

NAME                                                                     TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)    AGE
service/acm-cli-downloads                                                ClusterIP   172.30.84.113    <none>        443/TCP    25d
service/channels-apps-open-cluster-management-webhook-svc                ClusterIP   172.30.227.248   <none>        443/TCP    3d22h
service/console-chart-console-v2                                         ClusterIP   172.30.38.179    <none>        3000/TCP   25d
service/grc-policy-propagator-metrics                                    ClusterIP   172.30.17.144    <none>        8443/TCP   25d
service/hub-subscription-metrics                                         ClusterIP   172.30.234.69    <none>        8381/TCP   25d
service/insights-client                                                  ClusterIP   172.30.146.13    <none>        3030/TCP   25d
service/insights-metrics                                                 ClusterIP   172.30.253.128   <none>        8443/TCP   25d
service/metrics-siteconfig-open-cluster-management-io                    ClusterIP   172.30.166.137   <none>        8443/TCP   25d
service/multicluster-observability-webhook-service                       ClusterIP   172.30.189.41    <none>        443/TCP    25d
service/multicluster-operators-application-svc                           ClusterIP   172.30.161.182   <none>        443/TCP    25d
service/multicluster-operators-subscription                              ClusterIP   172.30.145.162   <none>        8443/TCP   25d
service/multiclusterhub-operator-metrics                                 ClusterIP   172.30.123.35    <none>        8383/TCP   25d
service/multiclusterhub-operator-webhook                                 ClusterIP   172.30.155.147   <none>        443/TCP    25d
service/propagator-webhook-service                                       ClusterIP   172.30.183.203   <none>        443/TCP    25d
service/search-collector                                                 ClusterIP   172.30.51.147    <none>        5010/TCP   25d
service/search-indexer                                                   ClusterIP   172.30.188.100   <none>        3010/TCP   25d
service/search-postgres                                                  ClusterIP   172.30.254.223   <none>        5432/TCP   25d
service/search-search-api                                                ClusterIP   172.30.94.96     <none>        4010/TCP   25d
service/search-v2-operator-controller-manager-metrics-service            ClusterIP   172.30.254.176   <none>        8443/TCP   25d
service/standalone-subscription-metrics                                  ClusterIP   172.30.37.12     <none>        8389/TCP   25d
service/webhook-clusterinstances-siteconfig-open-cluster-management-io   ClusterIP   172.30.237.182   <none>        443/TCP    25d

NAME                                                             READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/acm-cli-downloads                                2/2     2            2           25d
deployment.apps/cluster-permission                               1/1     1            1           25d
deployment.apps/console-chart-console-v2                         2/2     2            2           25d
deployment.apps/grc-policy-addon-controller                      2/2     2            2           25d
deployment.apps/grc-policy-propagator                            2/2     2            2           25d
deployment.apps/insights-client                                  1/1     1            1           25d
deployment.apps/insights-metrics                                 1/1     1            1           25d
deployment.apps/klusterlet-addon-controller-v2                   2/2     2            2           25d
deployment.apps/multicluster-integrations                        1/1     1            1           25d
deployment.apps/multicluster-observability-operator              1/1     1            1           25d
deployment.apps/multicluster-operators-application               1/1     1            1           25d
deployment.apps/multicluster-operators-channel                   1/1     1            1           25d
deployment.apps/multicluster-operators-hub-subscription          1/1     1            1           25d
deployment.apps/multicluster-operators-standalone-subscription   1/1     1            1           25d
deployment.apps/multicluster-operators-subscription-report       1/1     1            1           25d
deployment.apps/multiclusterhub-operator                         2/2     2            2           25d
deployment.apps/search-api                                       1/1     1            1           25d
deployment.apps/search-collector                                 1/1     1            1           25d
deployment.apps/search-indexer                                   1/1     1            1           25d
deployment.apps/search-postgres                                  1/1     1            1           25d
deployment.apps/search-v2-operator-controller-manager            1/1     1            1           25d
deployment.apps/siteconfig-controller-manager                    1/1     1            1           25d
deployment.apps/submariner-addon                                 1/1     1            1           25d
deployment.apps/volsync-addon-controller                         1/1     1            1           25d

NAME                                                                        DESIRED   CURRENT   READY   AGE
replicaset.apps/acm-cli-downloads-5fc6bc8d85                                2         2         2       25d
replicaset.apps/cluster-permission-6f67f6c954                               1         1         1       25d
replicaset.apps/console-chart-console-v2-96f486b75                          2         2         2       25d
replicaset.apps/grc-policy-addon-controller-649bb46476                      2         2         2       25d
replicaset.apps/grc-policy-propagator-7f789d5846                            2         2         2       25d
replicaset.apps/insights-client-74d678547b                                  1         1         1       25d
replicaset.apps/insights-metrics-7f84b87758                                 1         1         1       25d
replicaset.apps/klusterlet-addon-controller-v2-568f8dc46c                   2         2         2       25d
replicaset.apps/multicluster-integrations-57d6f95869                        1         1         1       25d
replicaset.apps/multicluster-observability-operator-795c987898              1         1         1       25d
replicaset.apps/multicluster-operators-application-7b77dcf77f               1         1         1       25d
replicaset.apps/multicluster-operators-channel-758d6f6fbf                   1         1         1       25d
replicaset.apps/multicluster-operators-hub-subscription-7fccf4d4b6          1         1         1       25d
replicaset.apps/multicluster-operators-standalone-subscription-654749cb55   1         1         1       25d
replicaset.apps/multicluster-operators-subscription-report-659c567d65       1         1         1       25d
replicaset.apps/multiclusterhub-operator-5bb66b594f                         2         2         2       25d
replicaset.apps/search-api-66b44dc74                                        1         1         1       25d
replicaset.apps/search-collector-945dd564c                                  1         1         1       25d
replicaset.apps/search-indexer-76758f69c                                    1         1         1       25d
replicaset.apps/search-postgres-5d8bc68dc7                                  1         1         1       25d
replicaset.apps/search-v2-operator-controller-manager-d7948f6b5             1         1         1       25d
replicaset.apps/siteconfig-controller-manager-567676944c                    1         1         1       25d
replicaset.apps/submariner-addon-bb8f698fd                                  1         1         1       25d
replicaset.apps/volsync-addon-controller-678595df9c                         1         1         1       25d

NAME                                         HOST/PORT                                                          PATH   SERVICES            PORT         TERMINATION          WILDCARD
route.route.openshift.io/acm-cli-downloads   acm-cli-downloads.apps.ocloud.example.com                           acm-cli-downloads   https-8443   reencrypt/Redirect   None
route.route.openshift.io/search-api          search-api-open-cluster-management.apps.ocloud.example.com          search-search-api   <all>        reencrypt            None

$ oc get all -n multicluster-engine
Warning: apps.openshift.io/v1 DeploymentConfig is deprecated in v4.14+, unavailable no sooner than v6.0+
NAME                                                       READY   STATUS    RESTARTS       AGE
pod/agentinstalladmission-5674b75874-hrpmp                 1/1     Running   2              25d
pod/agentinstalladmission-5674b75874-qrbmj                 1/1     Running   2              25d
pod/assisted-image-service-0                               1/1     Running   2              25d
pod/assisted-service-cf44b96c5-v2jvj                       2/2     Running   18 (40h ago)   25d
pod/cluster-curator-controller-77484cdcd9-sggwp            1/1     Running   2              25d
pod/cluster-curator-controller-77484cdcd9-v9nmr            1/1     Running   2              25d
pod/cluster-image-set-controller-84f858f95c-gzsc9          1/1     Running   2              25d
pod/cluster-manager-9bf874c87-bj9q4                        1/1     Running   2              25d
pod/cluster-manager-9bf874c87-lslxz                        1/1     Running   2              25d
pod/cluster-manager-9bf874c87-xnrwc                        1/1     Running   2              25d
pod/cluster-proxy-addon-manager-5b5f77b546-6t6bt           1/1     Running   17 (40h ago)   25d
pod/cluster-proxy-addon-manager-5b5f77b546-b6bl6           1/1     Running   8              25d
pod/cluster-proxy-addon-user-7799d6c86f-2ztgp              2/2     Running   4              25d
pod/cluster-proxy-addon-user-7799d6c86f-pg7q8              2/2     Running   4              25d
pod/cluster-proxy-d5957b769-5dvz2                          1/1     Running   2              25d
pod/cluster-proxy-d5957b769-xw4vb                          1/1     Running   2              25d
pod/clusterclaims-controller-568f4c8b84-dhfzz              2/2     Running   4              25d
pod/clusterclaims-controller-568f4c8b84-h7snk              2/2     Running   4              25d
pod/clusterlifecycle-state-metrics-v2-b5b87bf66-gz7st      1/1     Running   2              25d
pod/console-mce-console-df4558f6d-b2b9m                    1/1     Running   2              25d
pod/console-mce-console-df4558f6d-t7fzq                    1/1     Running   2              25d
pod/discovery-operator-57b545cb64-5s7gr                    1/1     Running   2              25d
pod/hive-operator-8b85d6576-d2lxw                          1/1     Running   2              25d
pod/hypershift-addon-manager-84f94555b-hstlr               1/1     Running   2              25d
pod/infrastructure-operator-76dc489985-h9xlp               1/1     Running   22 (40h ago)   25d
pod/managedcluster-import-controller-v2-75958c549f-65zfk   1/1     Running   7 (40h ago)    25d
pod/managedcluster-import-controller-v2-75958c549f-6xvrg   1/1     Running   4              25d
pod/multicluster-engine-operator-577d696db6-lv78w          1/1     Running   2              25d
pod/multicluster-engine-operator-577d696db6-txbl5          1/1     Running   3              25d
pod/ocm-controller-79998648b9-ljnk9                        1/1     Running   5              25d
pod/ocm-controller-79998648b9-qkhkc                        1/1     Running   6 (40h ago)    25d
pod/ocm-proxyserver-58d47fb94f-5qprt                       1/1     Running   2              25d
pod/ocm-proxyserver-58d47fb94f-b6k5k                       1/1     Running   2              25d
pod/ocm-webhook-846f9c95c-7rksd                            1/1     Running   2              25d
pod/ocm-webhook-846f9c95c-rl5hx                            1/1     Running   2              25d
pod/provider-credential-controller-c898b6d97-vxkv4         2/2     Running   4              25d

NAME                                                   TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)             AGE
service/agent-registration                             ClusterIP   172.30.237.132   <none>        9091/TCP            25d
service/agentinstalladmission                          ClusterIP   172.30.224.110   <none>        443/TCP             25d
service/assisted-image-service                         ClusterIP   172.30.61.235    <none>        8080/TCP,8081/TCP   25d
service/assisted-service                               ClusterIP   172.30.144.212   <none>        8090/TCP,8091/TCP   25d
service/cluster-proxy-addon-anp                        ClusterIP   172.30.197.51    <none>        8091/TCP            25d
service/cluster-proxy-addon-user                       ClusterIP   172.30.55.96     <none>        9092/TCP            25d
service/clusterlifecycle-state-metrics-v2              ClusterIP   172.30.242.190   <none>        8443/TCP            25d
service/console-mce-console                            ClusterIP   172.30.252.141   <none>        3000/TCP            25d
service/discovery-operator                             ClusterIP   172.30.183.220   <none>        8080/TCP            25d
service/discovery-operator-webhook-service             ClusterIP   172.30.82.141    <none>        443/TCP             25d
service/multicluster-engine-operator-metrics           ClusterIP   172.30.39.130    <none>        8080/TCP            25d
service/multicluster-engine-operator-webhook-service   ClusterIP   172.30.222.19    <none>        443/TCP             25d
service/ocm-proxyserver                                ClusterIP   172.30.215.181   <none>        443/TCP             25d
service/ocm-webhook                                    ClusterIP   172.30.223.167   <none>        443/TCP             25d
service/proxy-entrypoint                               ClusterIP   172.30.250.35    <none>        8090/TCP,8091/TCP   25d

NAME                                                  READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/agentinstalladmission                 2/2     2            2           25d
deployment.apps/assisted-service                      1/1     1            1           25d
deployment.apps/cluster-curator-controller            2/2     2            2           25d
deployment.apps/cluster-image-set-controller          1/1     1            1           25d
deployment.apps/cluster-manager                       3/3     3            3           25d
deployment.apps/cluster-proxy                         2/2     2            2           25d
deployment.apps/cluster-proxy-addon-manager           2/2     2            2           25d
deployment.apps/cluster-proxy-addon-user              2/2     2            2           25d
deployment.apps/clusterclaims-controller              2/2     2            2           25d
deployment.apps/clusterlifecycle-state-metrics-v2     1/1     1            1           25d
deployment.apps/console-mce-console                   2/2     2            2           25d
deployment.apps/discovery-operator                    1/1     1            1           25d
deployment.apps/hive-operator                         1/1     1            1           25d
deployment.apps/hypershift-addon-manager              1/1     1            1           25d
deployment.apps/infrastructure-operator               1/1     1            1           25d
deployment.apps/managedcluster-import-controller-v2   2/2     2            2           25d
deployment.apps/multicluster-engine-operator          2/2     2            2           25d
deployment.apps/ocm-controller                        2/2     2            2           25d
deployment.apps/ocm-proxyserver                       2/2     2            2           25d
deployment.apps/ocm-webhook                           2/2     2            2           25d
deployment.apps/provider-credential-controller        1/1     1            1           25d

NAME                                                             DESIRED   CURRENT   READY   AGE
replicaset.apps/agentinstalladmission-5674b75874                 2         2         2       25d
replicaset.apps/assisted-service-cf44b96c5                       1         1         1       25d
replicaset.apps/cluster-curator-controller-77484cdcd9            2         2         2       25d
replicaset.apps/cluster-image-set-controller-84f858f95c          1         1         1       25d
replicaset.apps/cluster-manager-9bf874c87                        3         3         3       25d
replicaset.apps/cluster-proxy-addon-manager-5b5f77b546           2         2         2       25d
replicaset.apps/cluster-proxy-addon-user-7799d6c86f              2         2         2       25d
replicaset.apps/cluster-proxy-d5957b769                          2         2         2       25d
replicaset.apps/clusterclaims-controller-568f4c8b84              2         2         2       25d
replicaset.apps/clusterlifecycle-state-metrics-v2-b5b87bf66      1         1         1       25d
replicaset.apps/console-mce-console-df4558f6d                    2         2         2       25d
replicaset.apps/discovery-operator-57b545cb64                    1         1         1       25d
replicaset.apps/hive-operator-8b85d6576                          1         1         1       25d
replicaset.apps/hypershift-addon-manager-84f94555b               1         1         1       25d
replicaset.apps/infrastructure-operator-76dc489985               1         1         1       25d
replicaset.apps/managedcluster-import-controller-v2-75958c549f   2         2         2       25d
replicaset.apps/multicluster-engine-operator-577d696db6          2         2         2       25d
replicaset.apps/ocm-controller-79998648b9                        2         2         2       25d
replicaset.apps/ocm-proxyserver-58d47fb94f                       2         2         2       25d
replicaset.apps/ocm-webhook-846f9c95c                            2         2         2       25d
replicaset.apps/provider-credential-controller-c898b6d97         1         1         1       25d

NAME                                      READY   AGE
statefulset.apps/assisted-image-service   1/1     25d

NAME                                                HOST/PORT                                                                  PATH   SERVICES                   PORT                     TERMINATION          WILDCARD
route.route.openshift.io/agent-registration         agent-registration-multicluster-engine.apps.ocloud.example.com              agent-registration         agentregistration        reencrypt/Redirect   None
route.route.openshift.io/assisted-image-service     assisted-image-service-multicluster-engine.apps.ocloud.example.com          assisted-image-service     assisted-image-service   reencrypt            None
route.route.openshift.io/assisted-service           assisted-service-multicluster-engine.apps.ocloud.example.com                assisted-service           assisted-service         reencrypt            None
route.route.openshift.io/cluster-proxy-addon-anp    cluster-proxy-anp.apps.ocloud.example.com                                   cluster-proxy-addon-anp    anp-port                 passthrough          None
route.route.openshift.io/cluster-proxy-addon-user   cluster-proxy-user.apps.ocloud.example.com                                  cluster-proxy-addon-user   user-port                reencrypt/Redirect   None

$ oc get MultiClusterHub -n open-cluster-management
NAME              STATUS    AGE   CURRENTVERSION   DESIREDVERSION
multiclusterhub   Running   25d   2.16.0           2.16.0

oc get MultiClusterEngine -n multicluster-engine
NAME                 STATUS      AGE   CURRENTVERSION   DESIREDVERSION
multiclusterengine   Available   25d   2.11.0           2.11.0
```

## oran-o2ims

Verify the oran-o2ims operator deployment by running the 'oc get all -n oran-o2ims'
command, for example:

```
$ oc get all -n oran-o2ims
Warning: apps.openshift.io/v1 DeploymentConfig is deprecated in v4.14+, unavailable no sooner than v6.0+
NAME                                                 READY   STATUS      RESTARTS       AGE
pod/alarms-server-6f945bfbc4-cfmzf                   1/1     Running     2              25d
pod/alarms-server-events-cleanup-29702580-r7f47      0/1     Completed   0              158m
pod/alarms-server-events-cleanup-29702640-vz5mr      0/1     Completed   0              98m
pod/alarms-server-events-cleanup-29702700-gxfcb      0/1     Completed   0              38m
pod/artifacts-server-f67dd9767-cl48c                 1/1     Running     2              25d
pod/cluster-server-576f567675-pvjq2                  1/1     Running     2              25d
pod/hardwareplugin-manager-server-67c96c8f4-xxjt6    1/1     Running     9 (40h ago)    25d
pod/metal3-hardwareplugin-server-6d9b74799b-2hztp    1/1     Running     9 (40h ago)    25d
pod/oran-o2ims-controller-manager-847885b8cc-krwsj   1/1     Running     21 (40h ago)   25d
pod/postgres-server-569ffb5b6c-ds9g8                 1/1     Running     2              25d
pod/provisioning-server-5b9d667c59-mpmwt             1/1     Running     2              25d
pod/resource-server-66669b9b54-j5d92                 1/1     Running     2              25d

NAME                                                    TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)    AGE
service/alarms-server                                   ClusterIP   172.30.58.62     <none>        8443/TCP   25d
service/artifacts-server                                ClusterIP   172.30.79.115    <none>        8443/TCP   25d
service/cluster-server                                  ClusterIP   172.30.149.214   <none>        8443/TCP   25d
service/hardwareplugin-manager-server                   ClusterIP   172.30.87.147    <none>        8443/TCP   25d
service/metal3-hardwareplugin-server                    ClusterIP   172.30.159.125   <none>        8443/TCP   25d
service/oran-o2ims-controller-manager-metrics-service   ClusterIP   172.30.129.130   <none>        8443/TCP   25d
service/oran-o2ims-nar-callback-service                 ClusterIP   172.30.237.29    <none>        8090/TCP   25d
service/oran-o2ims-webhook-service                      ClusterIP   172.30.49.90     <none>        443/TCP    25d
service/postgres-server                                 ClusterIP   172.30.51.146    <none>        5432/TCP   25d
service/provisioning-server                             ClusterIP   172.30.78.84     <none>        8443/TCP   25d
service/resource-server                                 ClusterIP   172.30.64.19     <none>        8443/TCP   25d

NAME                                            READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/alarms-server                   1/1     1            1           25d
deployment.apps/artifacts-server                1/1     1            1           25d
deployment.apps/cluster-server                  1/1     1            1           25d
deployment.apps/hardwareplugin-manager-server   1/1     1            1           25d
deployment.apps/metal3-hardwareplugin-server    1/1     1            1           25d
deployment.apps/oran-o2ims-controller-manager   1/1     1            1           25d
deployment.apps/postgres-server                 1/1     1            1           25d
deployment.apps/provisioning-server             1/1     1            1           25d
deployment.apps/resource-server                 1/1     1            1           25d

NAME                                                       DESIRED   CURRENT   READY   AGE
replicaset.apps/alarms-server-6f945bfbc4                   1         1         1       25d
replicaset.apps/artifacts-server-f67dd9767                 1         1         1       25d
replicaset.apps/cluster-server-576f567675                  1         1         1       25d
replicaset.apps/hardwareplugin-manager-server-67c96c8f4    1         1         1       25d
replicaset.apps/metal3-hardwareplugin-server-6d9b74799b    1         1         1       25d
replicaset.apps/oran-o2ims-controller-manager-5cbfb656fc   0         0         0       25d
replicaset.apps/oran-o2ims-controller-manager-847885b8cc   1         1         1       25d
replicaset.apps/postgres-server-569ffb5b6c                 1         1         1       25d
replicaset.apps/provisioning-server-5b9d667c59             1         1         1       25d
replicaset.apps/resource-server-66669b9b54                 1         1         1       25d

NAME                                         SCHEDULE    TIMEZONE   SUSPEND   ACTIVE   LAST SCHEDULE   AGE
cronjob.batch/alarms-server-events-cleanup   0 * * * *   <none>     False     0        38m             25d

NAME                                              STATUS     COMPLETIONS   DURATION   AGE
job.batch/alarms-server-events-cleanup-29702580   Complete   1/1           3s         158m
job.batch/alarms-server-events-cleanup-29702640   Complete   1/1           3s         98m
job.batch/alarms-server-events-cleanup-29702700   Complete   1/1           3s         38m

NAME                                                HOST/PORT                             PATH                                SERVICES              PORT   TERMINATION          WILDCARD
route.route.openshift.io/oran-o2ims-ingress-6dh8j   o2ims.apps.ocloud.example.com   /o2ims-infrastructureProvisioning   provisioning-server   api    reencrypt/Redirect   None
route.route.openshift.io/oran-o2ims-ingress-7swhz   o2ims.apps.ocloud.example.com   /o2ims-infrastructureMonitoring     alarms-server         api    reencrypt/Redirect   None
route.route.openshift.io/oran-o2ims-ingress-9jg4t   o2ims.apps.ocloud.example.com   /o2ims-infrastructureArtifacts      artifacts-server      api    reencrypt/Redirect   None
route.route.openshift.io/oran-o2ims-ingress-lbx89   o2ims.apps.ocloud.example.com   /o2ims-infrastructureCluster        cluster-server        api    reencrypt/Redirect   None
route.route.openshift.io/oran-o2ims-ingress-pl9db   o2ims.apps.ocloud.example.com   /o2ims-infrastructureInventory      resource-server       api    reencrypt/Redirect   None
```

Additionally, a service account can be created and a token generated for testing the O2 API endpoints:

```
$ oc apply -f https://raw.githubusercontent.com/openshift-kni/oran-o2ims/refs/heads/release-4.21/config/testing/client-service-account-rbac.yaml
serviceaccount/test-client created
clusterrolebinding.rbac.authorization.k8s.io/oran-o2ims-test-client-binding created

$ TOKEN=`oc create token -n oran-o2ims test-client`

$ curl -H "Authorization: Bearer $TOKEN" -k https://o2ims.apps.ocloud-baremetal-okd-aio.example.com/o2ims-infrastructureInventory/v1
$ curl -s -k -H "Authorization: Bearer $TOKEN" https://o2ims.apps.ocloud.example.com/o2ims-infrastructureInventory/v1 | jq
{
  "description": "OpenShift O-Cloud Manager",
  "extensions": {},
  "globalcloudId": "00000000-0000-0000-0000-000000000000",
  "name": "OpenShift O-Cloud Manager",
  "oCloudId": "78c0e1f8-e2a7-4a18-a777-88de0c1d493a",
  "serviceUri": "https://o2ims.apps.ocloud.example.com"
}
```

## ORAN O2 IMS Compliance

A playbook is provided to automate execution of O2 IMS compliance tests from the [it/test](https://gerrit.o-ran-sc.org/r/q/project:it/test) repo. The following extra variables must be provided:

- ocloud_kubeconfig: path of the kubeconfig for the cluster hosting the O2 API server
- ocloud_compliance_resource_type: name of a resource type associated with the target O-Cloud
- ocloud_compliance_resource_desc_substring: substring of a resource description associated with the target O-Cloud
- ocloud_compliance_notification_endpoint: endpoint of the O-Cloud event notification consumer
- ocloud_compliance_notification_resource_address: resource address of O-Cloud event notification subscription
- ocloud_compliance_notification_publisher_endpoint: endpoint of the O-Cloud event notification publisher

For example:

```
ansible-playbook -i inventory playbooks/ocloud_compliance.yml \
  -e ocloud_kubeconfig=$KUBECONFIG \
  -e ocloud_compliance_resource_type="HPE/ProLiant DL360 Gen10 Plus (P28948-B21)" \
  -e ocloud_compliance_resource_desc_substring="DL360" \
  -e ocloud_compliance_notification_endpoint=http://consumer-events-subscription-service.cloud-events.svc.cluster.local:9043/event \
  -e ocloud_compliance_notification_resource_address=/cluster/node/sno-du-1.example.com/sync/sync-status/sync-state \
  -e ocloud_compliance_notification_publisher_endpoint=http://ptp-event-publisher-service-sno-du-1.openshift-ptp.svc.cluster.local:9043/api/ocloudNotifications/v2/subscriptions
```

## Sample Workload Deployment

A playbook is provided to deploy a sample workload to the O-Cloud. The following extra variables must be provided:

- ocloud_workloads: comma-delimited list of workload(s) to deploy
- ocloud_dms_host: hostname of the O-Cloud Manager used to query available deployment managagers
- ocloud_dms_deployment_mgr_id: deployment manager ID where the sample workload(s) will be deployed
- ocloud_kubeconfig: path of the kubeconfig for the cluster hosting the O-Cloud Manager

For example:
```
ansible-playbook -i inventory playbooks/ocloud_workload.yml -e ocloud_dms_host=o2ims.apps.ocloud.example.com -e ocloud_dms_deployment_mgr_id=c0e63b38-c45d-514c-9688-e2ba43a9ab3e -e ocloud_workloads=oaicucp -e ocloud_kubeconfig=$KUBECONFIG
```

Supported sample workloads are:

- oaicucp: OpenAirInterface CU-CP
- oaicuup: OpenAirInterface CU-UP
- oaidu: OpenAirInterface DU

# Troubleshooting

## OKD

Refer to [Troubleshooting installation issues](https://docs.okd.io/latest/installing/validation_and_troubleshooting/installing-troubleshooting.html) for information on diagnosing OKD deployment failures.

# Cleanup

This section describes how to clean up a deployment, either due to a failure or to prepare for a new deployment.

## VM-based Deployment

For virtual machine-based deployments, a playbook is provided to automate the cleanup process. Alternatively, manual steps can be followed.

### Automated Cleanup (Recommended)

To automate the cleanup process, set the `ocloud_action` variable to `cleanup` in the `playbooks/ocloud.yml` playbook, as shown below. This will trigger the cleanup tasks within the `ocloud_infra_vm` role, which handles the destruction of VMs, networks, and associated storage.

```yaml
- name: Deploy O-Cloud
  hosts: ocloud
  gather_facts: false
  vars:
    ocloud_action: cleanup
  roles:
    - ocloud
```

Execute the playbook from the base directory to apply the changes:

```bash
ansible-playbook -i inventory <PATH TO YOUR INVENTORY> playbooks/ocloud.yml
```

NOTE: The playbook will prompt for confirmation before proceeding with the destructive actions.

### Manual Cleanup

If you prefer to perform the cleanup manually, follow these steps on the libvirt/KVM host as the root user. Note that the names for the VM, network, and disk images may differ if you have overridden the default variables.

1. **Shut down and remove the virtual machine(s):**
   This command will forcefully stop and then delete the definition of the virtual machine from libvirt.

   ```bash
   # Replace 'master-0' with the actual VM name if you changed the default
   virsh destroy master-0
   virsh undefine master-0
   ```
   If you have multiple VMs, repeat these commands for each one.

2. **Disable and remove the virtual network:**
   This will deactivate and delete the virtual network definition.

   ```bash
   # Replace 'ocloud' with the actual network name if you changed the default
   virsh net-destroy ocloud
   virsh net-undefine ocloud
   ```

3. **Remove virtual disk and boot media:**
   This command deletes the disk image and the installation ISO file.

   ```bash
   # Adjust path and names if defaults were changed
   rm /var/lib/libvirt/images/master-0*.{qcow2,iso}
   ```

## Bare Metal Deployment

For bare metal deployments, cleanup typically involves reprovisioning the server(s) using their baseboard management controllers (BMCs). This process is outside the scope of this automation. Refer to your server hardware documentation for instructions on how to reinstall an operating system. No cleanup is required on the Ansible deployer host for a bare metal deployment.
