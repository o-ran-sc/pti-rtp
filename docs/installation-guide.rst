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

  root@oran-aio:~# kubeadm init --kubernetes-version v1.15.2 --pod-network-cidr=10.244.0.0/16
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

3.11 Deploy CMK (CPU-Manager-for-Kubernetes)
''''''''''''''''''''''''''''''''''''''''''''

Build the CMK docker image

::

  root@oran-aio:~# cd /opt/kubernetes_plugins/cpu-manager-for-kubernetes/
  root@oran-aio:/opt/kubernetes_plugins/cpu-manager-for-kubernetes# make

Verify that the cmk docker images is built successfully

::

  root@oran-aio:/opt/kubernetes_plugins/cpu-manager-for-kubernetes# docker images|grep cmk
  cmk          v1.3.1              3fec5f753b05        44 minutes ago      765MB

Edit the template yaml file for your deployment:
  - The template file is: /etc/kubernetes/plugins/cpu-manager-for-kubernetes/cmk-cluster-init-pod-template.yaml
  - The options you may need to change:

::

  # You can change the value for the following env:
  env:
  - name: HOST_LIST
    # Change this to modify the the host list to be initialized
    value: "oran-aio"
  - name: NUM_EXCLUSIVE_CORES
    # Change this to modify the value passed to `--num-exclusive-cores` flag
    value: "4"
  - name: NUM_SHARED_CORES
    # Change this to modify the value passed to `--num-shared-cores` flag
    value: "1"
  - name: CMK_IMG
    # Change his ONLY if you built the docker images with a different tag name
    value: "cmk:v1.3.1"

Or you can also refer to `CMK operator manual`_

.. _`CMK operator manual`: https://github.com/intel/CPU-Manager-for-Kubernetes/blob/master/docs/operator.md


Depoly CMK from yaml files

::

  root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/cpu-manager-for-kubernetes/cmk-rbac-rules.yaml
  root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/cpu-manager-for-kubernetes/cmk-serviceaccount.yaml
  root@oran-aio:~# kubectl apply -f /etc/kubernetes/plugins/cpu-manager-for-kubernetes/cmk-cluster-init-pod-template.yaml

Verify that the cmk cluster init completed and the pods for nodereport and webhook deployment are up and running

::

  root@oran-aio:/opt/kubernetes_plugins/cpu-manager-for-kubernetes# kubectl get pods --all-namespaces |grep cmk
  default       cmk-cluster-init-pod                         0/1     Completed   0          11m
  default       cmk-init-install-discover-pod-oran-aio       0/2     Completed   0          10m
  default       cmk-reconcile-nodereport-ds-oran-aio-qbdqb   2/2     Running     0          10m
  default       cmk-webhook-deployment-6f9dd7dfb6-2lj2p      1/1     Running     0          10m

- For detail usage, please refer to `CMK user manual`_

.. _`CMK user manual`: https://github.com/intel/CPU-Manager-for-Kubernetes/blob/master/docs/user.md

References
----------

- `Flannel`_
- `Doc for dashboard`_
- `Multus-CNI quick start`_
- `CMK operator manual`_
- `CMK user manual`_

.. _`Flannel`: https://github.com/coreos/flannel/blob/master/README.md
