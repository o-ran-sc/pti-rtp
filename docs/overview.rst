.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2019 Wind River Systems, Inc.

RTP Overview (INF)
==================

This project implements a real time platform (rtp) to deploy the O-CU and O-DU.

In O-RAN architecture, the O-DU and O-CU could have different deployed scenarios.
The could be container based or VM based, in this release, we only cover the container one. 
In general the performance sensitive parts of the 5G stack require real time platform,
especially for O-DU, the L1 and L2 are requiring the real time feature,
the platform should support the Preemptive Scheduling feature. 
 
Following requirements are going to address the container based solution:

1.Support the real time kernel

2.Support Node Feature Discovery


3.Support CPU Affinity and Isolation


4.Support Dynamic HugePages Allocation


And for the network requirements, the following should be supported:

1.Multiple Networking Interface


2.High performance data plane including the DPDK based vswitch and PCI pass-through/SR-IOV.


In the Bronze release, the following components and services are enabled:

1. Fault Management

2. Configuration Management

3. Software Management

4. Host Management

5. Service Management

6. Support the ansible bootstrap to implement the zero touch provisioning

NOTE: These features leverage the StarlingX (www.starlingx.io). And in Bronze release, these features are only avalaible for IA platform.

NOTE: In this release single server solution is supported only. All the functionalities include controller functions, storage functions and compute functions are integrated in the single server.  

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


About StarlingX
---------------
StarlingX is a complete cloud infrastructure software stack for the edge used by the most demanding applications in industrial IOT, telecom, video delivery and other ultra-low latency use cases. With deterministic low latency required by edge applications, and tools that make distributed edge manageable, StarlingX provides a container-based infrastructure for edge implementations in scalable solutions that is ready for production now.

Contact info
------------
If you need support or add new features/components, please feel free to contact the following:
- Jackie Huang <jackie.huang@windriver.com>
- Xiaohua Zhang <xiaohua.zhang@windriver.com>
