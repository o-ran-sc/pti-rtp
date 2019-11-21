.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2019 Wind River Systems, Inc.

RTP Overview 
============

This project implements a real time platform to deploy the O-CU and O-DU.

In O-RAN architecture, the O-DU and O-CU could have different deployed scenarios. 
In general the performance sensitive parts of the 5G stack require real time platform, 
the platform should support the Preemptive Scheduling feature. 

For example, from implementation perspective, the non-virtualized DU, 
VM based DU and container based DU are requiring a real time host system. 
Following requirements are going to address the container based solution:

1.Support Node Feature Discovery


2.Support CPU Affinity and Isolation


3.Support Dynamic HugePages Allocation


And for the network requirements, the following should be supported:
1.Multiple Networking Interface


2.High performance data plane including the DPDK based vswitch and PCI pass-through/SR-IOV.


This is based on Yocto/OpenEmbedded, so it includes a Yocto/OpenEmbedded compatible
layers meta-oran and wrapper scripts to pull all required Yocto/OE layers to build
out the reference platform.

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

Contact info
------------
If you need support or add new features/components, please feel free to contact the following:
- Jackie Huang <jackie.huang@windriver.com>
- Xiaohua Zhang <xiaohua.zhang@windriver.com>
