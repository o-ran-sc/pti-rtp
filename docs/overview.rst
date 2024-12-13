.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2019-2024 Wind River Systems, Inc.

Documentation of Infrastructure (INF) Project
=============================================

.. contents::
   :depth: 3
   :local:

INF Overview
************

This project is for reference implementations of O-Cloud infrastructure which is compliant with `O-RAN Cloud Platform Reference Designs`_ in `O-RAN ALLIANCE Specifications`_.

In O-RAN architecture, the near-RT RIC, O-DU and O-CU could have different deployment scenarios. They could be container based or VM based, and the performance sensitive parts
of the 5G stack require real time platform, especially for O-DU, the L1 and L2 are requiring the real time feature, all these will be supported by the O-Clouds in INF project.

`StarlingX`_ is an opensrouce cloud-native infrastructure project with fully integrated cloud software stack that provides a fully featured open source distributed cloud
(See `StarlingX Distributed Cloud`_ for details). In different deployment scenarios in the O-RAN architecture, `StarlingX`_ O-Cloud can be deployed as a Regional Cloud Platform
to support Near-RT RIC and/or O-CU, or as Edge Cloud Platform to support O-DU and/or O-CU.

.. _`O-RAN ALLIANCE Specifications`: https://specifications.o-ran.org/specifications
.. _`O-RAN Cloud Platform Reference Designs`: https://specifications.o-ran.org/download?id=55
.. _`StarlingX`: https://www.starlingx.io/
.. _`StarlingX Distributed Cloud`: https://docs.starlingx.io/dist_cloud/index-dist-cloud-f5dbeb16b976.html

INF O-Cloud O2 sub-project
**************************

The INF O-Cloud O2 sub-project is for reference implementations of the O-RAN O2 IMS and DMS service to expose the INF O-CLoud to SMO via the O-RAN O2 interface.

Please see the detail of O2 service in the `INF O2 Service Overview`_.

And the INF O2 service is also integrated in `StarlingX`_ O-Cloud as a containerized application, please see the detail in the `O-RAN O2 Application in StarlingX`_
and the `StarlingX O-RAN O2 Application Installation Guide`_.

.. _`INF O2 Service Overview`: https://docs.o-ran-sc.org/projects/o-ran-sc-pti-o2/en/latest/overview.html
.. _`O-RAN O2 Application in StarlingX`: https://www.starlingx.io/blog/starlingx-oran-o2-application/
.. _`StarlingX O-RAN O2 Application Installation Guide`: https://docs.starlingx.io/r/stx.9.0/admintasks/kubernetes/oran-o2-application-b50a0c899e66.html

INF O-Cloud Spec Compliance
***************************

The following lists all reference implementations in INF project that are compliant with the O-RAN specifications.

- INF StarlingX O-Cloud is compliant with:

  - "O-RAN Cloud Platform Reference Designs 2.0"(`O-RAN.WG6.CLOUD-REF-v02.00`_)

- INF StarlingX O2 interface is compliant with: 

  - "O-RAN O2ims Interface Specification 6.0"(`O-RAN.WG6.O2IMS-INTERFACE-R003-v06.00`_)
  - "O-RAN O2dms Interface Specification: Kubernetes Native API Profile for Containerized NFs 5.0"(`O-RAN.WG6.O2DMS-INTERFACE-K8S-PROFILE-R003-v05.00`_)

- INF `StarlingX PTP Notification API`_ v2 conforms to "O-RAN O-Cloud Notification API Specification for Event Consumers 2.01"(`O-RAN.WG6.O-Cloud Notification API-v02.01`_)
  with the following exceptions, that are not supported in `StarlingX`_:

  - O-RAN SyncE Lock-Status-Extended notifications
  - O-RAN SyncE Clock Quality Change notifications
  - O-RAN Custom cluster names
  - /././sync endpoint

.. _`O-RAN.WG6.CLOUD-REF-v02.00`: https://specifications.o-ran.org/download?id=55
.. _`O-RAN.WG6.O2IMS-INTERFACE-R003-v06.00`: https://specifications.o-ran.org/download?id=674
.. _`O-RAN.WG6.O2DMS-INTERFACE-K8S-PROFILE-R003-v05.00`: https://specifications.o-ran.org/download?id=677
.. _`O-RAN.WG6.O-Cloud Notification API-v02.01`: https://specifications.o-ran.org/download?id=300
.. _`StarlingX PTP Notification API`: https://docs.starlingx.io/releasenotes/index.html#ptp-o-ran-spec-compliant-timing-api-notification

Supported O-Cloud Requirements
******************************

Operating System Requirements
-----------------------------

1. Support both the standard kernel and the real time kernel.

   - StarlingX offers a choice of two performance profiles: `StarlingX Worker Function Performance Profiles`_

2. Deterministic interrupt handling with a maximum latency of 20 μs for the real time kernel.

   - StarlingX supports this with the "Low Latency" profile in `StarlingX Worker Function Performance Profiles`_

4. CRI plugin containerd support.

   - `StarlingX containerd support`_

