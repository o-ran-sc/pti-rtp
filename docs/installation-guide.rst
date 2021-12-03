.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2019 Wind River Systems, Inc.


Installation Guide
==================

.. contents::
   :depth: 3
   :local:

Abstract
--------

This document describes how to install O-RAN INF image, example configuration (All-in-one Duplex)
for better real time performance, and example deployment of Kubernetes cluster and plugins.

The audience of this document is assumed to have basic knowledge in Yocto/Open-Embedded Linux
and container technology.


Preface
-------

Before starting the installation and deployment of O-RAN INF, you need to download the ISO image or build from source as described in developer-guide.


Hardware Requirements
---------------------

Following minimum hardware requirements must be met for installation of O-RAN INF image with AIO-DX:

+------------------+--------------------------------------------------------------------------------------------+
| **HW Aspect**    | **Requirement**                                                                            |
|                  |                                                                                            |
+------------------+--------------------------------------------------------------------------------------------+
| **# of servers** | 2                                                                                          |
+------------------+--------------------------------------------------------------------------------------------+
| **CPU**          | * Dual-CPU Intel® Xeon® E5 26xx family (SandyBridge) 8 cores/socket                        |
|                  | or                                                                                         |
|                  | * Single-CPU Intel® Xeon® D-15xx family, 8 cores (low-power/low-cost option)               |
+------------------+--------------------------------------------------------------------------------------------+
| **RAM**          | 32G                                                                                        |
|                  |                                                                                            |
+------------------+--------------------------------------------------------------------------------------------+
| **Disk**         | * Disk 1: 500G(It's better to be SSD)                                                      |
|                  | * Disk 2: 1 or more 500 GB for Ceph OSD                                                    |
+------------------+--------------------------------------------------------------------------------------------+
| **NICs**         | * OAM: 1x1GE                                                                               |
|                  | * Data: 1 or more x 10GE (optional)                                                        |
+------------------+--------------------------------------------------------------------------------------------+
| **BIOS settings  | * Hyper-Threading technology enabled                                                       |
|                  | * Virtualization technology enabled                                                        |
|                  | * VT for directed I/O enabled                                                              |
|                  | * CPU power and performance policy set to performance                                      |
|                  | * CPU C state control disabled                                                             |
|                  | * Plug & play BMC detection disabled                                                       |
+------------------+--------------------------------------------------------------------------------------------+

ORAN INF E Release tested on HP ProLiant DL380p Gen8
====================================================

1. Installation for the first server from the O-RAN INF ISO image
-----------------------------------------------------------------

-  Please see the README.md file for how to build the image.
-  The Image is a live ISO image with CLI installer:
   inf-image-aio-installer-intel-corei7-64.iso

1.1 Burn the image to the USB device
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

-  Assume the the usb device is /dev/sdX here

::

    $ sudo dd if=/path/to/inf-image-aio-installer-intel-corei7-64.iso of=/dev/sdX bs=1M

1.2 Install the first server (controller-0)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

-  Reboot the target from the USB device.

-  Select “All-in-one Graphics console” or “All-in-one Serial console
   install” and press ENTER

-  Start the auto installation

-  It will reboot aotumatically after installation

2. Configuration and initialize the bootstrap
---------------------------------------------

2.1 First Login with sysadmin/sysadmin and change password
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

2.2 Set OAM network before bootstrap
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

::

    export OAM_DEV=eno3
    export CONTROLLER0_OAM_CIDR=128.224.210.110/24
    export DEFAULT_OAM_GATEWAY=128.224.210.1
    sudo ip address add $CONTROLLER0_OAM_CIDR dev $OAM_DEV
    sudo ip link set up dev $OAM_DEV
    sudo ip route add default via $DEFAULT_OAM_GATEWAY dev $OAM_DEV

2.3 Login the server through SSH with sysadmin
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

2.4 Prepare the localhost.yml for bootstrap
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

::

    cat << EOF > localhost.yml
    system_mode: duplex
    management_subnet: 192.168.18.0/24
    management_start_address: 192.168.18.2
    management_end_address: 192.168.18.50
    management_gateway_address: 192.168.18.1
    external_oam_subnet: 128.224.210.0/24
    external_oam_gateway_address: 128.224.210.1
    external_oam_floating_address: 128.224.210.110
    external_oam_node_0_address: 128.224.210.111
    external_oam_node_1_address: 128.224.210.112
    EOF

2.5 Run the ansible bootstrap
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

::

    ansible-playbook /usr/share/ansible/stx-ansible/playbooks/bootstrap.yml -vvv

After the bootstrap successfully finish, it will show as following:

::

    PLAY RECAP *************************************************************************************************************
    localhost                  : ok=257  changed=151  unreachable=0    failed=0    skipped=214  rescued=0    ignored=0

2.6 Congiure controller-0
~~~~~~~~~~~~~~~~~~~~~~~~~

Acquire admin credentials:

::

    controller-0:~$ source /etc/platform/openrc
    [sysadmin@controller-0 ~(keystone_admin)]$

Configure the OAM and MGMT interfaces of controller-0 and specify the
attached networks:

::

    OAM_IF=eno3
    MGMT_IF=eno1
    system host-if-modify controller-0 lo -c none
    IFNET_UUIDS=$(system interface-network-list controller-0 | awk '{if ($6=="lo") print $4;}')
    for UUID in $IFNET_UUIDS; do
        system interface-network-remove ${UUID}
    done

    system host-if-modify controller-0 $OAM_IF -n oam0
    system host-if-modify controller-0 $MGMT_IF -n pxeboot0

    system host-if-modify controller-0 oam0 -c platform
    system interface-network-assign controller-0 oam0 oam

    system host-if-modify controller-0 pxeboot0 -c platform
    system interface-network-assign controller-0  pxeboot0 pxeboot

    system host-if-add -V 18 controller-0 mgmt0 vlan pxeboot0
    system interface-network-assign controller-0 mgmt0 mgmt

    system host-if-add -V 19 controller-0 cluster0 vlan pxeboot0
    system interface-network-assign controller-0 cluster0 cluster-host

Example output:

::

    [sysadmin@controller-0 ~(keystone_admin)]$ OAM_IF=eno3
    [sysadmin@controller-0 ~(keystone_admin)]$ MGMT_IF=eno1
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-if-modify controller-0 lo -c none
    +-----------------+--------------------------------------+
    | Property        | Value                                |
    +-----------------+--------------------------------------+
    | ifname          | lo                                   |
    | iftype          | virtual                              |
    | ports           | []                                   |
    | imac            | 00:00:00:00:00:00                    |
    | imtu            | 1500                                 |
    | ifclass         | None                                 |
    | aemode          | None                                 |
    | schedpolicy     | None                                 |
    | txhashpolicy    | None                                 |
    | uuid            | 08c95952-892b-40b5-b17a-7d2ad46e725c |
    | ihost_uuid      | 16afe3a2-ba50-46b8-9fd7-09010059e8b9 |
    | vlan_id         | None                                 |
    | uses            | []                                   |
    | used_by         | []                                   |
    | created_at      | 2021-11-17T00:30:45.265032+00:00     |
    | updated_at      | 2021-11-17T01:03:39.031612+00:00     |
    | sriov_numvfs    | 0                                    |
    | sriov_vf_driver | None                                 |
    +-----------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ IFNET_UUIDS=$(system interface-network-list controller-0 | awk '{if ($6=="lo") print $4;}')
    [sysadmin@controller-0 ~(keystone_admin)]$ for UUID in $IFNET_UUIDS; do
    >     system interface-network-remove ${UUID}
    > done
    Deleted Interface Network: 0bf11f1b-4fc6-4e97-b896-3d6393a3744e
    Deleted Interface Network: a62d95f6-ad4e-4779-bfc0-6a885067f8d8

    [sysadmin@controller-0 ~(keystone_admin)]$ system host-if-modify controller-0 $OAM_IF -n oam0
    +-----------------+--------------------------------------+
    | Property        | Value                                |
    +-----------------+--------------------------------------+
    | ifname          | oam0                                 |
    | iftype          | ethernet                             |
    | ports           | [u'eno3']                            |
    | imac            | 24:6e:96:5d:0c:b2                    |
    | imtu            | 1500                                 |
    | ifclass         | None                                 |
    | aemode          | None                                 |
    | schedpolicy     | None                                 |
    | txhashpolicy    | None                                 |
    | uuid            | d8a048fa-67ef-43ac-8166-671be93caa30 |
    | ihost_uuid      | 16afe3a2-ba50-46b8-9fd7-09010059e8b9 |
    | vlan_id         | None                                 |
    | uses            | []                                   |
    | used_by         | []                                   |
    | created_at      | 2021-11-17T00:28:32.365863+00:00     |
    | updated_at      | 2021-11-17T01:03:45.090904+00:00     |
    | sriov_numvfs    | 0                                    |
    | sriov_vf_driver | None                                 |
    | accelerated     | [True]                               |
    +-----------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-if-modify controller-0 $MGMT_IF -n pxeboot0
    +-----------------+--------------------------------------+
    | Property        | Value                                |
    +-----------------+--------------------------------------+
    | ifname          | pxeboot0                             |
    | iftype          | ethernet                             |
    | ports           | [u'eno1']                            |
    | imac            | 24:6e:96:5d:0c:92                    |
    | imtu            | 1500                                 |
    | ifclass         | None                                 |
    | aemode          | None                                 |
    | schedpolicy     | None                                 |
    | txhashpolicy    | None                                 |
    | uuid            | 23b5e923-1e53-4e70-a975-542d8380b7f2 |
    | ihost_uuid      | 16afe3a2-ba50-46b8-9fd7-09010059e8b9 |
    | vlan_id         | None                                 |
    | uses            | []                                   |
    | used_by         | []                                   |
    | created_at      | 2021-11-17T00:28:32.612230+00:00     |
    | updated_at      | 2021-11-17T01:03:47.341003+00:00     |
    | sriov_numvfs    | 0                                    |
    | sriov_vf_driver | None                                 |
    | accelerated     | [True]                               |
    +-----------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-if-modify controller-0 oam0 -c platform
    +-----------------+--------------------------------------+
    | Property        | Value                                |
    +-----------------+--------------------------------------+
    | ifname          | oam0                                 |
    | iftype          | ethernet                             |
    | ports           | [u'eno3']                            |
    | imac            | 24:6e:96:5d:0c:b2                    |
    | imtu            | 1500                                 |
    | ifclass         | platform                             |
    | aemode          | None                                 |
    | schedpolicy     | None                                 |
    | txhashpolicy    | None                                 |
    | uuid            | d8a048fa-67ef-43ac-8166-671be93caa30 |
    | ihost_uuid      | 16afe3a2-ba50-46b8-9fd7-09010059e8b9 |
    | vlan_id         | None                                 |
    | uses            | []                                   |
    | used_by         | []                                   |
    | created_at      | 2021-11-17T00:28:32.365863+00:00     |
    | updated_at      | 2021-11-17T01:03:49.368879+00:00     |
    | sriov_numvfs    | 0                                    |
    | sriov_vf_driver | None                                 |
    | accelerated     | [True]                               |
    +-----------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system interface-network-assign controller-0 oam0 oam
    +--------------+--------------------------------------+
    | Property     | Value                                |
    +--------------+--------------------------------------+
    | hostname     | controller-0                         |
    | uuid         | 3c8bd181-d3f3-4e14-8e89-75a3432db1e4 |
    | ifname       | oam0                                 |
    | network_name | oam                                  |
    +--------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-if-modify controller-0 pxeboot0 -c platform
    +-----------------+--------------------------------------+
    | Property        | Value                                |
    +-----------------+--------------------------------------+
    | ifname          | pxeboot0                             |
    | iftype          | ethernet                             |
    | ports           | [u'eno1']                            |
    | imac            | 24:6e:96:5d:0c:92                    |
    | imtu            | 1500                                 |
    | ifclass         | platform                             |
    | aemode          | None                                 |
    | schedpolicy     | None                                 |
    | txhashpolicy    | None                                 |
    | uuid            | 23b5e923-1e53-4e70-a975-542d8380b7f2 |
    | ihost_uuid      | 16afe3a2-ba50-46b8-9fd7-09010059e8b9 |
    | vlan_id         | None                                 |
    | uses            | []                                   |
    | used_by         | []                                   |
    | created_at      | 2021-11-17T00:28:32.612230+00:00     |
    | updated_at      | 2021-11-17T01:03:53.143795+00:00     |
    | sriov_numvfs    | 0                                    |
    | sriov_vf_driver | None                                 |
    | accelerated     | [True]                               |
    +-----------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system interface-network-assign controller-0  pxeboot0 pxeboot
    +--------------+--------------------------------------+
    | Property     | Value                                |
    +--------------+--------------------------------------+
    | hostname     | controller-0                         |
    | uuid         | 6c55622d-2da4-4f4e-ab5e-f8e06e03af7c |
    | ifname       | pxeboot0                             |
    | network_name | pxeboot                              |
    +--------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-if-add -V 18 controller-0 mgmt0 vlan pxeboot0
    +-----------------+--------------------------------------+
    | Property        | Value                                |
    +-----------------+--------------------------------------+
    | ifname          | mgmt0                                |
    | iftype          | vlan                                 |
    | ports           | []                                   |
    | imac            | 24:6e:96:5d:0c:92                    |
    | imtu            | 1500                                 |
    | ifclass         | None                                 |
    | aemode          | None                                 |
    | schedpolicy     | None                                 |
    | txhashpolicy    | None                                 |
    | uuid            | 119bdb85-1e24-44ff-b527-fe8f167b0ad3 |
    | ihost_uuid      | 16afe3a2-ba50-46b8-9fd7-09010059e8b9 |
    | vlan_id         | 18                                   |
    | uses            | [u'pxeboot0']                        |
    | used_by         | []                                   |
    | created_at      | 2021-11-17T01:03:57.303000+00:00     |
    | updated_at      | None                                 |
    | sriov_numvfs    | 0                                    |
    | sriov_vf_driver | None                                 |
    | accelerated     | [True]                               |
    +-----------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system interface-network-assign controller-0 mgmt0 mgmt
    +--------------+--------------------------------------+
    | Property     | Value                                |
    +--------------+--------------------------------------+
    | hostname     | controller-0                         |
    | uuid         | 2e93ef03-e9ee-457a-8667-05b52b7109a5 |
    | ifname       | mgmt0                                |
    | network_name | mgmt                                 |
    +--------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-if-add -V 19 controller-0 cluster0 vlan pxeboot0
    +-----------------+--------------------------------------+
    | Property        | Value                                |
    +-----------------+--------------------------------------+
    | ifname          | cluster0                             |
    | iftype          | vlan                                 |
    | ports           | []                                   |
    | imac            | 24:6e:96:5d:0c:92                    |
    | imtu            | 1500                                 |
    | ifclass         | None                                 |
    | aemode          | None                                 |
    | schedpolicy     | None                                 |
    | txhashpolicy    | None                                 |
    | uuid            | 6a620c8e-4f7b-4f74-a9f4-2a91d3ae9756 |
    | ihost_uuid      | 16afe3a2-ba50-46b8-9fd7-09010059e8b9 |
    | vlan_id         | 19                                   |
    | uses            | [u'pxeboot0']                        |
    | used_by         | []                                   |
    | created_at      | 2021-11-17T01:04:02.613518+00:00     |
    | updated_at      | None                                 |
    | sriov_numvfs    | 0                                    |
    | sriov_vf_driver | None                                 |
    | accelerated     | [True]                               |
    +-----------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system interface-network-assign controller-0 cluster0 cluster-host
    +--------------+--------------------------------------+
    | Property     | Value                                |
    +--------------+--------------------------------------+
    | hostname     | controller-0                         |
    | uuid         | fb8b6be6-1618-4662-b063-b1e8d340aa48 |
    | ifname       | cluster0                             |
    | network_name | cluster-host                         |
    +--------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-if-list controller-0
    +--------------------------------------+----------+----------+----------+---------+-----------+---------------+-------------------------+------------+
    | uuid                                 | name     | class    | type     | vlan id | ports     | uses i/f      | used by i/f             | attributes |
    +--------------------------------------+----------+----------+----------+---------+-----------+---------------+-------------------------+------------+
    | 119bdb85-1e24-44ff-b527-fe8f167b0ad3 | mgmt0    | platform | vlan     | 18      | []        | [u'pxeboot0'] | []                      | MTU=1500   |
    | 23b5e923-1e53-4e70-a975-542d8380b7f2 | pxeboot0 | platform | ethernet | None    | [u'eno1'] | []            | [u'mgmt0', u'cluster0'] | MTU=1500   |
    | 6a620c8e-4f7b-4f74-a9f4-2a91d3ae9756 | cluster0 | platform | vlan     | 19      | []        | [u'pxeboot0'] | []                      | MTU=1500   |
    | d8a048fa-67ef-43ac-8166-671be93caa30 | oam0     | platform | ethernet | None    | [u'eno3'] | []            | []                      | MTU=1500   |
    +--------------------------------------+----------+----------+----------+---------+-----------+---------------+-------------------------+------------+

Configure NTP Servers for network time synchronization:

::

    system ntp-modify ntpservers=0.pool.ntp.org,1.pool.ntp.org

Output

::

    [sysadmin@controller-0 ~(keystone_admin)]$ system ntp-modify ntpservers=0.pool.ntp.org,1.pool.ntp.org
    +--------------+--------------------------------------+
    | Property     | Value                                |
    +--------------+--------------------------------------+
    | uuid         | 3206cf01-c64a-457e-ac66-b8224c9684c3 |
    | ntpservers   | 0.pool.ntp.org,1.pool.ntp.org        |
    | isystem_uuid | cc79b616-d24e-4432-a953-85c9b242cb3a |
    | created_at   | 2021-11-17T00:27:23.529571+00:00     |
    | updated_at   | None                                 |
    +--------------+--------------------------------------+

Add an OSD on controller-0 for Ceph:

::

    system host-disk-list controller-0
    system host-disk-list controller-0 | awk '/\/dev\/sdb/{print $2}' | xargs -i system host-stor-add controller-0 {}
    system host-disk-list controller-0 | awk '/\/dev\/sdc/{print $2}' | xargs -i system host-stor-add controller-0 {}
    system host-stor-list controller-0

Output

::

    [sysadmin@controller-0 ~(keystone_admin)]$ system host-disk-list controller-0
    +--------------------------------------+-----------+---------+---------+-------+------------+--------------+----------------------------------+-------------------------------------------------+
    | uuid                                 | device_no | device_ | device_ | size_ | available_ | rpm          | serial_id                        | device_path                                     |
    |                                      | de        | num     | type    | gib   | gib        |              |                                  |                                                 |
    +--------------------------------------+-----------+---------+---------+-------+------------+--------------+----------------------------------+-------------------------------------------------+
    | 8e2a719a-fa5a-4c25-89af-70a23fb7b238 | /dev/sda  | 2048    | HDD     | 893.  | 644.726    | Undetermined | 00c66a07604fa8de2500151b14604609 | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:0:0 |
    |                                      |           |         |         | 75    |            |              |                                  |                                                 |
    |                                      |           |         |         |       |            |              |                                  |                                                 |
    | 61b6f262-a51f-4310-aeac-373b1c1bbbc2 | /dev/sdb  | 2064    | HDD     | 1117. | 1117.247   | Undetermined | 00c6b9139b76a8de2500151b14604609 | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:1:0 |
    |                                      |           |         |         | 25    |            |              |                                  |                                                 |
    |                                      |           |         |         |       |            |              |                                  |                                                 |
    | 81a7f4f9-dd3a-49b5-80d9-e1953aa43c79 | /dev/sdc  | 2080    | HDD     | 1117. | 1117.247   | Undetermined | 0053be63c794a8de2500151b14604609 | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:2:0 |
    |                                      |           |         |         | 25    |            |              |                                  |                                                 |
    |                                      |           |         |         |       |            |              |                                  |                                                 |
    | 4879b381-8e9f-48f3-84e2-f9c6a94bbfe0 | /dev/sdd  | 2096    | HDD     | 1117. | 0.0        | Undetermined | 0065482503bca8de2500151b14604609 | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:3:0 |
    |                                      |           |         |         | 25    |            |              |                                  |                                                 |
    |                                      |           |         |         |       |            |              |                                  |                                                 |
    +--------------------------------------+-----------+---------+---------+-------+------------+--------------+----------------------------------+-------------------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-disk-list controller-0 | awk '/\/dev\/sdb/{print $2}' | xargs -i system host-stor-add controller-0 {}
    +------------------+-------------------------------------------------------+
    | Property         | Value                                                 |
    +------------------+-------------------------------------------------------+
    | osdid            | 0                                                     |
    | function         | osd                                                   |
    | state            | configuring-on-unlock                                 |
    | journal_location | 0816c72f-a4f0-49ea-9a95-0f02c880717c                  |
    | journal_size_gib | 1024                                                  |
    | journal_path     | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:1:0-part2 |
    | journal_node     | /dev/sdb2                                             |
    | uuid             | 0816c72f-a4f0-49ea-9a95-0f02c880717c                  |
    | ihost_uuid       | 16afe3a2-ba50-46b8-9fd7-09010059e8b9                  |
    | idisk_uuid       | 61b6f262-a51f-4310-aeac-373b1c1bbbc2                  |
    | tier_uuid        | 3af8c893-9dd4-40af-afc6-30bb79048448                  |
    | tier_name        | storage                                               |
    | created_at       | 2021-11-17T01:05:04.063823+00:00                      |
    | updated_at       | None                                                  |
    +------------------+-------------------------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-disk-list controller-0 | awk '/\/dev\/sdc/{print $2}' | xargs -i system host-stor-add controller-0 {}
    +------------------+-------------------------------------------------------+
    | Property         | Value                                                 |
    +------------------+-------------------------------------------------------+
    | osdid            | 1                                                     |
    | function         | osd                                                   |
    | state            | configuring-on-unlock                                 |
    | journal_location | 7a0b3727-0e3f-4582-9415-56e44bb8f1e5                  |
    | journal_size_gib | 1024                                                  |
    | journal_path     | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:2:0-part2 |
    | journal_node     | /dev/sdc2                                             |
    | uuid             | 7a0b3727-0e3f-4582-9415-56e44bb8f1e5                  |
    | ihost_uuid       | 16afe3a2-ba50-46b8-9fd7-09010059e8b9                  |
    | idisk_uuid       | 81a7f4f9-dd3a-49b5-80d9-e1953aa43c79                  |
    | tier_uuid        | 3af8c893-9dd4-40af-afc6-30bb79048448                  |
    | tier_name        | storage                                               |
    | created_at       | 2021-11-17T01:05:06.939798+00:00                      |
    | updated_at       | None                                                  |
    +------------------+-------------------------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-stor-list controller-0
    +--------------------------------------+----------+-------+-----------------------+--------------------------------------+-------------------------------------------------------+--------------+------------------+-----------+
    | uuid                                 | function | osdid | state                 | idisk_uuid                           | journal_path                                          | journal_node | journal_size_gib | tier_name |
    +--------------------------------------+----------+-------+-----------------------+--------------------------------------+-------------------------------------------------------+--------------+------------------+-----------+
    | 0816c72f-a4f0-49ea-9a95-0f02c880717c | osd      | 0     | configuring-on-unlock | 61b6f262-a51f-4310-aeac-373b1c1bbbc2 | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:1:0-part2 | /dev/sdb2    | 1                | storage   |
    | 7a0b3727-0e3f-4582-9415-56e44bb8f1e5 | osd      | 1     | configuring-on-unlock | 81a7f4f9-dd3a-49b5-80d9-e1953aa43c79 | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:2:0-part2 | /dev/sdc2    | 1                | storage   |
    +--------------------------------------+----------+-------+-----------------------+--------------------------------------+-------------------------------------------------------+--------------+------------------+-----------+

2.7 Unlock controller-0
~~~~~~~~~~~~~~~~~~~~~~~

::

    system host-unlock controller-0

Output:

::

    [sysadmin@controller-0 ~(keystone_admin)]$ system host-unlock controller-0
    +-----------------------+-------------------------------------------------+
    | Property              | Value                                           |
    +-----------------------+-------------------------------------------------+
    | action                | none                                            |
    | administrative        | locked                                          |
    | availability          | online                                          |
    | bm_ip                 | None                                            |
    | bm_type               | none                                            |
    | bm_username           | None                                            |
    | boot_device           | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:0:0 |
    | capabilities          | {u'stor_function': u'monitor'}                  |
    | clock_synchronization | ntp                                             |
    | config_applied        | 6aa15fb4-8cb3-494e-b94e-95f85b560f22            |
    | config_status         | None                                            |
    | config_target         | c6ae9b2d-a3c4-4751-a79e-5487ba81ed82            |
    | console               | ttyS0,115200                                    |
    | created_at            | 2021-11-17T00:28:01.983673+00:00                |
    | hostname              | controller-0                                    |
    | id                    | 1                                               |
    | install_output        | graphical                                       |
    | install_state         | None                                            |
    | install_state_info    | None                                            |
    | inv_state             | inventoried                                     |
    | invprovision          | provisioning                                    |
    | location              | {}                                              |
    | mgmt_ip               | 192.168.18.3                                    |
    | mgmt_mac              | 24:6e:96:5d:0c:92                               |
    | operational           | disabled                                        |
    | personality           | controller                                      |
    | reserved              | False                                           |
    | rootfs_device         | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:0:0 |
    | serialid              | None                                            |
    | software_load         | 21.05                                           |
    | subfunction_avail     | online                                          |
    | subfunction_oper      | disabled                                        |
    | subfunctions          | controller,worker,lowlatency                    |
    | task                  | Unlocking                                       |
    | tboot                 | false                                           |
    | ttys_dcd              | None                                            |
    | updated_at            | 2021-11-17T01:05:07.015414+00:00                |
    | uptime                | 3496                                            |
    | uuid                  | 16afe3a2-ba50-46b8-9fd7-09010059e8b9            |
    | vim_progress_status   | None                                            |
    +-----------------------+-------------------------------------------------+

Controller-0 will reboot to apply configuration changes and come into
service. This can take 5-10 minutes, depending on the performance of the
host machine.

Once the controller comes back up, check the status of controller-0

::

    controller-0:~$ source /etc/platform/openrc
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-list
    +----+--------------+-------------+----------------+-------------+--------------+
    | id | hostname     | personality | administrative | operational | availability |
    +----+--------------+-------------+----------------+-------------+--------------+
    | 1  | controller-0 | controller  | unlocked       | enabled     | available    |
    +----+--------------+-------------+----------------+-------------+--------------+

2. Installation for the second server (controller-1)
----------------------------------------------------

2.1 Power on the controller-1 server and force it to network boot
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

2.2 As controller-1 boots, a message appears on its console instructing you to configure the personality of the node
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

2.3 On the console of controller-0, list hosts to see newly discovered controller-1 host (hostname=None)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

::

    [sysadmin@controller-0 ~(keystone_admin)]$ system host-list
    +----+--------------+-------------+----------------+-------------+--------------+
    | id | hostname     | personality | administrative | operational | availability |
    +----+--------------+-------------+----------------+-------------+--------------+
    | 1  | controller-0 | controller  | unlocked       | enabled     | degraded     |
    | 2  | None         | None        | locked         | disabled    | offline      |
    +----+--------------+-------------+----------------+-------------+--------------+

2.4 Using the host id, set the personality of this host to 'controller’:
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

::

    [sysadmin@controller-0 ~(keystone_admin)]$ system host-update 2 personality=controller
    +-----------------------+--------------------------------------+
    | Property              | Value                                |
    +-----------------------+--------------------------------------+
    | action                | none                                 |
    | administrative        | locked                               |
    | availability          | offline                              |
    | bm_ip                 | None                                 |
    | bm_type               | None                                 |
    | bm_username           | None                                 |
    | boot_device           | /dev/sda                             |
    | capabilities          | {}                                   |
    | clock_synchronization | ntp                                  |
    | config_applied        | None                                 |
    | config_status         | None                                 |
    | config_target         | None                                 |
    | console               | ttyS0,115200                         |
    | created_at            | 2021-11-17T10:17:44.387813+00:00     |
    | hostname              | controller-1                         |
    | id                    | 2                                    |
    | install_output        | text                                 |
    | install_state         | None                                 |
    | install_state_info    | None                                 |
    | inv_state             | None                                 |
    | invprovision          | None                                 |
    | location              | {}                                   |
    | mgmt_ip               | 192.168.18.4                         |
    | mgmt_mac              | 24:6e:96:5d:38:ee                    |
    | operational           | disabled                             |
    | personality           | controller                           |
    | reserved              | False                                |
    | rootfs_device         | /dev/sda                             |
    | serialid              | None                                 |
    | software_load         | 21.05                                |
    | subfunction_avail     | not-installed                        |
    | subfunction_oper      | disabled                             |
    | subfunctions          | controller,worker,lowlatency         |
    | task                  | None                                 |
    | tboot                 | false                                |
    | ttys_dcd              | None                                 |
    | updated_at            | None                                 |
    | uptime                | 0                                    |
    | uuid                  | f069381d-9743-49cc-bf8b-eb4bd3972203 |
    | vim_progress_status   | None                                 |
    +-----------------------+--------------------------------------+

2.5 Wait for the software installation on controller-1 to complete, for controller-1 to reboot, and for controller-1 to show as locked/disabled/online in 'system host-list'.
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This can take 5-10 minutes, depending on the performance of the host
machine.

::

    [root@controller-0 hieradata(keystone_admin)]$ system host-list
    +----+--------------+-------------+----------------+-------------+--------------+
    | id | hostname     | personality | administrative | operational | availability |
    +----+--------------+-------------+----------------+-------------+--------------+
    | 1  | controller-0 | controller  | unlocked       | enabled     | available    |
    | 2  | controller-1 | controller  | locked         | disabled    | online       |
    +----+--------------+-------------+----------------+-------------+--------------+

2.6 Configure controller-1
~~~~~~~~~~~~~~~~~~~~~~~~~~

::

    OAM_IF=eno3
    MGMT_IF=eno1
    system host-if-modify controller-1 $OAM_IF -n oam0
    system host-if-modify controller-1 oam0 -c platform
    system interface-network-assign controller-1 oam0 oam

    system host-if-add         -V 19 controller-1 cluster0 vlan pxeboot0
    system interface-network-assign controller-1 cluster0 cluster-host

    system host-if-list controller-1

    system host-disk-list controller-1
    system host-disk-list controller-1 | awk '/\/dev\/sdb/{print $2}' | xargs -i system host-stor-add controller-1 {}
    system host-disk-list controller-1 | awk '/\/dev\/sdc/{print $2}' | xargs -i system host-stor-add controller-1 {}
    system host-stor-list controller-1

Output:

::

    [sysadmin@controller-0 ~(keystone_admin)]$ OAM_IF=eno3
    [sysadmin@controller-0 ~(keystone_admin)]$ MGMT_IF=eno1
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-if-modify controller-1 $OAM_IF -n oam0
    +-----------------+--------------------------------------+
    | Property        | Value                                |
    +-----------------+--------------------------------------+
    | ifname          | oam0                                 |
    | iftype          | ethernet                             |
    | ports           | [u'eno3']                            |
    | imac            | 24:6e:96:5d:39:0e                    |
    | imtu            | 1500                                 |
    | ifclass         | None                                 |
    | aemode          | None                                 |
    | schedpolicy     | None                                 |
    | txhashpolicy    | None                                 |
    | uuid            | c2473511-d0d6-445d-9739-4d43dc029de9 |
    | ihost_uuid      | 63c930c7-2195-4d5a-870c-be610fd6b4fc |
    | vlan_id         | None                                 |
    | uses            | []                                   |
    | used_by         | []                                   |
    | created_at      | 2021-11-22T14:01:32.365863+00:00     |
    | updated_at      | 2021-11-22T15:04:45.090904+00:00     |
    | sriov_numvfs    | 0                                    |
    | sriov_vf_driver | None                                 |
    | accelerated     | [True]                               |
    +-----------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-if-modify controller-1 oam0 -c platform
    +-----------------+--------------------------------------+
    | Property        | Value                                |
    +-----------------+--------------------------------------+
    | ifname          | oam0                                 |
    | iftype          | ethernet                             |
    | ports           | [u'eno3']                            |
    | imac            | 24:6e:96:5d:39:0e                    |
    | imtu            | 1500                                 |
    | ifclass         | platform                             |
    | aemode          | None                                 |
    | schedpolicy     | None                                 |
    | txhashpolicy    | None                                 |
    | uuid            | c2473511-d0d6-445d-9739-4d43dc029de9 |
    | ihost_uuid      | 63c930c7-2195-4d5a-870c-be610fd6b4fc |
    | vlan_id         | None                                 |
    | uses            | []                                   |
    | used_by         | []                                   |
    | created_at      | 2021-11-22T14:05:16.052229+00:00     |
    | updated_at      | 2021-11-22T15:08:35.324634+00:00     |
    | sriov_numvfs    | 0                                    |
    | sriov_vf_driver | None                                 |
    | accelerated     | [True]                               |
    +-----------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system interface-network-assign controller-1 oam0 oam
    +--------------+--------------------------------------+
    | Property     | Value                                |
    +--------------+--------------------------------------+
    | hostname     | controller-1                         |
    | uuid         | f2e7f088-0dd0-4adc-8348-4e3cef23bc47 |
    | ifname       | oam0                                 |
    | network_name | oam                                  |
    +--------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$

    [sysadmin@controller-0 ~(keystone_admin)]$ system host-if-add -V 19 controller-1 cluster0 vlan pxeboot0
    +-----------------+--------------------------------------+
    | Property        | Value                                |
    +-----------------+--------------------------------------+
    | ifname          | cluster0                             |
    | iftype          | vlan                                 |
    | ports           | []                                   |
    | imac            | 24:6e:96:5d:38:ee                    |
    | imtu            | 1500                                 |
    | ifclass         | None                                 |
    | aemode          | None                                 |
    | schedpolicy     | None                                 |
    | txhashpolicy    | None                                 |
    | uuid            | b6783682-b2aa-4135-90d2-676e1db41ae8 |
    | ihost_uuid      | 63c930c7-2195-4d5a-870c-be610fd6b4fc |
    | vlan_id         | 19                                   |
    | uses            | [u'pxeboot0']                        |
    | used_by         | []                                   |
    | created_at      | 2021-11-22T15:08:43.932209+00:00     |
    | updated_at      | None                                 |
    | sriov_numvfs    | 0                                    |
    | sriov_vf_driver | None                                 |
    | accelerated     | [True]                               |
    +-----------------+--------------------------------------+

    [sysadmin@controller-0 ~(keystone_admin)]$ system interface-network-assign controller-1 cluster0 cluster-host
    +--------------+--------------------------------------+
    | Property     | Value                                |
    +--------------+--------------------------------------+
    | hostname     | controller-1                         |
    | uuid         | 8fc64805-b54b-45a4-b88a-e13b236abfe8 |
    | ifname       | cluster0                             |
    | network_name | cluster-host                         |
    +--------------+--------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-if-list controller-1
    +--------------------------------------+----------+----------+----------+---------+-----------+---------------+-------------------------+------------+
    | uuid                                 | name     | class    | type     | vlan id | ports     | uses i/f      | used by i/f             | attributes |
    +--------------------------------------+----------+----------+----------+---------+-----------+---------------+-------------------------+------------+
    | b6783682-b2aa-4135-90d2-676e1db41ae8 | cluster0 | platform | vlan     | 19      | []        | [u'pxeboot0'] | []                      | MTU=1500   |
    | b8921960-fde5-44c3-960d-2aebf42ea400 | pxeboot0 | platform | ethernet | None    | [u'eno1'] | []            | [u'mgmt0', u'cluster0'] | MTU=1500   |
    | c103275b-2b75-4568-865f-ac6be32ecb2d | mgmt0    | platform | vlan     | 18      | []        | [u'pxeboot0'] | []                      | MTU=1500   |
    | c2473511-d0d6-445d-9739-4d43dc029de9 | oam0     | platform | ethernet | None    | [u'eno3'] | []            | []                      | MTU=1500   |
    +--------------------------------------+----------+----------+----------+---------+-----------+---------------+-------------------------+------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-disk-list controller-1
    +--------------------------------------+-----------+---------+---------+-------+------------+--------------+----------------------------------+-------------------------------------------------+
    | uuid                                 | device_no | device_ | device_ | size_ | available_ | rpm          | serial_id                        | device_path                                     |
    |                                      | de        | num     | type    | gib   | gib        |              |                                  |                                                 |
    +--------------------------------------+-----------+---------+---------+-------+------------+--------------+----------------------------------+-------------------------------------------------+
    | 5b8fade4-b048-48fa-b906-9dcbdbed8e96 | /dev/sda  | 2048    | HDD     | 893.  | 644.726    | Undetermined | 00cbd97f3e36ccfa2500561b14604609 | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:0:0 |
    |                                      |           |         |         | 75    |            |              |                                  |                                                 |
    |                                      |           |         |         |       |            |              |                                  |                                                 |
    | 1a3f0a36-5961-42e5-a271-e71db1c25d42 | /dev/sdb  | 2064    | HDD     | 1117. | 1117.247   | Undetermined | 006d0e977b5fccfa2500561b14604609 | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:1:0 |
    |                                      |           |         |         | 25    |            |              |                                  |                                                 |
    |                                      |           |         |         |       |            |              |                                  |                                                 |
    | eddd732f-2cea-49b3-86db-b722c0b1a1ae | /dev/sdc  | 2080    | HDD     | 1117. | 1117.247   | Undetermined | 003a2377ac7fccfa2500561b14604609 | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:2:0 |
    |                                      |           |         |         | 25    |            |              |                                  |                                                 |
    |                                      |           |         |         |       |            |              |                                  |                                                 |
    | 774c3cd0-1178-4145-9573-f0d6dee2ba06 | /dev/sdd  | 2096    | HDD     | 1117. | 1117.247   | Undetermined | 00d7093ef0adccfa2500561b14604609 | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:3:0 |
    |                                      |           |         |         | 25    |            |              |                                  |                                                 |
    |                                      |           |         |         |       |            |              |                                  |                                                 |
    | 00361302-8d55-4730-855c-b0098c73ab7e | /dev/sde  | 2112    | SSD     | 223.  | 223.568    | N/A          | PHDW730104QM240E                 | /dev/disk/by-path/pci-0000:d8:00.0-ata-1        |
    |                                      |           |         |         | 57    |            |              |                                  |                                                 |
    |                                      |           |         |         |       |            |              |                                  |                                                 |
    | 7ce735e6-920f-4424-a890-a7a7f48d7632 | /dev/sdf  | 2128    | SSD     | 223.  | 223.568    | N/A          | PHDW730104LL240E                 | /dev/disk/by-path/pci-0000:d8:00.0-ata-2        |
    |                                      |           |         |         | 57    |            |              |                                  |                                                 |
    |                                      |           |         |         |       |            |              |                                  |                                                 |
    +--------------------------------------+-----------+---------+---------+-------+------------+--------------+----------------------------------+-------------------------------------------------+
    [sysadmin@controller-0 ~(keystone_admin)]$ system host-disk-list controller-1 | awk '/\/dev\/sdb/{print $2}' | xargs -i system host-stor-add controller-1 {}
    +------------------+-------------------------------------------------------+
    | Property         | Value                                                 |
    +------------------+-------------------------------------------------------+
    | osdid            | 2                                                     |
    | function         | osd                                                   |
    | state            | configuring-on-unlock                                 |
    | journal_location | 54a218d8-0466-4366-9ef0-3ec5a952fde7                  |
    | journal_size_gib | 1024                                                  |
    | journal_path     | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:1:0-part2 |
    | journal_node     | /dev/sdb2                                             |
    | uuid             | 54a218d8-0466-4366-9ef0-3ec5a952fde7                  |
    | ihost_uuid       | 63c930c7-2195-4d5a-870c-be610fd6b4fc                  |
    | idisk_uuid       | 1a3f0a36-5961-42e5-a271-e71db1c25d42                  |
    | tier_uuid        | 06b4740e-29db-4896-9600-03ee40fe0d6c                  |
    | tier_name        | storage                                               |
    | created_at       | 2021-11-22T15:11:55.641193+00:00                      |
    | updated_at       | None                                                  |
    +------------------+-------------------------------------------------------+

    [sysadmin@controller-0 ~(keystone_admin)]$ system host-disk-list controller-1 | awk '/\/dev\/sdc/{print $2}' | xargs -i system host-stor-add controller-1 {}
    +------------------+-------------------------------------------------------+
    | Property         | Value                                                 |
    +------------------+-------------------------------------------------------+
    | osdid            | 3                                                     |
    | function         | osd                                                   |
    | state            | configuring-on-unlock                                 |
    | journal_location | 5be88c7a-3a94-4b97-9da5-b247bb89406c                  |
    | journal_size_gib | 1024                                                  |
    | journal_path     | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:2:0-part2 |
    | journal_node     | /dev/sdc2                                             |
    | uuid             | 5be88c7a-3a94-4b97-9da5-b247bb89406c                  |
    | ihost_uuid       | 63c930c7-2195-4d5a-870c-be610fd6b4fc                  |
    | idisk_uuid       | eddd732f-2cea-49b3-86db-b722c0b1a1ae                  |
    | tier_uuid        | 06b4740e-29db-4896-9600-03ee40fe0d6c                  |
    | tier_name        | storage                                               |
    | created_at       | 2021-11-22T15:12:04.274839+00:00                      |
    | updated_at       | None                                                  |
    +------------------+-------------------------------------------------------+

    [sysadmin@controller-0 ~(keystone_admin)]$ system host-stor-list controller-1
    +--------------------------------------+----------+-------+-----------------------+--------------------------------------+-------------------------------------------------------+--------------+------------------+-----------+
    | uuid                                 | function | osdid | state                 | idisk_uuid                           | journal_path                                          | journal_node | journal_size_gib | tier_name |
    +--------------------------------------+----------+-------+-----------------------+--------------------------------------+-------------------------------------------------------+--------------+------------------+-----------+
    | 54a218d8-0466-4366-9ef0-3ec5a952fde7 | osd      | 2     | configuring-on-unlock | 1a3f0a36-5961-42e5-a271-e71db1c25d42 | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:1:0-part2 | /dev/sdb2    | 1                | storage   |
    | 5be88c7a-3a94-4b97-9da5-b247bb89406c | osd      | 3     | configuring-on-unlock | eddd732f-2cea-49b3-86db-b722c0b1a1ae | /dev/disk/by-path/pci-0000:86:00.0-scsi-0:2:2:0-part2 | /dev/sdc2    | 1                | storage   |
    +--------------------------------------+----------+-------+-----------------------+--------------------------------------+-------------------------------------------------------+--------------+------------------+-----------+

2.7 Unlock controller-1
~~~~~~~~~~~~~~~~~~~~~~~

Unlock controller-1 in order to bring it into service:

::

    [sysadmin@controller-0 ~(keystone_admin)]$ system host-unlock controller-1
    +-----------------------+--------------------------------------+
    | Property              | Value                                |
    +-----------------------+--------------------------------------+
    | action                | none                                 |
    | administrative        | locked                               |
    | availability          | online                               |
    | bm_ip                 | None                                 |
    | bm_type               | None                                 |
    | bm_username           | None                                 |
    | boot_device           | /dev/sda                             |
    | capabilities          | {u'stor_function': u'monitor'}       |
    | clock_synchronization | ntp                                  |
    | config_applied        | None                                 |
    | config_status         | Config out-of-date                   |
    | config_target         | 9747e0ce-2319-409d-b75c-2475bc5065ac |
    | console               | ttyS0,115200                         |
    | created_at            | 2021-11-22T12:58:11.630526+00:00     |
    | hostname              | controller-1                         |
    | id                    | 3                                    |
    | install_output        | text                                 |
    | install_state         | None                                 |
    | install_state_info    | None                                 |
    | inv_state             | inventoried                          |
    | invprovision          | unprovisioned                        |
    | location              | {}                                   |
    | mgmt_ip               | 192.168.18.4                         |
    | mgmt_mac              | 24:6e:96:5d:38:ee                    |
    | operational           | disabled                             |
    | personality           | controller                           |
    | reserved              | False                                |
    | rootfs_device         | /dev/sda                             |
    | serialid              | None                                 |
    | software_load         | 21.05                                |
    | subfunction_avail     | online                               |
    | subfunction_oper      | disabled                             |
    | subfunctions          | controller,worker,lowlatency         |
    | task                  | Unlocking                            |
    | tboot                 | false                                |
    | ttys_dcd              | None                                 |
    | updated_at            | 2021-11-22T15:13:09.716324+00:00     |
    | uptime                | 752                                  |
    | uuid                  | 63c930c7-2195-4d5a-870c-be610fd6b4fc |
    | vim_progress_status   | None                                 |
    +-----------------------+--------------------------------------+

Controller-1 will reboot in order to apply configuration changes and
come into service. This can take 5-10 minutes, depending on the
performance of the host machine.

::

    [root@controller-0 hieradata(keystone_admin)]$ system host-list
    +----+--------------+-------------+----------------+-------------+--------------+
    | id | hostname     | personality | administrative | operational | availability |
    +----+--------------+-------------+----------------+-------------+--------------+
    | 1  | controller-0 | controller  | unlocked       | enabled     | available    |
    | 2  | controller-1 | controller  | unlocked       | enabled     | available    |
    +----+--------------+-------------+----------------+-------------+--------------+

    [sysadmin@controller-0 ~(keystone_admin)]$ system host-show controller-1
    +-----------------------+-----------------------------------------------------------------------+
    | Property              | Value                                                                 |
    +-----------------------+-----------------------------------------------------------------------+
    | action                | none                                                                  |
    | administrative        | unlocked                                                              |
    | availability          | available                                                             |
    | bm_ip                 | None                                                                  |
    | bm_type               | None                                                                  |
    | bm_username           | None                                                                  |
    | boot_device           | /dev/sda                                                              |
    | capabilities          | {u'stor_function': u'monitor', u'Personality': u'Controller-Standby'} |
    | clock_synchronization | ntp                                                                   |
    | config_applied        | 9747e0ce-2319-409d-b75c-2475bc5065ac                                  |
    | config_status         | None                                                                  |
    | config_target         | 9747e0ce-2319-409d-b75c-2475bc5065ac                                  |
    | console               | ttyS0,115200                                                          |
    | created_at            | 2021-11-22T12:58:11.630526+00:00                                      |
    | hostname              | controller-1                                                          |
    | id                    | 2                                                                     |
    | install_output        | text                                                                  |
    | install_state         | None                                                                  |
    | install_state_info    | None                                                                  |
    | inv_state             | inventoried                                                           |
    | invprovision          | provisioned                                                           |
    | location              | {}                                                                    |
    | mgmt_ip               | 192.168.18.4                                                          |
    | mgmt_mac              | 24:6e:96:5d:38:ee                                                     |
    | operational           | enabled                                                               |
    | personality           | controller                                                            |
    | reserved              | False                                                                 |
    | rootfs_device         | /dev/sda                                                              |
    | serialid              | None                                                                  |
    | software_load         | 21.05                                                                 |
    | subfunction_avail     | available                                                             |
    | subfunction_oper      | enabled                                                               |
    | subfunctions          | controller,worker,lowlatency                                          |
    | task                  |                                                                       |
    | tboot                 | false                                                                 |
    | ttys_dcd              | None                                                                  |
    | updated_at            | 2021-11-22T23:59:07.787759+00:00                                      |
    | uptime                | 31008                                                                 |
    | uuid                  | 63c930c7-2195-4d5a-870c-be610fd6b4fc                                  |
    | vim_progress_status   | services-enabled                                                      |
    +-----------------------+-----------------------------------------------------------------------+

  
3. Simple use case for sriov
````````````````````````````

3.1 After controller-0 is rebooted and up running, download the DPDK
''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''

::

  [sysadmin@controller-0 ~(keystone_admin)]$ cd /opt
  [sysadmin@controller-0 opt(keystone_admin)]$ sudo wget https://fast.dpdk.org/rel/dpdk-17.11.10.tar.xz
  Password:
  --2021-06-04 02:35:30--  https://fast.dpdk.org/rel/dpdk-17.11.10.tar.xz
  Resolving fast.dpdk.org... 151.101.2.49, 151.101.66.49, 151.101.130.49, ...
  Connecting to fast.dpdk.org|151.101.2.49|:443... connected.
  
  HTTP request sent, awaiting response... 200 OK
  Length: 10251680 (9.8M) [application/octet-stream]
  Saving to: ‘dpdk-17.11.10.tar.xz’
  
  dpdk-17.11.10.tar.xz                        100% 
  [========================================================================================>]   9.78M  
  1.48MB/s    in 6.8s

  2021-06-04 02:35:40 (1.43 MB/s) - ‘dpdk-17.11.10.tar.xz’ saved [10251680/10251680]

  sudo tar xvf dpdk-17.11.10.tar.xz

  sudo ln -s dpdk-stable-17.11.10 dpdk-stable

3.2 Prepare the yaml file for the network assignment container
''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''

The following the exmaple of the yaml file:

::

  [sysadmin@controller-0 sriov(keystone_admin)]$ cat <<EOF > netdef-data-dpdk.yaml
  > apiVersion: "k8s.cni.cncf.io/v1"
  > kind: NetworkAttachmentDefinition
  > metadata:
  >   name: sriov-data-dpdk-0
  >   annotations:
  >     k8s.v1.cni.cncf.io/resourceName: intel.com/pci_sriov_net_physnet0
  > spec:
  >   config: '{
  >   "type": "sriov",
  >   "name": "sriov-data-dpdk-0"
  > }'
  >
  > ---
  > apiVersion: "k8s.cni.cncf.io/v1"
  > kind: NetworkAttachmentDefinition
  > metadata:
  >   name: sriov-data-dpdk-1
  >   annotations:
  >     k8s.v1.cni.cncf.io/resourceName: intel.com/pci_sriov_net_physnet1
  > spec:
  >   config: '{
  >   "type": "sriov",
  >   "name": "sriov-data-dpdk-1"
  > }'
  > EOF

