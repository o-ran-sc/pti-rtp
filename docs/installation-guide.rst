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

This document describes how to install O-RAN INF image, example configuration for better
real time performance, and example deployment of Kubernetes cluster and plugins. 

The audience of this document is assumed to have basic knowledge in Yocto/Open-Embedded Linux
and container technology.

Version history

+--------------------+--------------------+--------------------+--------------------+
| **Date**           | **Ver.**           | **Author**         | **Comment**        |
|                    |                    |                    |                    |
+--------------------+--------------------+--------------------+--------------------+
| 2019-11-02         | 1.0.0              | Jackie Huang       | Initail version    |
|                    |                    |                    |                    |
+--------------------+--------------------+--------------------+--------------------+
|                    |                    |                    |                    |
|                    |                    |                    |                    |
+--------------------+--------------------+--------------------+--------------------+
|                    |                    |                    |                    |
|                    |                    |                    |                    |
+--------------------+--------------------+--------------------+--------------------+


Preface
-------

Before starting the installation and deployment of O-RAN INF, you need to download the ISO image or build from source as described in developer-guide.


Hardware Requirements
---------------------

Following minimum hardware requirements must be met for installation of O-RAN INF image:

+--------------------+----------------------------------------------------+
| **HW Aspect**      | **Requirement**                                    |
|                    |                                                    |
+--------------------+----------------------------------------------------+
| **# of servers**   | 1                                                  |
+--------------------+----------------------------------------------------+
| **CPU**            | 2                                                  |
|                    |                                                    |
+--------------------+----------------------------------------------------+
| **RAM**            | 4G                                                 |
|                    |                                                    |
+--------------------+----------------------------------------------------+
| **Disk**           | 20G                                                |
|                    |                                                    |
+--------------------+----------------------------------------------------+
| **NICs**           | 1                                                  |
|                    |                                                    |
+--------------------+----------------------------------------------------+



Software Installation and Deployment
------------------------------------

1. Installation from the O-RAN INF ISO image
````````````````````````````````````````````

- Please see the README.md file for how to build the image.
- The Image is a live ISO image with CLI installer: oran-image-inf-host-intel-x86-64.iso

1.1 Burn the image to the USB device
''''''''''''''''''''''''''''''''''''

- Assume the the usb device is /dev/sdX here

::

  $ sudo dd if=/path/to/oran-image-inf-host-intel-x86-64.iso of=/dev/sdX bs=1M

1.2 Insert the USB device in the target to be booted.
'''''''''''''''''''''''''''''''''''''''''''''''''''''

1.3 Reboot the target from the USB device.
''''''''''''''''''''''''''''''''''''''''''

1.4 Select "Graphics console install" or "Serial console install" and press ENTER
'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''

1.5 Select the hard disk and press ENTER
''''''''''''''''''''''''''''''''''''''''

Notes: In this installer, you can only select which hard disk to install, the whole disk will be used and partitioned automatically.

- e.g. insert "sda" and press ENTER

1.6 Remove the USB device and press ENTER to reboot
'''''''''''''''''''''''''''''''''''''''''''''''''''

2. Configuration for better real time performance
`````````````````````````````````````````````````

Notes: Some of the tuning options are machine specific or depend on use cases,
like the hugepages, isolcpus, rcu_nocbs, kthread_cpus, irqaffinity, nohz_full and
so on, please do not just copy and past.

- Edit the grub.cfg with the following example tuning options

::

  # Notes: the grub.cfg file path is different for legacy and UEFI mode
  #   For legacy mode: /boot/grub/grub.cfg
  #   For UEFI mode: /boot/EFI/BOOT/grub.cfg

  grub_cfg="/boot/grub/grub.cfg"
  #grub_cfg="/boot/EFI/BOOT/grub.cfg"

  # In this example, 1-16 cores are isolated for real time processes
  root@intel-x86-64:~# rt_tuning="crashkernel=auto biosdevname=0 iommu=pt usbcore.autosuspend=-1 nmi_watchdog=0 softlockup_panic=0 intel_iommu=on cgroup_enable=memory skew_tick=1 hugepagesz=1G hugepages=4 default_hugepagesz=1G isolcpus=1-16 rcu_nocbs=1-16 kthread_cpus=0 irqaffinity=0 nohz=on nohz_full=1-16 intel_idle.max_cstate=0 processor.max_cstate=1 intel_pstate=disable nosoftlockup idle=poll mce=ignore_ce"

  # optional to add the console setting
  root@intel-x86-64:~# console="console=ttyS0,115200"

  root@intel-x86-64:~# sed -i "/linux / s/$/ $console $rt_tuning/" $grub_cfg


- Reboot the target

::

  root@intel-x86-64:~# reboot

3. Kubernetes cluster and plugins deployment instructions (All-in-one)
``````````````````````````````````````````````````````````````````````
This instruction will show you how to deploy kubernetes cluster and plugins in an all-in-one example scenario after the above installation.

3.1 Change the hostname (Optional)
''''''''''''''''''''''''''''''''''

