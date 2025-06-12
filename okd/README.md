# Overview
The purpose of the contained Ansible playbook and roles is to deploy an ORAN-compliant O-Cloud instance.

Currently supported Kubernetes platforms and infrastructure targets are:

## Platform
- [OKD](https://www.okd.io/)

## Infrastructure
- KVM/libvirtd virtual machine
- Bare metal (x86_64 architecture); see [Requirements for installing OpenShift on a single node](https://docs.okd.io/4.16/installing/installing_sno/install-sno-preparing-to-install-sno.html#install-sno-requirements-for-installing-on-a-single-node_install-sno-preparing) for hardware minimum resource requirements

# Prerequisites
The following prerequisites must be installed on the host where the playbook will be run (localhost, by default):

## Deployer

A Linux deployer host is required from which to execute Ansible playbooks. By default, localhost is used as the deployer.
A minimum of 2GB must be available in /tmp for generation of the installer boot image.

## Packages

Several packages are required by Ansible modules or deployment scripts that are invoked by Ansible roles, including:

- ansible
- make
- nmstate
- pip
- wget
- python development headers/libraries
- libvirt development headers/libraries

Following are examples of how to install these packages on common distributions:
```

Fedora Linux
```
dnf install https://dl.fedoraproject.org/pub/epel/epel{,-next}-release-latest-9.noarch.rpm
dnf group install "Development Tools"
dnf install python3-devel python3-libvirt python3-netaddr ansible pip pkgconfig libvirt-devel python-lxml nmstate wget make
```

Ubuntu Linux
```
apt-get install libpython3-dev python3-libvirt python3-netaddr ansible python3-pip wget make

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

Update inventory/hosts.yml to specify the deployment target host(s) under the 'ocloud' group. The sample
inventory can be used without modification to deploy to a VM host. For bare metal deployment, populate
the 'ocloud' group with the hostname(s) of the baremetal server(s) and create a directory for each
host under inventory/host_vars/ containing required variables as defined under [Infrastructure / Bare Metal](#infrastructure--bare-metal)
below.

#### Optional
The following variables can be set to override deployment defaults:
- ocloud_infra [default="vm"]: infrastructure target (supported values: "vm", "baremetal")
- ocloud_platform [default="okd"]: platform target
- ocloud_topology [default="aio"]: O-Cloud cluster topology
- ocloud_cluster_name [default="ocloud-{{ ocloud_infra }}-{{ ocloud_platform }}-{{ ocloud_topology }}"]: O-Cloud cluster name
- ocloud_domain_name [default="example.com"]: O-Cloud domain name
- ocloud_net_cidr [default="192.168.123.0/24"]: O-Cloud machine network CIDR

### Infrastructure / VM

#### Required
- role: cluster role of the node (supported values: "master")

#### Optional
The following variables can be set to override defaults for deploying to a VM infrastructure target:

- ocloud_infra_vm_cpus [default=8]: Number of vCPUs to allocate to the VM
- ocloud_infra_vm_mem_gb [default=24]: Amount of RAM to allocate to the VM in GB
- ocloud_infra_vm_disk_gb [default=120]: Amount of disk space to allocate to the VM in GB
- ocloud_infra_vm_disk_dir [default="/var/lib/libvirt/images"]: directory where VM images are stored
- ocloud_net_name [default="ocloud"]: virtual network name
- ocloud_net_bridge [default="ocloud-br"]: virtual network bridge name
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
- role: cluster role of the node (supported values: "master")

#### Optional

### Platform / OKD

#### Required
The following Ansible variables must be defined in group_vars/all.yml:

- ocloud_platform_okd_ssh_pubkey: the SSH public key that will be embedded in the OKD install image and used to access deployed nodes

#### Optional
Optionally, the following variables can be set to override default settings:

- ocloud_platform_okd_release [default=4.14.0-0.okd-2024-01-26-175629]: OKD release, as defined in [OKD releases](https://github.com/okd-project/okd/releases)
- ocloud_platform_okd_pull_secret [default=None]: pull secret for use with non-public image registries
- ocloud_platform_okd_api_vips: list of virtual IPs to use for OKD API access (required if deploying a multi-node cluster)
- ocloud_platform_okd_ingress_vips: list of virtual IPs to use for ingress (required if deploying a multi-node cluster)

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
export KUBECONFIG=/tmp/ansible.6u4ydu5n/cfg/auth/kubeconfig
```

Verify the installation completion by running the 'oc get nodes', 'oc get clusteroperators',
and 'oc get clusterversion' commands and confirm all nodes are ready and all cluster operators
are available, for example:

```
$ oc get nodes
NAME       STATUS   ROLES                         AGE    VERSION
master-0   Ready    control-plane,master,worker   105m   v1.27.9+e36e183

$ oc get clusteroperators
NAME                                       VERSION                          AVAILABLE   PROGRESSING   DEGRADED   SINCE   MESSAGE
authentication                             4.14.0-0.okd-2024-01-26-175629   True        False         False      87m
baremetal                                  4.14.0-0.okd-2024-01-26-175629   True        False         False      94m
cloud-controller-manager                   4.14.0-0.okd-2024-01-26-175629   True        False         False      93m
cloud-credential                           4.14.0-0.okd-2024-01-26-175629   True        False         False      116m
cluster-autoscaler                         4.14.0-0.okd-2024-01-26-175629   True        False         False      94m
config-operator                            4.14.0-0.okd-2024-01-26-175629   True        False         False      92m
console                                    4.14.0-0.okd-2024-01-26-175629   True        False         False      88m
control-plane-machine-set                  4.14.0-0.okd-2024-01-26-175629   True        False         False      94m
csi-snapshot-controller                    4.14.0-0.okd-2024-01-26-175629   True        False         False      96m
dns                                        4.14.0-0.okd-2024-01-26-175629   True        False         False      93m
etcd                                       4.14.0-0.okd-2024-01-26-175629   True        False         False      94m
image-registry                             4.14.0-0.okd-2024-01-26-175629   True        False         False      89m
ingress                                    4.14.0-0.okd-2024-01-26-175629   True        False         False      96m
insights                                   4.14.0-0.okd-2024-01-26-175629   True        False         False      91m
kube-apiserver                             4.14.0-0.okd-2024-01-26-175629   True        False         False      92m
kube-controller-manager                    4.14.0-0.okd-2024-01-26-175629   True        False         False      93m
kube-scheduler                             4.14.0-0.okd-2024-01-26-175629   True        False         False      91m
kube-storage-version-migrator              4.14.0-0.okd-2024-01-26-175629   True        False         False      97m
machine-api                                4.14.0-0.okd-2024-01-26-175629   True        False         False      91m
machine-approver                           4.14.0-0.okd-2024-01-26-175629   True        False         False      94m
machine-config                             4.14.0-0.okd-2024-01-26-175629   True        False         False      96m
marketplace                                4.14.0-0.okd-2024-01-26-175629   True        False         False      96m
monitoring                                 4.14.0-0.okd-2024-01-26-175629   True        False         False      85m
network                                    4.14.0-0.okd-2024-01-26-175629   True        False         False      98m
node-tuning                                4.14.0-0.okd-2024-01-26-175629   True        False         False      93m
openshift-apiserver                        4.14.0-0.okd-2024-01-26-175629   True        False         False      89m
openshift-controller-manager               4.14.0-0.okd-2024-01-26-175629   True        False         False      90m
openshift-samples                          4.14.0-0.okd-2024-01-26-175629   True        False         False      90m
operator-lifecycle-manager                 4.14.0-0.okd-2024-01-26-175629   True        False         False      93m
operator-lifecycle-manager-catalog         4.14.0-0.okd-2024-01-26-175629   True        False         False      94m
operator-lifecycle-manager-packageserver   4.14.0-0.okd-2024-01-26-175629   True        False         False      93m
service-ca                                 4.14.0-0.okd-2024-01-26-175629   True        False         False      97m
storage                                    4.14.0-0.okd-2024-01-26-175629   True        False         False      92m

$ oc get clusterversion
NAME      VERSION                          AVAILABLE   PROGRESSING   SINCE   STATUS
version   4.14.0-0.okd-2024-01-26-175629   True        False         83m     Cluster version is 4.14.0-0.okd-2024-01-26-175629
```

## Stolostron

Verify the Stolostron deployment by running the 'oc get all -n open-cluster-management'
and 'oc get MultiClusterHub -n open-cluster-management' commands, for example:

```
$ oc get all -n open-cluster-management
Warning: apps.openshift.io/v1 DeploymentConfig is deprecated in v4.14+, unavailable in v4.10000+
NAME                                                                  READY   STATUS             RESTARTS        AGE
pod/cluster-permission-6964454c7b-pt2pq                               1/1     Running            0               3h31m
pod/console-chart-console-v2-7f5554f4bf-mpxkq                         1/1     Running            0               3h31m
pod/console-chart-console-v2-7f5554f4bf-mt949                         1/1     Running            0               3h31m
pod/grc-policy-addon-controller-8599cc9c55-qxhtq                      1/1     Running            0               3h31m
pod/grc-policy-addon-controller-8599cc9c55-r2rpq                      1/1     Running            0               3h31m
pod/grc-policy-propagator-78f57b57d8-f79b9                            2/2     Running            0               3h31m
pod/grc-policy-propagator-78f57b57d8-j49cw                            2/2     Running            0               3h31m
pod/insights-client-5445dbd97f-lfl4w                                  1/1     Running            0               3h31m
pod/insights-metrics-7b568c78fd-zdk7t                                 2/2     Running            0               3h31m
pod/klusterlet-addon-controller-v2-7b9c995cc5-kmtkb                   1/1     Running            0               3h31m
pod/klusterlet-addon-controller-v2-7b9c995cc5-v8b6z                   1/1     Running            0               3h31m
pod/multicluster-integrations-78bbdf889f-7nj27                        3/3     Running            1 (3h32m ago)   3h34m
pod/multicluster-observability-operator-86dc5477cb-sbw5f              1/1     Running            0               3h31m
pod/multicluster-operators-application-7995b449fb-2gssq               3/3     Running            2 (3h32m ago)   3h34m
pod/multicluster-operators-channel-597d9ddc46-kvwk2                   1/1     Running            1 (3h33m ago)   3h34m
pod/multicluster-operators-hub-subscription-58c97bf6cc-7rxqk          1/1     Running            1 (3h32m ago)   3h34m
pod/multicluster-operators-standalone-subscription-6b548d9bb8-r9rhf   1/1     Running            0               3h34m
pod/multicluster-operators-subscription-report-7bb6dfdcb6-nblvh       1/1     Running            0               3h34m
pod/multiclusterhub-operator-66d8788b98-98m7l                         1/1     Running            1 (3h34m ago)   3h34m
pod/multiclusterhub-operator-66d8788b98-bg5s2                         1/1     Running            0               3h34m
pod/search-api-6cbd6557c8-ld687                                       1/1     Running            0               3h30m
pod/search-collector-f68dbfc6-m955j                                   1/1     Running            0               3h30m
pod/search-indexer-54b95db649-6mlpc                                   1/1     Running            0               3h30m
pod/search-postgres-bb88bc4d4-ngff9                                   1/1     Running            0               3h30m
pod/search-v2-operator-controller-manager-79cfcfdf5b-d4r42            2/2     Running            0               3h31m
pod/submariner-addon-77b9fb5df8-27rbx                                 0/1     CrashLoopBackOff   46 (9s ago)     3h31m
pod/volsync-addon-controller-546985f674-lj56q                         1/1     Running            0               3h31m

NAME                                                            TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)    AGE
service/channels-apps-open-cluster-management-webhook-svc       ClusterIP   172.30.186.153   <none>        443/TCP    3h32m
service/console-chart-console-v2                                ClusterIP   172.30.136.138   <none>        3000/TCP   3h31m
service/governance-policy-compliance-history-api                ClusterIP   172.30.194.57    <none>        8384/TCP   3h31m
service/grc-policy-propagator-metrics                           ClusterIP   172.30.220.46    <none>        8443/TCP   3h31m
service/hub-subscription-metrics                                ClusterIP   172.30.238.159   <none>        8381/TCP   3h34m
service/insights-client                                         ClusterIP   172.30.176.80    <none>        3030/TCP   3h31m
service/insights-metrics                                        ClusterIP   172.30.187.226   <none>        8443/TCP   3h31m
service/multicluster-observability-webhook-service              ClusterIP   172.30.160.21    <none>        443/TCP    3h31m
service/multicluster-operators-application-svc                  ClusterIP   172.30.104.75    <none>        443/TCP    3h33m
service/multicluster-operators-subscription                     ClusterIP   172.30.24.51     <none>        8443/TCP   3h33m
service/multiclusterhub-operator-metrics                        ClusterIP   172.30.250.141   <none>        8383/TCP   3h34m
service/multiclusterhub-operator-webhook                        ClusterIP   172.30.155.20    <none>        443/TCP    3h34m
service/propagator-webhook-service                              ClusterIP   172.30.186.176   <none>        443/TCP    3h31m
service/search-indexer                                          ClusterIP   172.30.223.23    <none>        3010/TCP   3h30m
service/search-postgres                                         ClusterIP   172.30.68.78     <none>        5432/TCP   3h30m
service/search-search-api                                       ClusterIP   172.30.231.222   <none>        4010/TCP   3h30m
service/search-v2-operator-controller-manager-metrics-service   ClusterIP   172.30.106.43    <none>        8443/TCP   3h31m
service/standalone-subscription-metrics                         ClusterIP   172.30.89.117    <none>        8389/TCP   3h34m

NAME                                                             READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/cluster-permission                               1/1     1            1           3h31m
deployment.apps/console-chart-console-v2                         2/2     2            2           3h31m
deployment.apps/grc-policy-addon-controller                      2/2     2            2           3h31m
deployment.apps/grc-policy-propagator                            2/2     2            2           3h31m
deployment.apps/insights-client                                  1/1     1            1           3h31m
deployment.apps/insights-metrics                                 1/1     1            1           3h31m
deployment.apps/klusterlet-addon-controller-v2                   2/2     2            2           3h31m
deployment.apps/multicluster-integrations                        1/1     1            1           3h34m
deployment.apps/multicluster-observability-operator              1/1     1            1           3h31m
deployment.apps/multicluster-operators-application               1/1     1            1           3h34m
deployment.apps/multicluster-operators-channel                   1/1     1            1           3h34m
deployment.apps/multicluster-operators-hub-subscription          1/1     1            1           3h34m
deployment.apps/multicluster-operators-standalone-subscription   1/1     1            1           3h34m
deployment.apps/multicluster-operators-subscription-report       1/1     1            1           3h34m
deployment.apps/multiclusterhub-operator                         2/2     2            2           3h34m
deployment.apps/search-api                                       1/1     1            1           3h30m
deployment.apps/search-collector                                 1/1     1            1           3h30m
deployment.apps/search-indexer                                   1/1     1            1           3h30m
deployment.apps/search-postgres                                  1/1     1            1           3h30m
deployment.apps/search-v2-operator-controller-manager            1/1     1            1           3h31m
deployment.apps/submariner-addon                                 0/1     1            0           3h31m
deployment.apps/volsync-addon-controller                         1/1     1            1           3h31m

NAME                                                                        DESIRED   CURRENT   READY   AGE
replicaset.apps/cluster-permission-6964454c7b                               1         1         1       3h31m
replicaset.apps/console-chart-console-v2-7f5554f4bf                         2         2         2       3h31m
replicaset.apps/grc-policy-addon-controller-8599cc9c55                      2         2         2       3h31m
replicaset.apps/grc-policy-propagator-78f57b57d8                            2         2         2       3h31m
replicaset.apps/insights-client-5445dbd97f                                  1         1         1       3h31m
replicaset.apps/insights-metrics-7b568c78fd                                 1         1         1       3h31m
replicaset.apps/klusterlet-addon-controller-v2-7b9c995cc5                   2         2         2       3h31m
replicaset.apps/multicluster-integrations-78bbdf889f                        1         1         1       3h34m
replicaset.apps/multicluster-observability-operator-86dc5477cb              1         1         1       3h31m
replicaset.apps/multicluster-operators-application-7995b449fb               1         1         1       3h34m
replicaset.apps/multicluster-operators-channel-597d9ddc46                   1         1         1       3h34m
replicaset.apps/multicluster-operators-hub-subscription-58c97bf6cc          1         1         1       3h34m
replicaset.apps/multicluster-operators-standalone-subscription-6b548d9bb8   1         1         1       3h34m
replicaset.apps/multicluster-operators-subscription-report-7bb6dfdcb6       1         1         1       3h34m
replicaset.apps/multiclusterhub-operator-66d8788b98                         2         2         2       3h34m
replicaset.apps/search-api-6cbd6557c8                                       1         1         1       3h30m
replicaset.apps/search-collector-f68dbfc6                                   1         1         1       3h30m
replicaset.apps/search-indexer-54b95db649                                   1         1         1       3h30m
replicaset.apps/search-postgres-bb88bc4d4                                   1         1         1       3h30m
replicaset.apps/search-v2-operator-controller-manager-79cfcfdf5b            1         1         1       3h31m
replicaset.apps/submariner-addon-77b9fb5df8                                 1         1         0       3h31m
replicaset.apps/volsync-addon-controller-546985f674                         1         1         1       3h31m

NAME                                  HOST/PORT                                                                      PATH   SERVICES            PORT    TERMINATION   WILDCARD
route.route.openshift.io/search-api   search-api-open-cluster-management.apps.ocloud-baremetal-okd-aio.example.com          search-search-api   <all>   reencrypt     None

$ oc get MultiClusterHub -n open-cluster-management
NAME              STATUS    AGE
multiclusterhub   Running   3h34m
```

## oran-o2ims

Verify the oran-o2ims operator deployment by running the 'oc get all -n oran-o2ims'
command, for example:

```
$ oc get all -n oran-o2ims
Warning: apps.openshift.io/v1 DeploymentConfig is deprecated in v4.14+, unavailable in v4.10000+
NAME                                                READY   STATUS    RESTARTS         AGE
pod/deployment-manager-server-658bc7cbdf-qxw4s      2/2     Running   0                3h34m
pod/metadata-server-6ffb478f87-fk7hc                2/2     Running   0                3h34m
pod/oran-o2ims-controller-manager-bf459859f-lrbks   2/2     Running   32 (7m12s ago)   3h34m
pod/postgres-server-6f658dbfb9-gpp4b                1/1     Running   0                3h34m
pod/resource-server-77ccdfdf6d-2fgn8                2/2     Running   0                3h34m

NAME                                                    TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)    AGE
service/alarms-server                                   ClusterIP   172.30.191.244   <none>        8000/TCP   3h34m
service/deployment-manager-server                       ClusterIP   172.30.71.250    <none>        8000/TCP   3h34m
service/metadata-server                                 ClusterIP   172.30.186.182   <none>        8000/TCP   3h34m
service/oran-o2ims-controller-manager-metrics-service   ClusterIP   172.30.238.160   <none>        8443/TCP   3h34m
service/postgres-server                                 ClusterIP   172.30.134.42    <none>        5432/TCP   3h34m
service/resource-server                                 ClusterIP   172.30.151.16    <none>        8000/TCP   3h34m

NAME                                            READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/deployment-manager-server       1/1     1            1           3h34m
deployment.apps/metadata-server                 1/1     1            1           3h34m
deployment.apps/oran-o2ims-controller-manager   1/1     1            1           3h34m
deployment.apps/postgres-server                 1/1     1            1           3h34m
deployment.apps/resource-server                 1/1     1            1           3h34m

NAME                                                      DESIRED   CURRENT   READY   AGE
replicaset.apps/deployment-manager-server-658bc7cbdf      1         1         1       3h34m
replicaset.apps/metadata-server-6ffb478f87                1         1         1       3h34m
replicaset.apps/oran-o2ims-controller-manager-bf459859f   1         1         1       3h34m
replicaset.apps/postgres-server-6f658dbfb9                1         1         1       3h34m
replicaset.apps/resource-server-77ccdfdf6d                1         1         1       3h34m

NAME                                 HOST/PORT                                         PATH                                                   SERVICES                    PORT   TERMINATION          WILDCARD
route.route.openshift.io/api-4h7lp   o2ims.apps.ocloud-baremetal-okd-aio.example.com   /o2ims-infrastructureInventory/v1/resourceTypes        resource-server             api    reencrypt/Redirect   None
route.route.openshift.io/api-5x9tj   o2ims.apps.ocloud-baremetal-okd-aio.example.com   /o2ims-infrastructureMonitoring                        alarms-server               api    reencrypt/Redirect   None
route.route.openshift.io/api-cg6wl   o2ims.apps.ocloud-baremetal-okd-aio.example.com   /                                                      metadata-server             api    reencrypt/Redirect   None
route.route.openshift.io/api-dshdl   o2ims.apps.ocloud-baremetal-okd-aio.example.com   /o2ims-infrastructureInventory/v1/deploymentManagers   deployment-manager-server   api    reencrypt/Redirect   None
route.route.openshift.io/api-jkcz4   o2ims.apps.ocloud-baremetal-okd-aio.example.com   /o2ims-infrastructureInventory/v1/resourcePools        resource-server             api    reencrypt/Redirect   None
route.route.openshift.io/api-njwp2   o2ims.apps.ocloud-baremetal-okd-aio.example.com   /o2ims-infrastructureInventory/v1/subscriptions        resource-server             api    reencrypt/Redirect   None
```

Additionally, a service account can be created and a token generated for testing the O2 API endpoints:

```
$ oc apply -f https://raw.githubusercontent.com/openshift-kni/oran-o2ims/refs/heads/osc-k-release/config/testing/client-service-account-rbac.yaml
serviceaccount/test-client created
clusterrole.rbac.authorization.k8s.io/oran-o2ims-test-client-role created
clusterrolebinding.rbac.authorization.k8s.io/oran-o2ims-test-client-binding created

$ TOKEN=`oc create token -n oran-o2ims test-client`

$ curl -H "Authorization: Bearer $TOKEN" -k https://o2ims.apps.ocloud-baremetal-okd-aio.example.com/o2ims-infrastructureInventory/v1
{
  "oCloudId": "c064e7b9-ee17-44eb-b689-90bb4d26274d",
  "globalCloudId": "3ff52a9a-e4f3-52cc-8823-044b9af8558b",
  "name": "OpenShift O-Cloud",
  "description": "OpenShift O-Cloud",
  "serviceUri": "https://o2ims.apps.ocloud-baremetal-okd-aio.example.com",
  "extensions": {
  }
```
# Troubleshooting

## OKD

Refer to [Troubleshooting installation issues](https://docs.okd.io/4.14/installing/installing-troubleshooting.html) for information
on diagnosing OKD deployment failures.

# Cleanup

## VM

To cleanup a VM-based deployment due to failure, or to prepare to redeploy, execute the following as root on the libvirt/KVM host:

1. Shut down and remove the virtual machine (note that the VM name may differ if the default is overridden):

   ```
   virsh destroy master-0
   virsh undefine master-0
   ```

2. Disable and remove the virtual network (note that the network name may differ if the default is overridden):

   ```
   virsh net-destroy ocloud
   virsh net-undefine ocloud
   ```

3. Remove virtual disk and boot media:

   ```
   rm /var/lib/libvirt/images/master-0*.{qcow2,iso}
   ```
