.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2019 Wind River Systems, Inc.

Developer-Guide
===============

.. contents::
   :depth: 3
   :local:


This project implements a real time platform to deploy the O-CU and O-DU and it's based on Yocto/OpenEmbedded.

This includes a Yocto/OpenEmbedded compatible layer meta-oran and wrapper scripts
to pull all required Yocto/OE layers to build out the reference platform.

To contribute on this project, basic knowledge of Yocto/OpenEmbedded is needed, please refer to the following docs if you want to learn about how to develop with Yocto/OpenEmbedded:

- `Yocto dev manual`_
- `OpenEmbedded wiki`_

.. _`Yocto dev manual`: https://www.yoctoproject.org/docs/2.6.3/dev-manual/dev-manual.html
.. _`OpenEmbedded wiki`: http://www.openembedded.org/wiki/Main_Page


Processes
---------

1. Prerequisite for build environment
`````````````````````````````````````

* Your host need to meet the requirements for Yocto, please refer to:

  * `Compatible Linux Distribution`_
  * `Supported Linux Distributions`_
  * `Required Packages for the Build Host`_

* The recommended and tested host is Ubuntu 16.04/18.04 and CentOS 7.

  * To install the required packages for Ubuntu 16.04/18.04:

.. _`Compatible Linux Distribution`: https://www.yoctoproject.org/docs/2.6.3/brief-yoctoprojectqs/brief-yoctoprojectqs.html#brief-compatible-distro
.. _`Supported Linux Distributions`: https://www.yoctoproject.org/docs/2.6.3/ref-manual/ref-manual.html#detailed-supported-distros)
.. _`Required Packages for the Build Host`: https://www.yoctoproject.org/docs/2.6.3/ref-manual/ref-manual.html#required-packages-for-the-build-host

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


2. Use wrapper script build_oran.sh to setup build env and build the image
``````````````````````````````````````````````````````````````````````````

::

  # Get the wrapper script with either curl or wget
  $ curl -o build_oran.sh 'https://gerrit.o-ran-sc.org/r/gitweb?p=pti/rtp.git;a=blob_plain;f=scripts/build_oran.sh;hb=HEAD'
  $ wget -O build_oran.sh 'https://gerrit.o-ran-sc.org/r/gitweb?p=pti/rtp.git;a=blob_plain;f=scripts/build_oran.sh;hb=HEAD'
  
  $ chmod +x build_oran.sh
  $ WORKSPACE=/path/to/workspace
  $ ./build_oran.sh -w ${WORKSPACE}

If all go well, you will get the ISO image in:
${WORKSPACE}/prj_wrl1018_oran/tmp-glibc/deploy/images/intel-x86-64/oran-image-inf-host-intel-x86-64.iso
