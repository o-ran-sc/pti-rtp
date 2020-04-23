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

DESCRIPTION = "StarlingX distributedcloud packages collection"
HOMEPAGE = "https://opendev.org/starlingx"
SECTION = "network"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://distributedcloud/LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

PROTOCOL = "https"
BRANCH = "r/stx.3.0"
SRCNAME = "distcloud"
SRCREV = "8329259704a5becd036663fc7de9b7a61f4bc27e"
PV = "1.0.0+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = " \
	git://opendev.org/starlingx/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH} \
	"

# TODO: 
# 1 - Patch service unit files to create the following directories:
#	- var/log/dcdbsync 
#	- var/log/dcmanager
#	- var/log/dcorch
# python-jsonschema >= 2.0.0
# python-keystoneauth1 >= 3.1.0
# python-pbr >= 1.8
# python-pecan >= 1.0.0
# python-routes >= 1.12.3

PACKAGES += "distributedcloud-dcmanager"
PACKAGES += "distributedcloud-dcorch"
PACKAGES += "distributedcloud-dcdbsync"
PACKAGES += "distributedcloud-ocf"
DESCRIPTION_distributedcloud-dcmanager = "Distributed Cloud Manager"
DESCRIPTION_distributedcloud-dcorch = "Distributed Cloud Orchestrator"
DESCRIPTION_distributedcloud-dcdbsync = "DC DCorch DBsync Agent"

DEPENDS += " \
	python-pbr-native \
	"

ALL_RDEPENDS = " \
	python-pycrypto \
	python-cryptography \
	python-eventlet \
	python-setuptools \
	python-jsonschema \
	python-keyring \
	python-keystonemiddleware \
	python-keystoneauth1 \
	python-netaddr \
	python-oslo.concurrency \
	python-oslo.config \
	python-oslo.context \
	python-oslo.db \
	python-oslo.i18n \
	python-oslo.log \
	python-oslo.messaging \
	python-oslo.middleware \
	python-oslo.policy \
	python-oslo.rootwrap \
	python-oslo.serialization \
	python-oslo.service \
	python-oslo.utils \
	python-oslo.versionedobjects \
	python-pbr \
	python-pecan \
	python-routes \
	python-sphinx \
	python-pyopenssl \
	systemd \
	python-babel \
	distributedcloud-ocf \
	"
# TODO: 
# Check dependencies
# 	python-sphinxcontrib-httpdomain

RDEPENDS_distributedcloud-dcmanager  += "  ${ALL_RDEPENDS}"
RDEPENDS_distributedcloud-dcorch += "  ${ALL_RDEPENDS}"
RDEPENDS_distributedcloud-dcdbsync+= " ${ALL_RDEPENDS}"
	

inherit distutils python-dir

do_configure() {
	cd ${S}/distributedcloud
	distutils_do_configure
}

do_compile() {
	cd ${S}/distributedcloud
	distutils_do_compile
}

do_install() {
	cd ${S}/distributedcloud
	distutils_do_install

	SRCPATH=${datadir}/starlingx/distrbutedcloud-config-files/

	# dcmanager
	install -d -m 0755 ${D}/var/log/dcmanager
	install -d -m 0755 ${D}/var/cache/dcmanager
	install -d -m 0755 ${D}/${sysconfdir}/dcmanager
	install -d -m 0755 ${D}/${sysconfdir}/tempfiles.d
	install -d -m 0755 ${D}/${datadir}/starlingx/distrbutedcloud-config-files/
	install -d -m 0755 ${D}/${systemd_system_unitdir}
	install -d -m 0755 ${D}/opt/dc/ansible

	# Install systemd unit files
	install -p -D -m 0644 centos/files/dcmanager-api.service ${D}/${systemd_system_unitdir}/
	install -p -D -m 0644 centos/files/dcmanager-manager.service ${D}/${systemd_system_unitdir}/
	install -p -D -m 0755 centos/files/dcmanager.conf ${D}/${sysconfdir}/tempfiles.d

	# Install default config files
	# defer postinst_ontarget
	install -p -m 0644 dcmanager//config-generator.conf ${D}/${SRCPATH}/dcmanager-config-generator.conf

	# dcorch
	install -d -m 0755 ${D}/var/log/dcorch
	install -d -m 0755 ${D}/var/cache/dcorch
	install -d -m 0755 ${D}/${sysconfdir}/dcorch

	# Install systemd unit files
	install -p -D -m0644 centos/files/dcorch-api.service ${D}/${systemd_system_unitdir}/
	install -p -D -m0644 centos/files/dcorch-engine.service ${D}/${systemd_system_unitdir}/
	install -p -D -m0644 centos/files/dcorch-sysinv-api-proxy.service ${D}/${systemd_system_unitdir}/
	install -p -D -m0644 centos/files/dcorch-snmp.service ${D}/${systemd_system_unitdir}/
	install -p -D -m0644 centos/files/dcorch-identity-api-proxy.service ${D}/${systemd_system_unitdir}/
	install -p -D -m0644 centos/files/dcorch.conf ${D}/${sysconfdir}/tempfiles.d

	# Install ocf scripts
	install -d -m 0755 ${D}/${libdir}/ocf/resource.d/openstack/ocf
	install -m 0644 \
		ocf/dcdbsync-api \
		ocf/dcmanager-api \
		ocf/dcmanager-manager \
		ocf/dcorch-cinder-api-proxy \
		ocf/dcorch-engine \
		ocf/dcorch-identity-api-proxy \
		ocf/dcorch-neutron-api-proxy \
		ocf/dcorch-nova-api-proxy \
		ocf/dcorch-patch-api-proxy \
		ocf/dcorch-snmp \
		ocf/dcorch-sysinv-api-proxy ${D}/${libdir}/ocf/resource.d/openstack/

	# Install default config files
	# defer postinst_ontarget
	install -p -m 0644 dcorch/config-generator.conf ${D}/${SRCPATH}/dcorch-config-generator.conf

	# dc dbsync agent
	install -d -m 755 ${D}/var/log/dcdbsync
	install -d -m 755 ${D}/var/cache/dcdbsync
	install -d -m 755 ${D}/${sysconfdir}/dcdbsync/

	# Install systemd unit files
	install -p -D -m 644 centos/files/dcdbsync-api.service ${D}/${systemd_system_unitdir}/dcdbsync-api.service
	# ???? CheckInstall systemd unit files for optional second instance
	install -p -D -m 644 centos/files/dcdbsync-openstack-api.service ${D}/${systemd_system_unitdir}/dcdbsync-openstack-api.service
	install -p -D -m 644 centos/files/dcdbsync.conf  ${D}/${sysconfdir}/tmpfiles.d

	# Install default config files
	# defer postinst_ontarget
	install -p -m 0644 dcdbsync/config-generator.conf ${D}/${SRCPATH}/dcdbsync-config-generator.conf
}