::

  # Assuming the hostname is oran-aio, ip address is <aio_host_ip>
  # please DO NOT copy and paste, use your actaul hostname and ip address
  root@intel-x86-64:~# echo oran-aio > /etc/hostname
  root@intel-x86-64:~# export AIO_HOST_IP="<aio_host_ip>"
  root@intel-x86-64:~# echo "$AIO_HOST_IP oran-aio" >> /etc/hosts

3.2 Disable swap for Kubernetes
'''''''''''''''''''''''''''''''

::

  root@intel-x86-64:~# sed -i '/ swap / s/^\(.*\)$/#\1/g' /etc/fstab
  root@intel-x86-64:~# systemctl mask dev-sda4.swap

3.3 Set the proxy for docker (Optional)
'''''''''''''''''''''''''''''''''''''''

- If you are under a firewall, you may need to set the proxy for docker to pull images

::

  root@intel-x86-64:~# HTTP_PROXY="http://<your_proxy_server_ip>:<port>"
  root@intel-x86-64:~# mkdir /etc/systemd/system/docker.service.d/
  root@intel-x86-64:~# cat << EOF > /etc/systemd/system/docker.service.d/http-proxy.conf
  [Service]
  Environment="HTTP_PROXY=$HTTP_PROXY" "NO_PROXY=localhost,127.0.0.1,localaddress,.localdomain.com,$AIO_HOST_IP,10.244.0.0/16"
  EOF

3.4 Reboot the target
'''''''''''''''''''''

::

  root@intel-x86-64:~# reboot

3.5 Initialize kubernetes cluster master
''''''''''''''''''''''''''''''''''''''''

::

  root@oran-aio:~# kubeadm init --kubernetes-version v1.16.2 --pod-network-cidr=10.244.0.0/16
  root@oran-aio:~# mkdir -p $HOME/.kube
  root@oran-aio:~# cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  root@oran-aio:~# chown $(id -u):$(id -g) $HOME/.kube/config

3.6 Make the master also works as a worker node
'''''''''''''''''''''''''''''''''''''''''''''''

::

  root@oran-aio:~# kubectl taint nodes oran-aio node-role.kubernetes.io/master-

3.7 Deploy flannel
''''''''''''''''''

::

  root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/flannel/kube-flannel.yml

Check that the aio node is ready after flannel is successfully deployed and running

::

  root@oran-aio:~# kubectl get pods --all-namespaces |grep flannel
  kube-system   kube-flannel-ds-amd64-bwt52        1/1     Running   0          3m24s

  root@oran-aio:~# kubectl get nodes
  NAME       STATUS   ROLES    AGE     VERSION
  oran-aio   Ready    master   3m17s   v1.15.2-dirty

3.8 Deploy kubernetes dashboard
'''''''''''''''''''''''''''''''

Deploy kubernetes dashboard

::

  root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/kubernetes-dashboard/kubernetes-dashboard-admin.rbac.yaml
  root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/kubernetes-dashboard/kubernetes-dashboard.yaml

Verify that the dashboard is up and running

::

  # Check the pod for dashboard
  root@oran-aio:~# kubectl get pods --all-namespaces |grep dashboard
  kube-system   kubernetes-dashboard-5b67bf4d5f-ghg4f   1/1     Running   0          64s

Access the dashboard UI in a web browser with the https url, port number is 30443.

- For detail usage, please refer to `Doc for dashboard`_

.. _`Doc for dashboard`: https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/

