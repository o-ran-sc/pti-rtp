#
# Copyright (C) 2019 Wind River Systems, Inc.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

#
meta-stx
=========

Build
------------------------
```
git clone --branch r3_rebase git@github.com:zbsarashki/staging-stx.git
cd staging-stx
./setup.sh
```

Introduction
------------------------

This layer enables starlingx on poky. 


Dependencies
-------------------------

This layer depends on:

```
	URI: git://git.openembedded.org/meta-openembedded/
	revision: HEAD

	layes: meta-oe
	meta-python
	meta-networking
```
You are solely responsible for determining the appropriateness of using or redistributing the above dependencies and assume any risks associated with your exercise of permissions under the license.

Maintenance
-------------------------

Maintainer:



Building the meta-stx layer
---------------------------


Setup workspace
```
mkdir -p $P/workspace/{layers,build}
cd $P/workspace/layers

git clone --branch thud git://git.yoctoproject.org/poky.git
git clone --branch thud git://git.openembedded.org/meta-openembedded
git clone --branch thud git://git.yoctoproject.org/meta-virtualization
git clone --branch thud git://git.yoctoproject.org/meta-cloud-services
git clone --branch thud git://git.yoctoproject.org/meta-intel
git clone --branch thud git://git.yoctoproject.org/meta-intel-qat
git clone --branch thud git://git.yoctoproject.org/meta-selinux
git clone --branch thud git://git.yoctoproject.org/meta-security
git clone --branch thud https://github.com/jiazhang0/meta-secure-core.git
git clone --branch thud https://github.com/intel-iot-devkit/meta-iot-cloud.git 
git clone --branch thud https://github.com/rauc/meta-rauc.git
git clone --branch thud git://git.yoctoproject.org/meta-intel
git clone --branch thud git://git.yoctoproject.org/meta-intel-qat
git clone --branch thud https://github.com/intel-iot-devkit/meta-iot-cloud.git
git clone https://github.com/zbsarashki/meta-stx.git

```
Add the following layers to conf/bblayers.conf

```

P=Path to workspace 

cd $P/workspace/layers/poky
source oe-init-build-env $P/workspace/build

cat > conf/bblayers.conf << EOF
# POKY_BBLAYERS_CONF_VERSION is increased each time build/conf/bblayers.conf
# changes incompatibly
POKY_BBLAYERS_CONF_VERSION = "2"
 
BBPATH = "\${TOPDIR}"
BBFILES ?= ""

BBLAYERS ?= " \\
	$P/workspace/layers/poky/meta \\
	$P/workspace/layers/poky/meta-poky \\
	$P/workspace/layers/poky/meta-yocto-bsp \\
	$P/workspace/layers/meta-openembedded/meta-oe \\
	$P/workspace/layers/meta-openembedded/meta-networking \\
	$P/workspace/layers/meta-openembedded/meta-filesystems \\
	$P/workspace/layers/meta-openembedded/meta-perl \\
	$P/workspace/layers/meta-openembedded/meta-python \\
	$P/workspace/layers/meta-openembedded/meta-webserver \\
	$P/workspace/layers/meta-openembedded/meta-initramfs \\
	$P/workspace/layers/meta-openembedded/meta-gnome \\
	$P/workspace/layers/meta-virtualization \\
	$P/workspace/layers/meta-cloud-services \\
	$P/workspace/layers/meta-cloud-services/meta-openstack \\
	$P/workspace/layers/meta-cloud-services/meta-openstack-aio-deploy \\
	$P/workspace/layers/meta-cloud-services/meta-openstack-compute-deploy \\
	$P/workspace/layers/meta-cloud-services/meta-openstack-controller-deploy \\
	$P/workspace/layers/meta-cloud-services/meta-openstack-qemu \\
	$P/workspace/layers/meta-cloud-services/meta-openstack-swift-deploy \\
	$P/workspace/layers/meta-secure-core/meta-signing-key \\
	$P/workspace/layers/meta-secure-core/meta-efi-secure-boot \\
	$P/workspace/layers/meta-secure-core/meta-encrypted-storage \\
	$P/workspace/layers/meta-secure-core/meta-integrity \\
	$P/workspace/layers/meta-secure-core/meta-tpm2 \\
	$P/workspace/layers/meta-secure-core/meta \\
	$P/workspace/layers/meta-security \\
	$P/workspace/layers/meta-security/meta-security-compliance \\
	$P/workspace/layers/meta-selinux \\
	$P/workspace/layers/meta-intel \\
	$P/workspace/layers/meta-intel-qat \\
	$P/workspace/layers/meta-rauc \\
	$P/workspace/layers/meta-iot-cloud \\
	$P/workspace/layers/meta-stx \\
	"
EOF
	sed -i -e 's/^\(#MACHINE.*\"qemuarm\"\)/MACHINE \?= \"intel-corei7-64\"\n\1/g' conf/local.conf
	echo 'PREFERRED_PROVIDER_virtual/kernel = "linux-yocto"' >> conf/local.conf

```

Use Case:
---------------------------


# Legal Notices

All product names, logos, and brands are property of their respective owners. All company, product and service names used in this software are for identification purposes only. Wind River is a registered trademarks of Wind River Systems, Inc. Linux is a registered trademark of Linus Torvalds.

Disclaimer of Warranty / No Support: Wind River does not provide support and maintenance services for this software, under Wind River’s standard Software Support and Maintenance Agreement or otherwise. Unless required by applicable law, Wind River provides the software (and each contributor provides its contribution) on an “AS IS” BASIS, WITHOUT WARRANTIES OF ANY KIND, either express or implied, including, without limitation, any warranties of TITLE, NONINFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE. You are solely responsible for determining the appropriateness of using or redistributing the software and assume any risks associated with your exercise of permissions under the license.
