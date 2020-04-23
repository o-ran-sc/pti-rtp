#
## Copyright (C) 2019 Wind River Systems, Inc.
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

SUMMARY = "The Kubernetes Package Manager"
HOMEPAGE = "https://github.com/kubernetes/helm/releases "
SECTION = "devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0c7bcb474e766c7d92924a18cd9d3878"

#SRCREV = "618447cbf203d147601b4b9bd7f8c37a5d39fbb4"
SRCNAME = "helm"
#PROTOCOL = "https"
#BRANCH = "release-2.13"
S = "${WORKDIR}/linux-amd64"
PV = "2.13.1"

SRC_URI = " \
	https://get.helm.sh/helm-v2.13.1-linux-amd64.tar.gz \
	file://helm-upload \
	file://helm.sudo \
	"
# Client: &version.Version{SemVer:"v2.13.1", GitCommit:"618447cbf203d147601b4b9bd7f8c37a5d39fbb4", GitTreeState:"clean"}
SRC_URI[md5sum] = "ffbe37fe328d99156d14a950bbd8107c"
SRC_URI[sha256sum] = "c1967c1dfcd6c921694b80ededdb9bd1beb27cb076864e58957b1568bc98925a"

INSANE_SKIP_${PN} = "ldflags"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT  = "1"

RDEPENDS_${PN} += " bash"


do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
	install -m 0755 -d ${D}/${sbindir}/
	install -m 0750 -d ${D}/${sysconfdir}/sudoers.d

	install -m 0755 ${S}/helm ${D}/${sbindir}/
	install -m 0755 ${S}/../helm.sudo ${D}/${sysconfdir}/sudoers.d/helm
	install -m 0755 ${S}/../helm-upload ${D}/${sbindir}/
}

BBCLASSEXTEND = "native nativesdk"

FILES_${PN} = " \
	${sbindir}/helm \
	${sbindir}/helm-upload \
	${sysconfdir}/sudoers.d \
	${sysconfdir}/sudoers.d/helm \
	"
