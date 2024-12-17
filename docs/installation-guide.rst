.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2019-2024 Wind River Systems, Inc.


INF Installation Guide
======================

.. contents::
   :depth: 3
   :local:

Overview
********

O-RAN INF is a downstream project of `StarlingX`_ and `OKD`_, and use the installation and deployment methods of those platforms.

Please see the detail of all the supported `Deployment Configurations`_.

Notes: For Yocto based image, only "All-in-one Simplex" and "All-in-one Duplex (up to 50 worker nodes)" are supported.

.. _`Deployment Configurations`: https://docs.starlingx.io/r/stx.7.0/deploy/index-deploy-da06a98b83b1.html

Preface
*******

Before starting the installation and deployment of O-RAN INF, you need to download the released ISO image or build from source as described in developer-guide.

The INF project supports Multi OS and the latest released images for each based OS can be dwonloaded in:
  - CentOS 7: `inf-image-centos-all-x86-64.iso`_
  - Debian 11 (bullseye): `inf-image-debian-all-x86-64.iso`_
  - Yocto 2.7 (warrior): `inf-image-yocto-aio-x86-64.iso`_

.. _`inf-image-debian-all-x86-64.iso`: https://nexus.o-ran-sc.org/content/sites/images/org/o-ran-sc/pti/rtp/latest/inf-image-debian-all-x86-64.iso
.. _`inf-image-centos-all-x86-64.iso`: https://nexus.o-ran-sc.org/content/sites/images/org/o-ran-sc/pti/rtp/latest/inf-image-centos-all-x86-64.iso
.. _`inf-image-yocto-aio-x86-64.iso`: https://nexus.o-ran-sc.org/content/sites/images/org/o-ran-sc/pti/rtp/latest/inf-image-yocto-aio-x86-64.iso

Deployment automation and documentation for OKD / CentOS Stream CoreOS can be found under the 'okd' directory in the `pti/rtp`_ repository.

.. _`pti/rtp`: https://gerrit.o-ran-sc.org/r/admin/repos/pti/rtp

Hardware Requirements
*********************

* For INF platform Hardware Requirements, refer to `System Hardware Requirements`_.
* For INF Openstack Hardware Requirements, refer to `Openstack Hardware Requirements`_.

* And you can also refer to the `Verified Commercial Hardware`_.

.. _`System Hardware Requirements`: https://docs.starlingx.io/planning/kubernetes/starlingx-hardware-requirements.html
.. _`Verified Commercial Hardware`: https://docs.starlingx.io/planning/kubernetes/verified-commercial-hardware.html
.. _`Openstack Hardware Requirements`: https://docs.starlingx.io/planning/openstack/hardware-requirements.html

Installation
************

Platform Installation
---------------------

INF uses the same installation and deployment methods of StarlingX or OKD, please refer to `StarlingX Installation`_ or 'okd/README.md' in the `pti/rtp`_ repository for the detail installation steps.

.. _`StarlingX Installation`: https://docs.starlingx.io/r/stx.7.0/deploy_install_guides/index-install-e083ca818006.html 

Applications Installation
-------------------------

Here are the example applications installations:

* `Install O-RAN O2`_
* `Install FlexRAN`_

.. _`Install O-RAN O2`: https://docs.starlingx.io/admintasks/kubernetes/oran-o2-application-b50a0c899e66.html
.. _`Install FlexRAN`: https://docs.starlingx.io/sample_apps/flexran/deploy-flexran-2203-on-starlingx-1d1b15ecb16f.html

References
**********
  
- `StarlingX`_
- `OKD`_

.. _`StarlingX`: https://docs.starlingx.io/
.. _`OKD`: https://www.okd.io/
