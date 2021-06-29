.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2019 Wind River Systems, Inc.


Release-notes
=============


This document provides the release notes for 3.0.0 of RTP.

.. contents::
   :depth: 3
   :local:


Version history
---------------

+--------------------+--------------------+--------------------+--------------------+
| **Date**           | **Ver.**           | **Author**         | **Comment**        |
|                    |                    |                    |                    |
+--------------------+--------------------+--------------------+--------------------+
| 2019-11-02         | 1.0.0              | Jackie Huang       | Initial version    |
|                    |                    |                    |                    |
+--------------------+--------------------+--------------------+--------------------+
| 2020-06-14         | 2.0.0.             | Xiaohua Zhang      |                    |
|                    |                    |                    |                    |
+--------------------+--------------------+--------------------+--------------------+
| 2020-11-23         | 3.0.0              | Xiaohua Zhang      |                    |
|                    |                    |                    |                    |
+--------------------+--------------------+--------------------+--------------------+
| 2021-06-29         | 4.0.0              | Xiaohua Zhang      |                    |
|                    |                    |                    |                    |
+--------------------+--------------------+--------------------+--------------------+

Version 4.0.0, 2021-06-29
-------------------------
- Fourth version (D release)
- Enable the AIO duplex mode (2 servers with High Availabity) with additional worker node.
- Reconstruct the repo to align the upstream projects include StarlingX and Yocto

Version 3.0.0, 2020-11-23
-------------------------
- Third version (Cherry)
- Based on version 2.0.0 (Bronze)
- Add the AIO (all-in-one) 2 servers mode (High Availability)

Version 2.0.0, 2020-06-14
-------------------------
- Second version (Bronze)
- Based on Yocto version 2.7
- Linux kernel 5.0 with preempt-rt patches
- Leverage the StarlingX 3.0
- Support the AIO (all-in-one) deployment scenario
- With Software Management, Configuration Management, Host Management, Service Management, and Service Management enabled for IA platform
- Support the Kubernetes Cluster for ARM platform (verified by NXP LX2160A)
- With the ansbile bootstrap supported for IA platform


Version 1.0.0, 2019-11-02
-------------------------
- Initial Version
- Based on Yocto version 2.6 ('thud' branch)
- Linux kernel 4.18.41 with preempt-rt patches
- Add Docker-18.09.0, kubernetes-1.15.2
- Add kubernetes plugins:

  - kubernetes-dashboard-1.8.3
  - flannel-0.11.0
  - multus-cni-3.3
  - node-feature-discovery-0.4.0
  - cpu-manager-for-kubernetes-1.3.1


