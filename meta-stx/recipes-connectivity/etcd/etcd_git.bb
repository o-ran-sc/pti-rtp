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

SUMMARY = "A distributed key-value store for shared config and service discovery"
DESCRIPTION = " \
    etcd is a distributed reliable key-value store for the most critical data \
    of a distributed system, with a focus on being: \
    \
    * Simple: well-defined, user-facing API (gRPC) \
    * Secure: automatic TLS with optional client cert authentication \
    * Fast: benchmarked 10,000 writes/sec \
    * Reliable: properly distributed using Raft \
"
HOMEPAGE = "https://github.com/coreos/etcd"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/import/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = " \
    git://github.com/coreos/etcd.git;branch=release-3.3 \
    "

SRCREV = "94745a4eed0425653b3b4275a208d38babceeaec"
PV = "3.3.15+git${SRCPV}"

S = "${WORKDIR}/git"

inherit go goarch systemd useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = " \
    --system --shell ${sbindir}/nologin --comment 'etcd user' \
    --home-dir ${localstatedir}/lib --groups etcd --gid etcd etcd \
    "
GROUPADD_PARAM_${PN} = "etcd"

TARGET_CC_ARCH += "${LDFLAGS}"
GO_IMPORT = "import"

do_compile() {
	export GOARCH="${TARGET_GOARCH}"

	# Setup vendor directory so that it can be used in GOPATH.
	#
	# Go looks in a src directory under any directory in GOPATH but
	# uses 'vendor' instead of 'vendor/src'. We can fix this with a symlink.
	#
	# We also need to link in the ipallocator directory as that is not under
	# a src directory.
	export GOPATH="${B}/src/import/"
	mkdir -p ${B}/src/import/src/github.com/coreos/
	ln -s ${S}/src/import ${B}/src/import/src/github.com/coreos/etcd

	# Pass the needed cflags/ldflags so that cgo
	# can find the needed headers files and libraries
	export CGO_ENABLED="1"
	export CFLAGS=""
	export LDFLAGS=""
	export CGO_CFLAGS="${BUILDSDK_CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
	export CGO_LDFLAGS="${BUILDSDK_LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"

	if [ "${TARGET_ARCH}" = "x86_64" ]; then
		export GOARCH="amd64"
	elif [ "${TARGET_ARCH}" = "i586" ]; then
		export GOARCH="386"
	fi

	./src/import/build
}

do_install() {
	install -d ${D}/${bindir}
	install -m 0755 ${B}/bin/etcd ${D}/${bindir}/etcd
	install -m 0755 ${B}/bin/etcdctl ${D}/${bindir}/etcdctl

	install -d ${D}${systemd_system_unitdir}
	install -m 0644 ${S}/src/import/contrib/systemd/etcd.service ${D}${systemd_system_unitdir}

	# etcd state is in /var/lib/etcd
	install -d ${D}${sysconfdir}/tmpfiles.d
	echo "d ${localstatedir}/lib/${BPN} 0755 etcd etcd -" \
		> ${D}${sysconfdir}/tmpfiles.d/${BPN}.conf

	# we aren't creating a user, so we need to comment out this line
	sed -i '/User/s/^/#/' ${D}${systemd_unitdir}/system/etcd.service
}

deltask compile_ptest_base

RDEPENDS_${PN} = "bash"

# During packaging etcd gets the warning "no GNU hash in elf binary"
# This issue occurs due to compiling without ldflags, but a
# solution has yet to be found. For now we ignore this error with
# the line below.
#INSANE_SKIP_${PN} = "ldflags"