5. QEMU/KVM support for virtual machines.

   - StarlingX supports KubeVirt(`StarlingX KubeVirt`_) and OpenStack(`StarlingX OpenStack`_) for virtual machines, and both KubeVirt and OpenStack are based on QEMU/KVM.

.. _`StarlingX Worker Function Performance Profiles`: https://docs.starlingx.io/deploy/kubernetes/worker-function-performance-profiles.html
.. _`StarlingX containerd support`: https://opendev.org/starlingx/integ/src/branch/master/kubernetes/containerd/debian
.. _`StarlingX KubeVirt`: https://docs.starlingx.io/kube-virt/index-kubevirt-f1bfd2a21152.html
.. _`StarlingX OpenStack`: https://docs.starlingx.io/planning/index-planning-332af0718d15.html#openstack

Cloud Platform Runtime Requirements
-----------------------------------

1. Accelerator Driver: driver for loading, configuring, managing and interfacing with accelerator hardware providing offload functions for O-DU container or VM.

   - See `StarlingX Verified Commercial Hardware`_ for supported accelerators.
   - See `StarlingX SR-IOV FEC Operator`_ as example of how to use accelerators in StarlingX.

2. Network Driver: Network driver(s) for front-haul, back-haul, mid-haul, inter container or VM communication, management and storage networks.

   - See `StarlingX Verified Commercial Hardware`_ and `StarlingX OpenStack Verified Commercial Hardware`_ for supported network cards.
   - See `StarlingX Network Requirements`_ for details of different networks.

3. Board Management: Board management for interfacing with server hardware and sensors.

   - See `StarlingX Provision BMC from Horizon`_ and `StarlingX provision BMC from CLI`_ for details.

4. PTP: Precision time protocol for distributing phase, time and synchronization over a packetbased network.

   - See `StarlingX PTP Overview`_ for details.

5. Software-defined Storage (SDS): Software implementation of block storage running on COTS servers,

   - See `StarlingX Storage`_ for details.

6. Container Runtime: Executes and manages container images on a node.

   - See `StarlingX containerd support`_

7. Hypervisor: Allows host to run multiple isolated VMs.

   - StarlingX supports KubeVirt(`StarlingX KubeVirt`_) and OpenStack(`StarlingX OpenStack`_) for virtual machines.

.. _`StarlingX Verified Commercial Hardware`: https://docs.starlingx.io/planning/kubernetes/verified-commercial-hardware.html
.. _`StarlingX OpenStack Verified Commercial Hardware`: https://docs.starlingx.io/planning/openstack/installation-and-resource-planning-verified-commercial-hardware.html
.. _`StarlingX SR-IOV FEC Operator`: https://docs.starlingx.io/node_management/kubernetes/hardware_acceleration_devices/configure-sriov-fec-operator-to-enable-hw-accelerators-for-hosted-vran-containarized-workloads.html
.. _`StarlingX Network Requirements`: https://docs.starlingx.io/planning/kubernetes/network-requirements.html
.. _`StarlingX Provision BMC from Horizon`: https://docs.starlingx.io/node_management/kubernetes/provisioning_bmc/provisioning-board-management-control-from-horizon.html
.. _`StarlingX provision BMC from CLI`: https://docs.starlingx.io/node_management/kubernetes/provisioning_bmc/provisioning-board-management-control-using-the-cli.html
.. _`StarlingX PTP Overview`: https://docs.starlingx.io/system_configuration/kubernetes/ptp-introduction-d981dd710bda.html
.. _`StarlingX Storage`: https://docs.starlingx.io/storage/index-storage-6cd708f1ada9.html


Generic Requirements for Cloud Platform Management
--------------------------------------------------

1. Fault Management

   - Framework for infrastructure services via API
   
     - Set, clear and query customer alarms
     - Generate customer logs for significant events

   - Maintains an Active Alarm List
   - Provides REST API to query alarms and events
   - Support for alarm suppression
   - Operator alarms

     - On platform nodes and resources
     - On hosted virtual resources

   - Operator logs - Event List

     - Logging of sets/clears of alarms
     - Related to platform nodes and resources
     - Related to hosted virtual resources

   - `StarlingX Kubernetes Fault Management Overview`_
   - `StarlingX OpenStack Fault Management Overview`_

.. _`StarlingX Kubernetes Fault Management Overview`: https://docs.starlingx.io/fault-mgmt/kubernetes/fault-management-overview.html
.. _`StarlingX OpenStack Fault Management Overview`: https://docs.starlingx.io/fault-mgmt/openstack/openstack-fault-management-overview.html

2. Configuration Management

   - Manages Installation
   
     - Auto-discover of new nodes
     - Manage installation parameters (i.e. console, root disks)
     - Bulk provisioning of nodes through XML file

   - Nodal Configuration

     - Node role, role profiles
     - Core, memory (including huge page) assignments
     - Network Interfaces and storage assignments

   - Inventory Discovery

     - CPU/cores, SMT, processors, memory, huge pages
     - Storage, ports
     - GPUs, storage, Crypto/compression H/W

