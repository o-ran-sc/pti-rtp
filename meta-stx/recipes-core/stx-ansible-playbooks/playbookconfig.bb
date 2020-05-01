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

FILESEXTRAPATHS_prepend := "${THISDIR}/patches:${THISDIR}/files:"
DESCRIPTION = " stx-ansible-playbooks"

STABLE = "starlingx/master"
PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCREV = "0ad01cd4cae7d5c85e1022b816ed465b334bb2e5"
S = "${WORKDIR}/git"
PV = "1.0.0"

LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

# The patch 0001-Treat-the-failure-as-expected-result-if-resize-using.patch
# need to be removed if updating to stx 2.0.0 or above.
SRC_URI = " \
	git://opendev.org/starlingx/ansible-playbooks.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	file://0001-stx.3.0-rebase-adjust-path.patch \
	file://0002-update_sysinv_database-do-not-fail-if-ceph-monitor-a.patch \
	file://0003-update_sysinv_database-wait-after-provision.patch \
	file://0004-bringup_flock_services-use-systmd-for-fminit-and-add.patch \
	file://0005-persist-config-add-retry-for-etcd.patch \
	file://0006-bringup_helm-wait-after-initialize-helm-to-avoid-tim.patch \
        "

RDEPENDS_playbookconfig = " \
	nscd \
	python \
	python-netaddr \
	python-ptyprocess \
	python-pexpect \
	python-ansible \
	sysinv \
	grub \
	grubby \
	dracut \
	openssl-bin \
	ipset \
	"

do_configure () {
	:
} 

do_compile() {
	:
}

do_install () {
	cd ${S}/playbookconfig/src
	oe_runmake -e \
		DESTDIR=${D}/${datadir}/ansible/stx-ansible
}

ANSIBLE_SSH_TIMEOUT = "60"
ANSIBLE_SSH_RETRY = "3"

pkg_postinst_${PN}() {
	cp $D${sysconfdir}/ansible/ansible.cfg $D${sysconfdir}/ansible/ansible.cfg.orig
	cp $D${sysconfdir}/ansible/hosts $D${sysconfdir}/ansible/hosts.orig
	cp $D${datadir}/ansible/stx-ansible/playbooks/ansible.cfg $D${sysconfdir}/ansible
	cp $D${datadir}/ansible/stx-ansible/playbooks/hosts $D${sysconfdir}/ansible

	sed -i -e 's/#timeout = .*/timeout = ${ANSIBLE_SSH_TIMEOUT}/' \
	       -e 's/#retries = .*/retries = ${ANSIBLE_SSH_RETRY}/' \
	       $D${sysconfdir}/ansible/ansible.cfg
}

FILES_${PN} = " \
	${datadir} \
	"