3.3 Run the network assignent container for the 2 VFs
'''''''''''''''''''''''''''''''''''''''''''''''''''''

::

  [sysadmin@controller-0 sriov(keystone_admin)]$ kubectl create -f netdef-data-dpdk.yaml
  networkattachmentdefinition.k8s.cni.cncf.io/sriov-data-dpdk-0 created
  networkattachmentdefinition.k8s.cni.cncf.io/sriov-data-dpdk-1 created

3.4 Prepare the VF container yaml file
''''''''''''''''''''''''''''''''''''''

::

  [sysadmin@controller-0 sriov(keystone_admin)]$ cat <<EOF > pod-with-dpdk-vfs-0.yaml
  > apiVersion: v1
  > kind: Pod
  metadata:
  > metadata:
  >   name: pod-with-dpdk-vfs-0
  >   annotations:
  >     k8s.v1.cni.cncf.io/networks: '[
  >             { "name": "sriov-data-dpdk-0" },
              { "name": "sriov-data-dpdk-1" }
  >             { "name": "sriov-data-dpdk-1" }
  >     ]'
  > spec:
  >   restartPolicy: Never
  >   containers:
  >   - name: pod-with-dpdk-vfs-0
  >     image: wrsnfv/ubuntu-dpdk-build:v0.3
  >     env:
  >     - name: RTE_SDK
  >       value: "/usr/src/dpdk"
  >     command:
  >     - sleep
  >     - infinity
  >     stdin: true
  >     tty: true
  >     securityContext:
  >       privileged: true
  >       capabilities:
  >         add:
  >         - ALL
  >     resources:
  >       requests:
  >         cpu: 4
  >         memory: 4Gi
  >         intel.com/pci_sriov_net_physnet0: '1'
  >         intel.com/pci_sriov_net_physnet1: '1'
  >       limits:
  >         cpu: 4
  >         hugepages-1Gi: 2Gi
  >         memory: 4Gi
  >         intel.com/pci_sriov_net_physnet0: '1'
  >         intel.com/pci_sriov_net_physnet1: '1'
  >     volumeMounts:
  >     - mountPath: /mnt/huge-1048576kB
  >       name: hugepage
  >     - name: dpdk-volume
  >       mountPath: /usr/src/dpdk
  >     - name: lib-volume
  >       mountPath: /lib/modules
  >     - name: src-volume
  >       mountPath: /usr/src/
  >   volumes:
  >   - name: hugepage
  >     emptyDir:
  >       medium: HugePages
  >   - name: dpdk-volume
  >     hostPath:
  >       path: /opt/dpdk-stable/
  >   - name: lib-volume
  >     hostPath:
  >       path: /lib/modules
  >   - name: src-volume
  >     hostPath:
  >       path: /usr/src/
  > EOF

3.5 Run the VF container
''''''''''''''''''''''''

Start the VF container.

::

  [sysadmin@controller-0 sriov(keystone_admin)]$ kubectl create -f pod-with-dpdk-vfs-0.yaml
  pod/pod-with-dpdk-vfs-0 created

  [sysadmin@controller-0 sriov(keystone_admin)]$ kubectl get pod
  NAME                  READY   STATUS    RESTARTS   AGE
  pod-with-dpdk-vfs-0   1/1     Running   0          6m40s

Login the VF container

::

  kubectl exec -it pod-with-dpdk-vfs-0 -- bash

Build the DPDK

::

  cd /lib/modules/5.0.19-rt11-yocto-preempt-rt/build

  root@pod-with-dpdk-vfs-0:/lib/modules/5.0.19-rt11-yocto-preempt-rt/build# make prepare
    HOSTCC  scripts/basic/fixdep
    HOSTCC  scripts/kconfig/conf.o
    HOSTCC  scripts/kconfig/confdata.o
    HOSTCC  scripts/kconfig/expr.o
    HOSTCC  scripts/kconfig/symbol.o
    HOSTCC  scripts/kconfig/preprocess.o
    HOSTCC  scripts/kconfig/zconf.lex.o
    HOSTCC  scripts/kconfig/zconf.tab.o
    HOSTLD  scripts/kconfig/conf
  scripts/kconfig/conf  --syncconfig Kconfig
    HOSTCC  arch/x86/tools/relocs_32.o
    HOSTCC  arch/x86/tools/relocs_64.o
    HOSTCC  arch/x86/tools/relocs_common.o
    HOSTLD  arch/x86/tools/relocs
    HOSTCC  scripts/genksyms/genksyms.o
    YACC    scripts/genksyms/parse.tab.c
    HOSTCC  scripts/genksyms/parse.tab.o
    LEX     scripts/genksyms/lex.lex.c
    YACC    scripts/genksyms/parse.tab.h
    HOSTCC  scripts/genksyms/lex.lex.o
    HOSTLD  scripts/genksyms/genksyms
    HOSTCC  scripts/bin2c
    HOSTCC  scripts/kallsyms
    HOSTCC  scripts/conmakehash
    HOSTCC  scripts/recordmcount
    HOSTCC  scripts/sortextable
    HOSTCC  scripts/asn1_compiler
    HOSTCC  scripts/sign-file
    HOSTCC  scripts/extract-cert
    CC      scripts/mod/empty.o
    HOSTCC  scripts/mod/mk_elfconfig
    MKELF   scripts/mod/elfconfig.h
    HOSTCC  scripts/mod/modpost.o
    CC      scripts/mod/devicetable-offsets.s
    UPD     scripts/mod/devicetable-offsets.h
    HOSTCC  scripts/mod/file2alias.o
    HOSTCC  scripts/mod/sumversion.o
    HOSTLD  scripts/mod/modpost
    CC      kernel/bounds.s
    CC      arch/x86/kernel/asm-offsets.s
    CALL    scripts/checksyscalls.sh

Build the test_pmd application

::

  cd $RTE_SDK
  ./usertools/dpdk-setup.sh
  Option: 14
    CC config.o
    CC iofwd.o
    CC macfwd.o
    CC macswap.o
    CC flowgen.o
    CC rxonly.o
    CC txonly.o
    CC csumonly.o
    CC icmpecho.o
    CC tm.o
    LD testpmd
    INSTALL-APP testpmd
    INSTALL-MAP testpmd.map
  == Build app/proc_info
    CC main.o
    LD dpdk-procinfo
    INSTALL-APP dpdk-procinfo
    INSTALL-MAP dpdk-procinfo.map
  == Build app/pdump
    CC main.o
    LD dpdk-pdump
    INSTALL-APP dpdk-pdump
    INSTALL-MAP dpdk-pdump.map
  == Build app/test-crypto-perf
    CC main.o
    CC cperf_ops.o
    CC cperf_options_parsing.o
    CC cperf_test_vectors.o
    CC cperf_test_throughput.o
    CC cperf_test_latency.o
    CC cperf_test_pmd_cyclecount.o
    CC cperf_test_verify.o
    CC cperf_test_vector_parsing.o
    CC cperf_test_common.o
    LD dpdk-test-crypto-perf
    INSTALL-APP dpdk-test-crypto-perf
    INSTALL-MAP dpdk-test-crypto-perf.map
  == Build app/test-eventdev
    CC evt_main.o
    CC evt_options.o
    CC evt_test.o
    CC parser.o
    CC test_order_common.o
    CC test_order_queue.o
    CC test_order_atq.o
    CC test_perf_common.o
    CC test_perf_queue.o
    CC test_perf_atq.o
    LD dpdk-test-eventdev
    INSTALL-APP dpdk-test-eventdev
    INSTALL-MAP dpdk-test-eventdev.map
  Build complete [x86_64-native-linuxapp-gcc]
  Installation cannot run with T defined and DESTDIR undefined
  ------------------------------------------------------------------------------
  RTE_TARGET exported as x86_64-native-linuxapp-gcc
  ------------------------------------------------------------------------------

  Press enter to continue ...

Check the VF PCI information:

::

  root@pod-with-dpdk-vfs-0:/usr/src/dpdk# printenv | grep PCIDEVICE_INTEL_COM
  PCIDEVICE_INTEL_COM_PCI_SRIOV_NET_PHYSNET1=0000:05:11.1
  PCIDEVICE_INTEL_COM_PCI_SRIOV_NET_PHYSNET0=0000:05:11.0

Exit from pod back to host to find which VFs are assigned to this pod by check the pci address:

::

  [root@controller-0 sysadmin(keystone_admin)]# ls -l /sys/class/net/ens2f0/device/virtfn*
  lrwxrwxrwx 1 root root 0 Jun  4 02:12 /sys/class/net/ens2f0/device/virtfn0 -> ../0000:05:10.0
  lrwxrwxrwx 1 root root 0 Jun  4 02:12 /sys/class/net/ens2f0/device/virtfn1 -> ../0000:05:10.2
  lrwxrwxrwx 1 root root 0 Jun  4 02:12 /sys/class/net/ens2f0/device/virtfn2 -> ../0000:05:10.4
  lrwxrwxrwx 1 root root 0 Jun  4 02:12 /sys/class/net/ens2f0/device/virtfn3 -> ../0000:05:10.6
  lrwxrwxrwx 1 root root 0 Jun  4 02:12 /sys/class/net/ens2f0/device/virtfn4 -> ../0000:05:11.0
  lrwxrwxrwx 1 root root 0 Jun  4 02:12 /sys/class/net/ens2f0/device/virtfn5 -> ../0000:05:11.2

  [root@controller-0 sysadmin(keystone_admin)]# ls -l /sys/class/net/ens2f1/device/virtfn*
  lrwxrwxrwx 1 root root 0 Jun  4 02:12 /sys/class/net/ens2f1/device/virtfn0 -> ../0000:05:10.1
  lrwxrwxrwx 1 root root 0 Jun  4 02:12 /sys/class/net/ens2f1/device/virtfn1 -> ../0000:05:10.3
  lrwxrwxrwx 1 root root 0 Jun  4 02:12 /sys/class/net/ens2f1/device/virtfn2 -> ../0000:05:10.5
  lrwxrwxrwx 1 root root 0 Jun  4 02:12 /sys/class/net/ens2f1/device/virtfn3 -> ../0000:05:10.7
  lrwxrwxrwx 1 root root 0 Jun  4 02:12 /sys/class/net/ens2f1/device/virtfn4 -> ../0000:05:11.1
  lrwxrwxrwx 1 root root 0 Jun  4 02:12 /sys/class/net/ens2f1/device/virtfn5 -> ../0000:05:11.3

  [root@controller-0 sysadmin(keystone_admin)]# sudo ip link set ens2f0 vf 4 mac 9e:fd:e6:dd:c1:01
  [root@controller-0 sysadmin(keystone_admin)]# sudo ip link set ens2f1 vf 4 mac 9e:fd:e6:dd:c1:02


Get back to pod and run the test_pmd

::

  cd $RTE_SDK/x86_64-native-linuxapp-gcc/app/

  taskset -p --cpu-list 1
  pid 1's current affinity list: 2,3,18,19

  ./testpmd --socket-mem 1024,1024 -l 2,3 -w 0000:05:11.0 -w 0000:05:11.1 --file-prefix=testpmd_ -- --auto- 
  start --tx-first --stats-period 1 --disable-hw-vlan --eth-peer=0,"9e:fd:e6:dd:c1:02" --eth- 
  peer=1,"9e:fd:e6:dd:c1:01"
  EAL: Detected 32 lcore(s)
  EAL: No free hugepages reported in hugepages-2048kB
  EAL: Probing VFIO support...
  EAL: VFIO support initialized
  EAL: PCI device 0000:05:11.0 on NUMA socket 0
  EAL:   probe driver: 8086:10ed net_ixgbe_vf
  EAL:   using IOMMU type 1 (Type 1)
  EAL: PCI device 0000:05:11.1 on NUMA socket 0
  EAL:   probe driver: 8086:10ed net_ixgbe_vf
  Auto-start selected
  Ports to start sending a burst of packets first
  Warning: lsc_interrupt needs to be off when  using tx_first. Disabling.
  USER1: create a new mbuf pool <mbuf_pool_socket_0>: n=155456, size=2176, socket=0
  Configuring Port 0 (socket 0)
  Port 0: 9E:FD:E6:DD:C1:01
  Configuring Port 1 (socket 0)
  Port 1: 9E:FD:E6:DD:C1:02
  Checking link statuses...
  Port0 Link Up. speed 10000 Mbps- full-duplex
  Port1 Link Up. speed 10000 Mbps- full-duplex
  Done
  No commandline core given, start packet forwarding
  io packet forwarding - ports=2 - cores=1 - streams=2 - NUMA support enabled, MP over anonymous pages 
  disabled
  Logical Core 3 (socket 0) forwards packets on 2 streams:
    RX P=0/Q=0 (socket 0) -> TX P=1/Q=0 (socket 0) peer=9E:FD:E6:DD:C1:01
    RX P=1/Q=0 (socket 0) -> TX P=0/Q=0 (socket 0) peer=9E:FD:E6:DD:C1:02

    io packet forwarding packets/burst=32
    nb forwarding cores=1 - nb forwarding ports=2
    port 0:
    CRC stripping enabled
    RX queues=1 - RX desc=128 - RX free threshold=32
    RX threshold registers: pthresh=8 hthresh=8  wthresh=0
    TX queues=1 - TX desc=512 - TX free threshold=32
    TX threshold registers: pthresh=32 hthresh=0  wthresh=0
    TX RS bit threshold=32 - TXQ flags=0xf01
    port 1:
    CRC stripping enabled
    RX queues=1 - RX desc=128 - RX free threshold=32
    RX threshold registers: pthresh=8 hthresh=8  wthresh=0
    TX queues=1 - TX desc=512 - TX free threshold=32
    TX threshold registers: pthresh=32 hthresh=0  wthresh=0
    TX RS bit threshold=32 - TXQ flags=0xf01

  Port statistics ====================================
    ######################## NIC statistics for port 0  ########################
    RX-packets: 56         RX-missed: 0          RX-bytes:  4096
    RX-errors: 0
    RX-nombuf:  0
    TX-packets: 64         TX-errors: 0          TX-bytes:  4096

    Throughput (since last show)
    Rx-pps:            0
    Tx-pps:            0
    ############################################################################

    ######################## NIC statistics for port 1  ########################
    RX-packets: 432        RX-missed: 0          RX-bytes:  27712
    RX-errors: 0
    RX-nombuf:  0
    TX-packets: 461        TX-errors: 0          TX-bytes:  30080

    Throughput (since last show)
    Rx-pps:            0
    Tx-pps:            0
    ############################################################################

  Port statistics ====================================
    ######################## NIC statistics for port 0  ########################
    RX-packets: 14124641   RX-missed: 0          RX-bytes:  903977344
    RX-errors: 0
    RX-nombuf:  0
    TX-packets: 14170205   TX-errors: 0          TX-bytes:  906893376

    Throughput (since last show)
    Rx-pps:      7068409
    Tx-pps:      7091206
    ############################################################################




  
  
References
----------
  
- `StarlingX`_

.. _`StarlingX`: https://docs.starlingx.io/
          