3. Software Management

   - Manages Installation and Commissioning

     - Auto-discover of new nodes
     - Full Infrastructure management
     - Manage installation parameters (i.e. console, root disks)

   - Nodal Configuration

     - Node role, role profiles
     - Core, memory (including huge page) assignments
     - Network Interfaces and storage assignments

   - Hardware Discovery

     - CPU/cores, SMT, processors, memory, huge pages
     - Storage, ports
     - GPUs, storage, Crypto/compression H/W

4. Host Management

   - Full life-cycle and availability management of the physical hosts
   - Detects and automatically handles host failures and initiates recovery
   - Monitoring and fault reporting for:

     - Cluster connectivity
     - Critical process failures
     - Resource utilization thresholds, interface states
     - H/W fault / sensors, host watchdog
     - Activity progress reporting

   - Interfaces with board management (BMC)

     - For out of band reset
     - Power-on/off
     - H/W sensor monitoring

5. Service Management

   - Manages high availability of critical infrastructure and cluster services

     - Supports many redundancy models: N, or N+M
     - Active or passive monitoring of services
     - Allows for specifying the impact of a service failure and escalation policy
     - Automatically recovers failed services

   - Uses multiple messaging paths to avoid split-brain communication failures

     - Up to 3 independent communication paths
     - LAG can also be configured for multi-link protection of each path
     - Messages are authenticated using HMAC
     - SHA-512 if configured / enabled on an interface by-interface basis

6. HA Management

   - High-availability services for supporting cloud platform redundancy

7. User Management

   - User authentication and authorization
   - Isolation of control and resources among different users
  
8. Node Feature Management

   - Detection and setting of node-level policies to align resource allocation choices (i.e.NUMA, SR-IOV, CPU, etc.)

9. HW Accelerator Management

   - Support for managing hardware accelerators, mapping them to O-RAN applications VMs and/or containers, and updating accelerator firmware
  
10. Support the ansible bootstrap to implement the zero touch provisioning

   - Enable the ansible configuration functions for infrastructure itself including the image installation and service configuration.

11. Distributed Cloud

   - StarlingX Distributed Cloud configuration supports an edge computing solution by providing central management and orchestration for
     a geographically distributed network of StarlingX systems.
   - See `StarlingX Distributed Cloud`_ for details.

Multi OS and Deployment Configurations
**************************************

* The INF project supports Multi OS and currently the following OS are supported:

  * StarlingX

    * Debian 11 (bullseye)
    * CentOS 7
    * Yocto 2.7 (warrior)

  * OKD

    * Fedora CoreOS 38

A variety of deployment configuration options are supported:

1. **All-in-one Simplex**

  A single physical server providing all three cloud functions (controller, worker and storage).

2. **All-in-one Duplex**

  Two HA-protected physical servers, both running all three cloud functions (controller, worker and storage), optionally with up to 50 worker nodes added to the cluster.

3. **All-in-one Duplex + up to 50 worker nodes**

  Two HA-protected physical servers, both running all three cloud functions (controller, worker and storage), plus with up to 50 worker nodes added to the cluster.

4. **Standard with Storage Cluster on Controller Nodes**

  A two node HA controller + storage node cluster, managing up to 200 worker nodes.

5. **Standard with Storage Cluster on dedicated Storage Nodes**

  A two node HA controller node cluster with a 2-9 node Ceph storage cluster, managing up to 200 worker nodes.

6. **Distributed Cloud**

  Distributed Cloud configuration supports an edge computing solution by providing central management and orchestration for a geographically distributed network of StarlingX systems.

**NOTE:**

 - For Debian and CentOS based image, all the above deployment configuration are supported.
 - For Yocto Based image, only deployment 1 - 3 are supported, and only container based solution is supported, VM based is not supprted yet.

Upstream Opensource Projects
****************************

About StarlingX
---------------
StarlingX is a complete cloud infrastructure software stack for the edge used by the most demanding applications in industrial IOT, telecom, video delivery and
other ultra-low latency use cases. With deterministic low latency required by edge applications, and tools that make distributed edge manageable, StarlingX
provides a container-based infrastructure for edge implementations in scalable solutions that is ready for production now.

About Yocto and OpenEmbedded
----------------------------
The Yocto Project is an open source collaboration project that provides templates,
tools and methods to help you create custom Linux-based systems for embedded and
IOT products, regardless of the hardware architecture.

OpenEmbedded is a build automation framework and cross-compile environment used
to create Linux distributions for embedded devices. The OpenEmbedded framework
is developed by the OpenEmbedded community, which was formally established in 2003.
OpenEmbedded is the recommended build system of the Yocto Project, which is a Linux
Foundation workgroup that assists commercial companies in the development of Linux-based
systems for embedded products.

About OKD
---------------
OKD is a complete open source container application platform and the community Kubernetes distribution that powers Red Hat OpenShift.

Contact info
============
If you need support or add new features/components, please feel free to contact the following:

 - Jackie Huang <jackie.huang@windriver.com>
