meta-starlingx
=========

Introduction
------------------------

StarlingX is a complete cloud infrastructure software stack for the edge used by
the most demanding applications in industrial IOT, telecom, video delivery and
other ultra-low latency use cases. With deterministic low latency required by
edge applications, and tools that make distributed edge manageable, StarlingX
provides a container-based infrastructure for edge implementations in scalable
solutions that is ready for production now.

For more info on StarlingX See:

https://www.starlingx.io/

This layer enables StarlingX on poky.

Dependencies
-------------------------

This layer depends on:

```
	URI: git://git.openembedded.org/meta-openembedded
	branch: warrior
	revision: HEAD
	layers:
		openembedded-layer
		filesystem-layer
		meta-initramfs
		networking-layer
		perl-layer
		meta-python
		webserver
		

	URI: git://git.yoctoproject.org/meta-virtualization
	layer: virtualization-layer
	branch: warrior
	revision: HEAD

	URI: git://git.yoctoproject.org/meta-cloud-services
	branch: warrior
	revision: HEAD
	layers: cloud-services-layer
		openstack-layer
		

	URI: git://git.yoctoproject.org/meta-intel
	layer: intel
	branch: warrior
	revision: HEAD

	URI: git://git.yoctoproject.org/meta-security
	layer: security
	branch: warrior
	revision: HEAD

	URI: git://git.yoctoproject.org/meta-selinux
	layer: selinux
	branch: warrior
	revision: HEAD

	URI: https://github.com/intel-iot-devkit/meta-iot-cloud.git
	layer: iot-cloud
	branch: warrior
	revision: HEAD

	URI: git://git.openembedded.org/meta-python2
	layer: meta-python2
	branch: warrior
	revision: HEAD

	URI: https://git.yoctoproject.org/git/meta-dpdk
	layer: dpdk
	branch: warrior
	revision: HEAD

	URI: git://git.yoctoproject.org/meta-anaconda
	layer: meta-anaconda
	branch: warrior
	revision: HEAD

```
You are solely responsible for determining the appropriateness of using or redistributing the above dependencies and assume any risks associated with your exercise of permissions under the license.

Maintenance
-------------------------

Maintainers:

Submit bug reports via launchpad and story board with [MultiOS][Yocto] tags.

- Saul Wold <saul.wold@windriver.com>
- Jackie Huang <jackie.huang@windriver.com>
- Babak A. Sarashki <babak.sarashki@windriver.com>

Build:
---------------------------

A build script is found at git@github.com:zbsarashki/staging-stx.git.

Setup Build Environment
---------------------------

Tasks:
- Setup and Build Runtime image
- Setup and Build Installer image

Setup and Build Runtime image:
---------------------------

Setup build environment with the following added to the bblayers.conf:

```

 ${LAYER_PATH}/layers/poky/meta
 ${LAYER_PATH}/layers/poky/meta-poky
 ${LAYER_PATH}/layers/poky/meta-yocto-bsp
 ${LAYER_PATH}/layers/meta-openembedded/meta-oe
 ${LAYER_PATH}/layers/meta-openembedded/meta-filesystems
 ${LAYER_PATH}/layers/meta-openembedded/meta-initramfs
 ${LAYER_PATH}/layers/meta-openembedded/meta-networking
 ${LAYER_PATH}/layers/meta-openembedded/meta-perl
 ${LAYER_PATH}/layers/meta-openembedded/meta-python
 ${LAYER_PATH}/layers/meta-openembedded/meta-webserver
 ${LAYER_PATH}/layers/meta-openembedded/meta-gnome
 ${LAYER_PATH}/layers/meta-virtualization
 ${LAYER_PATH}/layers/meta-cloud-services
 ${LAYER_PATH}/layers/meta-cloud-services/meta-openstack
 ${LAYER_PATH}/layers/meta-intel
 ${LAYER_PATH}/layers/meta-security
 ${LAYER_PATH}/layers/meta-selinux
 ${LAYER_PATH}/layers/meta-iot-cloud
 ${LAYER_PATH}/layers/meta-python2
 ${LAYER_PATH}/layers/meta-dpdk
 ${LAYER_PATH}/layers/meta-stx/meta-stx-cloud
 ${LAYER_PATH}/layers/meta-stx/meta-stx-distro
 ${LAYER_PATH}/layers/meta-stx/meta-stx-flock
 ${LAYER_PATH}/layers/meta-stx/meta-stx-integ
 ${LAYER_PATH}/layers/meta-stx/meta-stx-virt
 ${LAYER_PATH}/layers/meta-anaconda

```

Edit conf/local.conf and set:

```
MACHINE = "intel-corei7-64"
PREFERRED_PROVIDER_virtual/kernel = "linux-yocto-rt"
IMAGE_FSTYPES = " tar.bz2"
IMAGE_FSTYPES_remove = " wic"
IMAGE_FSTYPES_remove = " ext4"
EXTRA_IMAGE_FEATURES ?= "debug-tweaks"
EXTRA_IMAGE_FEATURES += "tools-sdk"
EXTRA_IMAGE_FEATURES += "tools-debug"
EXTRA_IMAGE_FEATURES += "package-management"
DISTRO = "poky-stx"
DISTRO_FEATURES_append = " anaconda-support"
```

Build target with:

```
bitbake stx-image-aio
```

Setup and Build Installer image:
---------------------------
Setup build environment with the bblayers.conf as in RunTime image.

Edit conf/local.conf and set:

```
CONF_VERSION = "1"
DISTRO = 'anaconda'
MACHINE = "intel-corei7-64"
PREFERRED_PROVIDER_virtual/kernel = "linux-yocto"
INSTALLER_TARGET_BUILD = "/<PATH_TO_RUNTIME_STX_PRJ_DIR>/build/"
INSTALLER_TARGET_IMAGE = "stx-image-aio"

```
Build installer target with:

```
bitbake stx-image-installer-aio

```

Use Case:
---------------------------

This layer currently limited to AIO simplex mode has been tested to provision on host as outlined at:

- https://docs.starlingx.io/deploy_install_guides/r3_release/virtual/aio_simplex.html
- https://docs.starlingx.io/deploy_install_guides/r3_release/bare_metal/aio_simplex.html

License
-------
Copyright (C) 2019 Wind River Systems, Inc.

Source code included in tree for individual recipes is under the LICENSE
stated in each recipe (.bb file) unless otherwise stated.

The metadata is under the following license unless otherwise stated.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

# Legal Notices

All product names, logos, and brands are property of their respective owners. All company, product and service names used in this software are for identification purposes only. Wind River is a registered trademarks of Wind River Systems, Inc. Linux is a registered trademark of Linus Torvalds.

Disclaimer of Warranty / No Support: Wind River does not provide support and maintenance services for this software, under Wind River’s standard Software Support and Maintenance Agreement or otherwise. Unless required by applicable law, Wind River provides the software (and each contributor provides its contribution) on an “AS IS” BASIS, WITHOUT WARRANTIES OF ANY KIND, either express or implied, including, without limitation, any warranties of TITLE, NONINFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE. You are solely responsible for determining the appropriateness of using or redistributing the software and assume any risks associated with your exercise of permissions under the license.