3.9 Deploy Multus-CNI
'''''''''''''''''''''

::

  root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/multus-cni/multus-daemonset.yml

Verify that the multus-cni is up and running

::

  root@oran-aio:~# kubectl get pods --all-namespaces | grep -i multus
  kube-system   kube-multus-ds-amd64-hjpk4              1/1     Running   0          7m34s

- For further validating, please refer to the `Multus-CNI quick start`_

.. _`Multus-CNI quick start`: https://github.com/intel/multus-cni/blob/master/doc/quickstart.md

3.10 Deploy NFD (node-feature-discovery)
''''''''''''''''''''''''''''''''''''''''

::

  root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/node-feature-discovery/nfd-master.yaml
  root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/node-feature-discovery/nfd-worker-daemonset.yaml

Verify that nfd-master and nfd-worker are up and running

::

  root@oran-aio:~# kubectl get pods --all-namespaces |grep nfd
  default       nfd-master-7v75k                        1/1     Running   0          91s
  default       nfd-worker-xn797                        1/1     Running   0          24s

Verify that the node is labeled by nfd:

::

  root@oran-aio:~# kubectl describe nodes|grep feature.node.kubernetes
                     feature.node.kubernetes.io/cpu-cpuid.AESNI=true
                     feature.node.kubernetes.io/cpu-cpuid.AVX=true
                     feature.node.kubernetes.io/cpu-cpuid.AVX2=true
                     (...snip...)