pkg_postinst_ontarget_distributedcloud-dcmanager() {
	SRCPATH=${datadir}/starlingx/distrbutedcloud-config-files/
	oslo-config-generator --config-file ${SRCPATH}/dcmanager-config-generator.conf \
		--output-file ${sysconfdir}/dcmanager/dcmanager.conf.sample
}

pkg_postinst_ontarget_distributedcloud-dcorch() {
	SRCPATH=${datadir}/starlingx/distrbutedcloud-config-files/
	oslo-config-generator --config-file ${SRCPATH}/dcorch-config-generator.conf \
		--output-file ${sysconfdir}/dcorch/dcorch.conf.sample
}


pkg_postinst_ontarget_distributedcloud-dcdbsync() {
	SRCPATH=${datadir}/starlingx/distrbutedcloud-config-files/
	oslo-config-generator --config-file ${SRCPATH}/dcdbsync-config-generator.conf \
		--output-file ${sysconfdir}/dcdbsync/dcdbsync.conf.sample
}

FILES_${PN} = " \
  /var/volatile \
  /var/log \
  /var/volatile/log \
  /etc/tmpfiles.d \
  "

FILES_distributedcloud-ocf = " \
	${libdir}/ocf/resource.d/openstack  \
	"

FILES_distributedcloud-dcdbsync = " \
	${PYTHON_SITEPACKAGES_DIR}/dcdbsync \
	${PYTHON_SITEPACKAGES_DIR}/dcdbsync-*.egg.info \
	/var/cache/dcdbsync \
	/var/volatile/log/dcdbsync \
	${bindir}/dcdbsync-api \
	${systemd_system_unitdir}/dcdbsync-api.service \
	${systemd_system_unitdir}/dcdbsync-openstack-api.service \
	${sysconfdir}/dcdbsync/ \
	${datadir}/starlingx/distrbutedcloud-config-files/dcdbsync-config-generator.conf \
	"

FILES_distributedcloud-dcorch = " \
	${PYTHON_SITEPACKAGES_DIR}/dcorch \
	${PYTHON_SITEPACKAGES_DIR}/distributedcloud-*.egg-info \
	${bindir}/dcorch-api \
	${systemd_system_unitdir}/dcorch-api.service \
	${bindir}/dcorch-engine \
	${systemd_system_unitdir}/dcorch-engine.service \
	${bindir}/dcorch-api-proxy \
	${systemd_system_unitdir}/dcorch-sysinv-api-proxy.service \
	${systemd_system_unitdir}/dcorch-identity-api-proxy.service \
	${bindir}/dcorch-manage \
	${bindir}/dcorch-snmp \
	${systemd_system_unitdir}/dcorch-snmp.service \
	${sysconfdir}/tempfiles.d/dcorch.conf \
	/var/cache/dcorch \
	${sysconfdir}/dcorch \
	${datadir}/starlingx/distrbutedcloud-config-files/dcorch-config-generator.conf \
	"

FILES_distributedcloud-dcmanager = " \
	${PYTHON_SITEPACKAGES_DIR}/dcmanager \
	${PYTHON_SITEPACKAGES_DIR}/distributedcloud-*.egg-info \
	${bindir}/dcmanager-api \
	${systemd_system_unitdir}/dcmanager-api.service \
	${bindir}/dcmanager-manager \
	${systemd_system_unitdir}/dcmanager-manager.service \
	${bindir}/dcmanager-manage \
	${sysconfdir}/tmpfiles.d/dcmanager.conf \
	/var/cache/dcmanager \
	${sysconfdir}/dcmanager \
	${sysconfdir}/tempfiles.d/dcmanager.conf \
	/opt/dc/ansible  \
	${datadir}/starlingx/distrbutedcloud-config-files/dcmanager-config-generator.conf \
	"
