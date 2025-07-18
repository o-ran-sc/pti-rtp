.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2019-2024 Wind River Systems, Inc.

INF Developer Guide
===================

.. contents::
   :depth: 3
   :local:

1. About the INF project
************************

This project is a reference implementation of O-Cloud infrastructure which is based on StarlingX and OKD, and it supports multi-OS.

* Currently the following OS are supported:

  * StarlingX

    * Debian 11 (bullseye)

  * OKD

    * CentOS Stream CoreOS 9

Notes:
  * Debian based is the recommended platfrom.
  * The intended audiences of this guide are the developers who want to develop/integrate apps in INF platform, if you just want to install and deploy INF platform, you can ignore this guide and read the `INF Installation Guide`_

Experimental feature:
* INF project starts to support ARM64 architecture in this release as experimental feature (POC level), limited features implemented and tested.

  * It can only be natively built on HPE RL300 Gen11 server (Ampere Altra).
  * AIO-SX (std kernel) tested on VM and HPE RL300 server.
  * AIO-DX (std kernel) tested on VM.

.. _`INF Installation Guide`: https://docs.o-ran-sc.org/projects/o-ran-sc-pti-rtp/en/latest/installation-guide.html

1.1 About the Debian based implementaion
----------------------------------------
The project provde wrapper scripts to automate all the steps of `StarlingX Debian Build Guide`_ to build out the reference platform as an installable ISO image.

.. _`StarlingX Debian Build Guide`: https://wiki.openstack.org/wiki/StarlingX/DebianBuildEnvironment

1.2 About the CentOS Stream CoreOS / OKD based implementation
-------------------------------------------------------------
Deployment automation and documentation for OKD / CentOS Stream CoreOS can be found under the 'okd' directory in the `pti/rtp`_ repository.

.. _`pti/rtp`: https://gerrit.o-ran-sc.org/r/admin/repos/pti/rtp

2. How to build the INF project
*******************************

2.1 How to build the Debian based image
---------------------------------------

2.1.1 Prerequisite for Debian build environment
+++++++++++++++++++++++++++++++++++++++++++++++

NOTE: The build system for Debian requires a Linux system with Docker and python 3.x installed. The the following steps have been tested on CentOS 7 and Ubuntu 20.04.

* Refer to `Install docker on ubuntu`_ or `Install docker on centos`_ to install docker.
* Refer to `Configure_Debian_build_environment`_ to install prerequisite packges and configure for Debian build environment.

.. _`Install docker on ubuntu`: https://docs.docker.com/engine/install/ubuntu/
.. _`Install docker on centos`: https://docs.docker.com/engine/install/centos/
.. _`Configure_Debian_build_environment`: https://wiki.openstack.org/wiki/StarlingX/DebianBuildEnvironment#Configure_build_environment

2.1.2 Use wrapper script build_inf_debian.sh to build the Debian based image
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

::

  # Get the wrapper script to build the debian image
  $ wget -O build_inf_debian.sh 'https://gerrit.o-ran-sc.org/r/gitweb?p=pti/rtp.git;a=blob_plain;f=scripts/build_inf_debian/build_inf_debian.sh;hb=HEAD'

  $ chmod +x build_inf_debian.sh
  $ WORKSPACE=/path/to/workspace
  $ ./build_inf_debian.sh -w ${WORKSPACE}

If all go well, you will get the ISO image in:
${WORKSPACE}/prj_output/inf-image-debian-all-x86-64.iso

2.2 How to build the Debian based image for ARM64 arch
------------------------------------------------------

2.2.1 Prerequisite for Debian build environment
+++++++++++++++++++++++++++++++++++++++++++++++

NOTE:
  * The build env only tested on HPE RL300 server (Ampere Altra).
  * The build system for Debian requires a Linux system with Docker and python 3.x installed. The the following steps have been tested on Debian 11.

* Refer to `Install docker on ubuntu`_ or `Install docker on centos`_ to install docker.
* Refer to `Configure_Debian_build_environment`_ to install prerequisite packges and configure for Debian build environment.

.. _`Install docker on debian`: https://docs.docker.com/engine/install/debian/
.. _`Configure_Debian_build_environment`: https://wiki.openstack.org/wiki/StarlingX/DebianBuildEnvironment#Configure_build_environment

2.2.2 Use wrapper script build_stx_debian.sh to build the Debian based image
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

::

  # Get the wrapper script to build the debian image
  $ wget -O build_stx_debian.sh 'https://gerrit.o-ran-sc.org/r/gitweb?p=pti/rtp.git;a=blob_plain;f=scripts/build_inf_debian/build_stx_debian.sh;hb=HEAD'

  $ chmod +x build_stx_debian.sh
  $ WORKSPACE=/path/to/workspace
  $ ./build_stx_debian.sh -w ${WORKSPACE} -a arm64


The build-image will always fail for now, do the following workaround after build-image fails:

::

  cd ${WORKSPACE}
  source env.prj-stx-deb
  cd src/stx-tools
  source import-stx
  
  stx shell --container lat
  
  # inside the LAT pod
  cd /localdisk
  . /opt/LAT/SDK/environment-setup-cortexa57-wrs-linux
  appsdk --log-dir log genimage lat.yaml

If all go well, you will get the ISO image in:
${WORKSPACE}/localdisk/deploy/starlingx-qemuarm64-cd.iso
