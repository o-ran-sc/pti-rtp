.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2019 Wind River Systems, Inc.

INF Developer Guide
===================

.. contents::
   :depth: 3
   :local:

1. About the INF project
************************

This project is a reference implementation of O-Cloud infrastructure which is based on StarlingX, and it supports multi-OS.

* Currently the following OS are supported:

  * CentOS 7
  * Yocto 2.7 (warrior)

1.1 About the CentOS based implementaion
----------------------------------------
The project provde wrapper scripts to automate all the steps of `StarlingX Build Guide`_ to build out the reference platform as an installable ISO image.

.. _`StarlingX Build Guide`: https://docs.starlingx.io/developer_resources/build_guide.html

1.2 About the Yocto based implementation
----------------------------------------

The project provde wrapper scripts to pull all required Yocto/OE layers to build out the reference platform as an installable ISO image.

To contribute on this project, basic knowledge of Yocto/OpenEmbedded is needed, please refer to the following docs if you want to learn about how to develop with Yocto/OpenEmbedded:

- `Yocto dev manual`_
- `OpenEmbedded wiki`_

.. _`Yocto dev manual`: https://www.yoctoproject.org/docs/2.6.3/dev-manual/dev-manual.html
.. _`OpenEmbedded wiki`: http://www.openembedded.org/wiki/Main_Page


2. How to build the INF project
*******************************

2.1 How to build the CentOS based image
---------------------------------------

2.1.1 Prerequisite for CentOS build environment
+++++++++++++++++++++++++++++++++++++++++++++++
TBD

2.1.2 Use wrapper script build_inf_centos.sh to setup build the CentOS based image
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

::

  # Get the wrapper script with either curl or wget
  $ curl -o build_inf_centos.sh 'https://gerrit.o-ran-sc.org/r/gitweb?p=pti/rtp.git;a=blob_plain;f=scripts/build_inf_centos/build_inf_centos.sh;hb=HEAD'
  $ wget -O build_inf_centos.sh 'https://gerrit.o-ran-sc.org/r/gitweb?p=pti/rtp.git;a=blob_plain;f=scripts/build_inf_centos/build_inf_centos.sh;hb=HEAD'

  $ chmod +x build_inf_centos.sh
  $ WORKSPACE=/path/to/workspace
  $ ./build_inf_centos.sh -w ${WORKSPACE}

If all go well, you will get the ISO image in:
${WORKSPACE}/prj_output/inf-image-centos-all-x86-64.iso


2.2 How to build the Yocto based image
--------------------------------------

2.2.1 Prerequisite for Yocto build environment
++++++++++++++++++++++++++++++++++++++++++++++

* Your host need to meet the requirements for Yocto, please refer to:

  * `Compatible Linux Distribution`_
  * `Supported Linux Distributions`_
  * `Required Packages for the Build Host`_

The recommended and tested host is Ubuntu 16.04/18.04 and CentOS 7.

* To install the required packages for Ubuntu 16.04/18.04:

.. _`Compatible Linux Distribution`: https://www.yoctoproject.org/docs/2.7.3/brief-yoctoprojectqs/brief-yoctoprojectqs.html#brief-compatible-distro
.. _`Supported Linux Distributions`: https://www.yoctoproject.org/docs/2.7.3/ref-manual/ref-manual.html#detailed-supported-distros
.. _`Required Packages for the Build Host`: https://www.yoctoproject.org/docs/2.7.3/ref-manual/ref-manual.html#required-packages-for-the-build-host

::

  $ sudo apt-get install gawk wget git-core diffstat unzip texinfo gcc-multilib \
    build-essential chrpath socat cpio python python3 python3-pip python3-pexpect \
    xz-utils debianutils iputils-ping make xsltproc docbook-utils fop dblatex xmlto \
    python-git

* To install the required packages for CentOS 7:

::

  $ sudo yum install -y epel-release
  $ sudo yum makecache
  $ sudo yum install gawk make wget tar bzip2 gzip python unzip perl patch \
    diffutils diffstat git cpp gcc gcc-c++ glibc-devel texinfo chrpath socat \
    perl-Data-Dumper perl-Text-ParseWords perl-Thread-Queue perl-Digest-SHA \
    python34-pip xz which SDL-devel xterm

2.2.2 Use wrapper script build_inf_yocto.sh to setup build the Yocto based image
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

::

  # Get the wrapper script with either curl or wget
  $ curl -o build_inf_yocto.sh 'https://gerrit.o-ran-sc.org/r/gitweb?p=pti/rtp.git;a=blob_plain;f=scripts/build_inf_yocto/build_inf_yocto.sh;hb=HEAD'
  $ wget -O build_inf_yocto.sh 'https://gerrit.o-ran-sc.org/r/gitweb?p=pti/rtp.git;a=blob_plain;f=scripts/build_inf_yocto/build_inf_yocto.sh;hb=HEAD'

  $ chmod +x build_inf_yocto.sh
  $ WORKSPACE=/path/to/workspace
  $ ./build_inf_yocto.sh -w ${WORKSPACE}

If all go well, you will get the ISO image in:
${WORKSPACE}/prj_output/inf-image-yocto-aio-x86-64.iso