3.11 Deploy SRIOV CNI
'''''''''''''''''''''

Provision VF drivers and devices


Enumerate  PF Devices

::

  root@oran-aio:~/dpdk-18.08/usertools# lspci -D |grep 82599
  0000:04:00.0 Ethernet controller: Intel Corporation 82599ES 10-Gigabit SFI/SFP+ Network Connection (rev 01)
  0000:04:00.1 Ethernet controller: Intel Corporation 82599ES 10-Gigabit SFI/SFP+ Network Connection (rev 01)

Correlate the PF device to eth interfaces and bring them up

::

  root@oran-aio:~# ethtool -i eth4  |grep bus-info
  bus-info: 0000:04:00.0
  root@oran-aio:~# ethtool -i eth5  |grep bus-info
  bus-info: 0000:04:00.1
  root@oran-aio:~# ifconfig eth4 up
  root@oran-aio:~# ifconfig eth5 up

Load VF Driver modules

::

  root@oran-aio:~# modprobe ixgbevf
  root@oran-aio:~# modprobe uio
  root@oran-aio:~# modprobe igb-uio
  root@oran-aio:~# modprobe vfio
  root@oran-aio:~# modprobe vfio-pci
  root@oran-aio:~# lsmod |grep ixgbevf
  ixgbevf                61440  0
  root@oran-aio:~# lsmod |grep vfio
  vfio_pci               40960  0
  vfio_virqfd            16384  1 vfio_pci
  vfio_iommu_type1       24576  0
  vfio                   24576  2 vfio_iommu_type1,vfio_pci
  irqbypass              16384  2 vfio_pci,kvm


Bind VF drivers to VF devices

::

  root@oran-aio:~# cat /sys/bus/pci/devices/0000\:04\:00.0/sriov_totalvfs
  root@oran-aio:~# cat /sys/bus/pci/devices/0000\:04\:00.1/sriov_totalvfs
  root@oran-aio:~# cat /sys/bus/pci/devices/0000\:04\:00.0/sriov_numvfs
  root@oran-aio:~# cat /sys/bus/pci/devices/0000\:04\:00.1/sriov_numvfs
  root@oran-aio:~# echo 8 > /sys/bus/pci/devices/0000\:04\:00.0/sriov_numvfs
  root@oran-aio:~# echo 8 > /sys/bus/pci/devices/0000\:04\:00.1/sriov_numvfs
  
  root@oran-aio:~# lspci -D |grep 82599
  0000:04:00.0 Ethernet controller: Intel Corporation 82599ES 10-Gigabit SFI/SFP+ Network Connection (rev 01)
  0000:04:00.1 Ethernet controller: Intel Corporation 82599ES 10-Gigabit SFI/SFP+ Network Connection (rev 01)
  0000:04:10.0 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:10.1 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:10.2 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:10.3 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:10.4 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:10.5 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:10.6 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:10.7 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:11.0 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:11.1 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:11.2 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:11.3 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:11.4 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:11.5 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:11.6 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  0000:04:11.7 Ethernet controller: Intel Corporation 82599 Ethernet Controller Virtual Function (rev 01)
  
  root@oran-aio:~# dpdk-devbind -b vfio-pci 0000:04:11.0 0000:04:11.1 0000:04:11.2 0000:04:11.3 0000:04:11.4 0000:04:11.5 0000:04:11.6 0000:04:11.7
  
  root@oran-aio:~# dpdk-devbind --status-dev net
  
  Network devices using DPDK-compatible driver
  ============================================
  0000:04:11.0 '82599 Ethernet Controller Virtual Function 10ed' drv=vfio-pci unused=ixgbevf,igb_uio
  0000:04:11.1 '82599 Ethernet Controller Virtual Function 10ed' drv=vfio-pci unused=ixgbevf,igb_uio
  0000:04:11.2 '82599 Ethernet Controller Virtual Function 10ed' drv=vfio-pci unused=ixgbevf,igb_uio
  0000:04:11.3 '82599 Ethernet Controller Virtual Function 10ed' drv=vfio-pci unused=ixgbevf,igb_uio
  0000:04:11.4 '82599 Ethernet Controller Virtual Function 10ed' drv=vfio-pci unused=ixgbevf,igb_uio
  0000:04:11.5 '82599 Ethernet Controller Virtual Function 10ed' drv=vfio-pci unused=ixgbevf,igb_uio
  0000:04:11.6 '82599 Ethernet Controller Virtual Function 10ed' drv=vfio-pci unused=ixgbevf,igb_uio
  0000:04:11.7 '82599 Ethernet Controller Virtual Function 10ed' drv=vfio-pci unused=ixgbevf,igb_uio
  
  Network devices using kernel driver
  ===================================
  0000:04:00.0 '82599ES 10-Gigabit SFI/SFP+ Network Connection 10fb' if=eth4 drv=ixgbe unused=igb_uio,vfio-pci
  0000:04:00.1 '82599ES 10-Gigabit SFI/SFP+ Network Connection 10fb' if=eth5 drv=ixgbe unused=igb_uio,vfio-pci
  0000:04:10.0 '82599 Ethernet Controller Virtual Function 10ed' if=eth6 drv=ixgbevf unused=igb_uio,vfio-pci
  0000:04:10.1 '82599 Ethernet Controller Virtual Function 10ed' if=eth14 drv=ixgbevf unused=igb_uio,vfio-pci
  0000:04:10.2 '82599 Ethernet Controller Virtual Function 10ed' if=eth7 drv=ixgbevf unused=igb_uio,vfio-pci
  0000:04:10.3 '82599 Ethernet Controller Virtual Function 10ed' if=eth15 drv=ixgbevf unused=igb_uio,vfio-pci
  0000:04:10.4 '82599 Ethernet Controller Virtual Function 10ed' if=eth8 drv=ixgbevf unused=igb_uio,vfio-pci
  0000:04:10.5 '82599 Ethernet Controller Virtual Function 10ed' if=eth16 drv=ixgbevf unused=igb_uio,vfio-pci
  0000:04:10.6 '82599 Ethernet Controller Virtual Function 10ed' if= drv=ixgbevf unused=igb_uio,vfio-pci
  0000:04:10.7 '82599 Ethernet Controller Virtual Function 10ed' if=eth17 drv=ixgbevf unused=igb_uio,vfio-pci


Build SRIOV CNI

::

  root@oran-aio:~# HTTP_PROXY="http://<your_proxy_server_ip>:<port>"
  
  root@oran-aio:~# wget https://dl.google.com/go/go1.14.1.linux-amd64.tar.gz
  root@oran-aio:~# tar -zxvf go1.14.1.linux-amd64.tar.gz
  root@oran-aio:~# PATH=$PATH:/root/go/bin/
  root@oran-aio:~# git clone https://github.com/intel/sriov-cni
  root@oran-aio:~# cd sriov-cni
  root@oran-aio:~/sriov-cni# make
  root@oran-aio:~/sriov-cni# cp build/sriov /opt/cni/bin
  
  root@oran-aio:~# cd ~/
  root@oran-aio:~# git clone https://github.com/intel/sriov-network-device-plugin
  root@oran-aio:~# cd sriov-network-device-plugin
  root@oran-aio:~/sriov-network-device-plugin# git fetch origin pull/196/head:fpgadp
  root@oran-aio:~/sriov-network-device-plugin# git checkout fpgadp
  root@oran-aio:~/sriov-network-device-plugin# make image
  root@oran-aio:~/sriov-network-device-plugin# docker images |grep sriov-device-plugin
  nfvpe/sriov-device-plugin                             latest              f4e6bbefad67        5 minutes ago       25.5MB


Deploy SRIOV CNI

::

  root@oran-aio:~/sriov-network-device-plugin# cat <<EOF> deployments/sriovdp_configMap.yaml
  apiVersion: v1
  kind: ConfigMap
  metadata:
    name: sriovdp-config
    namespace: kube-system
  data:
    config.json: |
      {
          "resourceList": [{
                  "resourceName": "intel_sriov_netdevice",
                  "selectors": {
                      "vendors": ["8086"],
                      "devices": ["154c", "10ed"],
                      "drivers": ["i40evf", "ixgbevf"]
                  }
              },
              {
                  "resourceName": "intel_sriov_dpdk",
                  "selectors": {
                      "vendors": ["8086"],
                      "devices": ["154c", "10ed"],
                      "drivers": ["vfio-pci"]
                  }
              },
              {
                  "resourceName": "mlnx_sriov_rdma",
                  "isRdma": true,
                  "selectors": {
                      "vendors": ["15b3"],
                      "devices": ["1018"],
                      "drivers": ["mlx5_ib"]
                  }
              }
          ]
      }
  EOF
  
  root@oran-aio:~/sriov-network-device-plugin# kubectl create -f deployments/sriovdp_configMap.yaml
  root@oran-aio:~/sriov-network-device-plugin# kubectl create -f deployments/k8s-v1.16/sriovdp-daemonset.yaml

  root@oran-aio:~/sriov-network-device-plugin# kubectl get pods --all-namespaces |grep kube-sriov-device-plugin
  kube-system   kube-sriov-device-plugin-amd64-6lm8n   1/1     Running   0          12m
  
  root@oran-aio:~/sriov-network-device-plugin# kubectl -n kube-system logs kube-sriov-device-plugin-amd64-6lm8n
  I0327 02:14:46.488409   14488 manager.go:115] Creating new ResourcePool: intel_sriov_netdevice
  I0327 02:14:46.488427   14488 factory.go:144] device added: [pciAddr: 0000:04:10.0, vendor: 8086, device: 10ed, driver: ixgbevf]
  I0327 02:14:46.488439   14488 factory.go:144] device added: [pciAddr: 0000:04:10.1, vendor: 8086, device: 10ed, driver: ixgbevf]
  I0327 02:14:46.488446   14488 factory.go:144] device added: [pciAddr: 0000:04:10.2, vendor: 8086, device: 10ed, driver: ixgbevf]
  I0327 02:14:46.488459   14488 factory.go:144] device added: [pciAddr: 0000:04:10.3, vendor: 8086, device: 10ed, driver: ixgbevf]
  I0327 02:14:46.488467   14488 factory.go:144] device added: [pciAddr: 0000:04:10.4, vendor: 8086, device: 10ed, driver: ixgbevf]
  I0327 02:14:46.488473   14488 factory.go:144] device added: [pciAddr: 0000:04:10.5, vendor: 8086, device: 10ed, driver: ixgbevf]
  I0327 02:14:46.488479   14488 factory.go:144] device added: [pciAddr: 0000:04:10.6, vendor: 8086, device: 10ed, driver: ixgbevf]
  I0327 02:14:46.488485   14488 factory.go:144] device added: [pciAddr: 0000:04:10.7, vendor: 8086, device: 10ed, driver: ixgbevf]
  I0327 02:14:46.488502   14488 manager.go:128] New resource server is created for intel_sriov_netdevice ResourcePool
  I0327 02:14:46.488511   14488 manager.go:114]
  I0327 02:14:46.488516   14488 manager.go:115] Creating new ResourcePool: intel_sriov_dpdk
  I0327 02:14:46.488529   14488 factory.go:144] device added: [pciAddr: 0000:04:11.0, vendor: 8086, device: 10ed, driver: vfio-pci]
  I0327 02:14:46.488538   14488 factory.go:144] device added: [pciAddr: 0000:04:11.1, vendor: 8086, device: 10ed, driver: vfio-pci]
  I0327 02:14:46.488545   14488 factory.go:144] device added: [pciAddr: 0000:04:11.2, vendor: 8086, device: 10ed, driver: vfio-pci]
  I0327 02:14:46.488551   14488 factory.go:144] device added: [pciAddr: 0000:04:11.3, vendor: 8086, device: 10ed, driver: vfio-pci]
  I0327 02:14:46.488562   14488 factory.go:144] device added: [pciAddr: 0000:04:11.4, vendor: 8086, device: 10ed, driver: vfio-pci]
  I0327 02:14:46.488569   14488 factory.go:144] device added: [pciAddr: 0000:04:11.5, vendor: 8086, device: 10ed, driver: vfio-pci]
  I0327 02:14:46.488575   14488 factory.go:144] device added: [pciAddr: 0000:04:11.6, vendor: 8086, device: 10ed, driver: vfio-pci]
  I0327 02:14:46.488581   14488 factory.go:144] device added: [pciAddr: 0000:04:11.7, vendor: 8086, device: 10ed, driver: vfio-pci]
  I0327 02:14:46.488591   14488 manager.go:128] New resource server is created for intel_sriov_dpdk ResourcePool


Test intel_sriov_netdeivce

::

  root@oran-aio:~/sriov-network-device-plugin# cat <<EOF> deployments/sriov-crd.yaml
  apiVersion: "k8s.cni.cncf.io/v1"
  kind: NetworkAttachmentDefinition
  metadata:
    name: sriov-net1
    annotations:
      k8s.v1.cni.cncf.io/resourceName: intel.com/intel_sriov_netdevice
  spec:
    config: '{
    "type": "sriov",
    "cniVersion": "0.3.1",
    "name": "sriov-network",
    "vlan": 100,
    "ipam": {
      "type": "host-local",
      "subnet": "10.56.217.0/24",
      "routes": [{
        "dst": "0.0.0.0/0"
      }],
      "gateway": "10.56.217.1"
    }
  }'
  EOF
  
  root@oran-aio:~/sriov-network-device-plugin# kubectl create -f deployments/sriov-crd.yaml
  root@oran-aio:~/sriov-network-device-plugin# kubectl create -f deployments/pod-tc1.yaml
  root@oran-aio:~/sriov-network-device-plugin# kubectl get pods  |grep testpod1
  root@oran-aio:~/sriov-network-device-plugin# ip link |grep 'vlan 100'
    vf 3 MAC a6:01:0a:34:39:e1, vlan 100, spoof checking on, link-state auto, trust off, query_rss off
   
  root@oran-aio:~/sriov-network-device-plugin# kubectl exec -it testpod1 -- ip addr show |grep a6:01:0a:34:39:e1 -C 2
    valid_lft forever preferred_lft forever
  21: net1: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc mq state UP group default qlen 1000
    link/ether a6:01:0a:34:39:e1 brd ff:ff:ff:ff:ff:ff
    inet 10.56.217.3/24 brd 10.56.217.255 scope global net1
       valid_lft forever preferred_lft forever


Test intel_sriov_dpdk

::

  root@oran-aio:~/sriov-network-device-plugin# cat <<EOF> deployments/sriovdpdk-crd.yaml
  apiVersion: "k8s.cni.cncf.io/v1"
  kind: NetworkAttachmentDefinition
  metadata:
    name: sriov1-vfio
    annotations:
      k8s.v1.cni.cncf.io/resourceName: intel.com/intel_sriov_dpdk
  spec:
    config: '{
    "type": "sriov",
    "cniVersion": "0.3.1",
    "vlan": 101,
    "name": "sriov1-vfio"
  }'
  EOF
  
  root@oran-aio:~/sriov-network-device-plugin# cat <<EOF> deployments/dpdk-1g.yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: dpdk-1g
    annotations:
      k8s.v1.cni.cncf.io/networks: '[
         {"name": "sriov1-vfio"},
         {"name": "sriov1-vfio"}
      ]'
  spec:
    restartPolicy: Never
    containers:
    - name: dpdk-1g
      image: centos/tools
      imagePullPolicy: IfNotPresent
      volumeMounts:
      - mountPath: /mnt/huge-2048
        name: hugepage
      - name: lib-modules
        mountPath: /lib/modules
      - name: src
        mountPath: /usr/src
      command: ["/bin/bash", "-ec", "sleep infinity"]
      securityContext:
        privileged: true
        capabilities:
          add:
          - ALL
      resources:
        requests:
          memory: 4Gi
          hugepages-1Gi: 4Gi
          intel.com/intel_sriov_dpdk: '2'
        limits:
          memory: 4Gi
          hugepages-1Gi: 4Gi
          intel.com/intel_sriov_dpdk: '2'
    imagePullSecrets:
    - name: admin-registry-secret
    volumes:
    - name: hugepage
      emptyDir:
        medium: HugePages
    - name: lib-modules
      hostPath:
        path: /lib/modules
    - name: src
      hostPath:
        path: /usr/src
    imagePullSecrets:
    - name: admin-registry-secret
  EOF
  
  root@oran-aio:~/sriov-network-device-plugin# kubectl create -f deployments/sriovdpdk-crd.yaml
  root@oran-aio:~/sriov-network-device-plugin# kubectl create -f deployments/dpdk-1g.yaml
  
  root@oran-aio:~/sriov-network-device-plugin# root@oran-aio:~/sriov-network-device-plugin# kubectl get pods | grep dpdk
  dpdk-1g    1/1     Running   0          13s
  
  root@oran-aio:~/sriov-network-device-plugin# ip link |grep 101
    vf 7 MAC 00:00:00:00:00:00, vlan 101, spoof checking on, link-state auto, trust off, query_rss off
    vf 6 MAC 00:00:00:00:00:00, vlan 101, spoof checking on, link-state auto, trust off, query_rss off


Now test with dpdk

::

  ### build following package and copy to target server: bitbake bison;bitbake kernel-devsrc
  root@oran-aio:~/sriov-network-device-plugin# rpm -ivh ~/bison-3.0.4-r0.corei7_64.rpm
  root@oran-aio:~/sriov-network-device-plugin# rpm -ivh ~/kernel-devsrc-1.0-r0.intel_x86_64.rpm

  root@oran-aio:~/sriov-network-device-plugin# kubectl exec -it $(kubectl get pods -o wide | grep dpdk | awk '{ print $1 }') -- /bin/bash
  [root@dpdk-1g /]# export |grep INTEL
    declare -x PCIDEVICE_INTEL_COM_INTEL_SRIOV_DPDK="0000:04:11.6,0000:04:11.5"
  
  [root@dpdk-1g /]# yum -y install wget ncurses-devel unzip libpcap-devel ncurses-devel libedit-devel pciutils lua-devel
  
  [root@dpdk-1g /]# cd /opt
  [root@dpdk-1g /]# wget https://fast.dpdk.org/rel/dpdk-18.08.tar.xz
  [root@dpdk-1g /]# tar xf dpdk-18.08.tar.xz
  [root@dpdk-1g /]# cd dpdk-18.08/
  [root@dpdk-1g /]# sed -i 's/CONFIG_RTE_EAL_IGB_UIO=y/CONFIG_RTE_EAL_IGB_UIO=n/g' config/common_linuxapp
  [root@dpdk-1g /]# sed -i 's/CONFIG_RTE_LIBRTE_KNI=y/CONFIG_RTE_LIBRTE_KNI=n/g' config/common_linuxapp
  [root@dpdk-1g /]# sed -i 's/CONFIG_RTE_KNI_KMOD=y/CONFIG_RTE_KNI_KMOD=n/g' config/common_linuxapp
  [root@dpdk-1g /]# export RTE_SDK=/opt/dpdk-18.08
  [root@dpdk-1g /]# export RTE_TARGET=x86_64-native-linuxapp-gcc
  [root@dpdk-1g /]# export RTE_BIND=$RTE_SDK/usertools/dpdk-devbind.py
  [root@dpdk-1g /]# make install T=$RTE_TARGET
  [root@dpdk-1g /]# cd examples/helloworld
  [root@dpdk-1g /]# make
  [root@dpdk-1g /]# NR_hugepages=2
  [root@dpdk-1g /]# ./build/helloworld -l 1-4 -n 4 -m $NR_hugepages
  ...
      hello from core 2
      hello from core 3
      hello from core 4
      hello from core 1



References
----------

- `Flannel`_
- `Doc for dashboard`_
- `Multus-CNI quick start`_

.. _`Flannel`: https://github.com/coreos/flannel/blob/master/README.md
