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

PACKAGES += " sysinv"

RDEPENDS_sysinv += " python"
RDEPENDS_sysinv += " bash"

inherit setuptools distutils python-dir systemd useradd

USERADD_PACKAGES = "sysinv"
USERADD_PARAM_sysinv = "-r -g sysinv -u 168 -d /var/lib/sysinv -s /sbin/nologin -c 'sysinv Daemons' sysinv"
GROUPADD_PARAM_sysinv = "-r -g 168 sysinv"

DEPENDS += " \
	python-pbr-native \
	"
RDEPENDS_sysinv += " \
	python-anyjson \
	python-amqp \
	python-amqplib \
	python-passlib \
	python-websockify \
	python-pyparted \
	python-boto3 \
	python-botocore \
	python-coverage \
	python-docker \
	python-eventlet \
	python-ipaddr \
	python-keyring \
	python-kubernetes \
	python-netaddr \
	python-pyudev \
	python-pbr \
	python-webtest \
	python-wsme \
	python-six \
	python-django \
	python-mox3 \
	python-oslo.i18n \
	python-oslo.config \
	python-oslo.concurrency \
	python-oslo.db \
	python-oslo.log \
	python-oslo.utils \
	python-pecan \
	python2-rpm \
	python-pyghmi \
	python-paramiko \
	tsconfig \
	resource-agents \
	gptfdisk \
	"

do_configure_append() {
	echo "Configure sysinv..."
	cd ${S}/sysinv/sysinv/sysinv
	distutils_do_configure
} 

do_compile_append() {
	
	echo "Building sysinv..."
	cd ${S}/sysinv/sysinv/sysinv
	distutils_do_compile
}

do_install_append() {

	echo "Installing sysinv..."
	cd ${S}/sysinv/sysinv/sysinv
	distutils_do_install

	install -d -m 755 ${D}${sysconfdir}/goenabled.d
	install -p -D -m 755 etc/sysinv/sysinv_goenabled_check.sh ${D}${sysconfdir}/goenabled.d/sysinv_goenabled_check.sh
	
	install -d -m 755 ${D}${sysconfdir}/sysinv
	install -p -D -m 755 etc/sysinv/policy.json ${D}${sysconfdir}/sysinv/policy.json
	install -p -D -m 640 etc/sysinv/profileSchema.xsd ${D}${sysconfdir}/sysinv/profileSchema.xsd
	
	install -p -D -m 644 etc/sysinv/crushmap-storage-model.txt ${D}${sysconfdir}/sysinv/crushmap-storage-model.txt
	install -p -D -m 644 etc/sysinv/crushmap-controller-model.txt ${D}${sysconfdir}/sysinv/crushmap-controller-model.txt
	install -p -D -m 644 etc/sysinv/crushmap-aio-sx.txt ${D}${sysconfdir}/sysinv/crushmap-aio-sx.txt
	
	install -d -m 755 ${D}${sysconfdir}/motd.d
	install -p -D -m 755 etc/sysinv/motd-system ${D}${sysconfdir}/motd.d/10-system
	
	install -d -m 755 ${D}${sysconfdir}/sysinv/upgrades
	install -p -D -m 755 etc/sysinv/delete_load.sh ${D}${sysconfdir}/sysinv/upgrades/delete_load.sh
	
	install -m 755 -p -D scripts/sysinv-api ${D}/usr/lib/ocf/resource.d/platform/sysinv-api
	install -m 755 -p -D scripts/sysinv-conductor ${D}/usr/lib/ocf/resource.d/platform/sysinv-conductor
	
	install -m 644 -p -D scripts/sysinv-api.service ${D}${systemd_system_unitdir}/sysinv-api.service
	install -m 644 -p -D scripts/sysinv-conductor.service ${D}${systemd_system_unitdir}/sysinv-conductor.service
	
	#install -p -D -m 755 ${D}/usr/bin/sysinv-api ${D}/usr/bin/sysinv-api
	#install -p -D -m 755 ${D}/usr/bin/sysinv-agent ${D}/usr/bin/sysinv-agent
	#install -p -D -m 755 ${D}/usr/bin/sysinv-conductor ${D}/usr/bin/sysinv-conductor
	
	install -d -m 755 ${D}${bindir}/
	install -p -D -m 755 sysinv/cmd/partition_info.sh ${D}${bindir}/partition_info.sh
	install -p -D -m 755 sysinv/cmd/manage-partitions ${D}${bindir}/manage-partitions
	install -p -D -m 755 sysinv/cmd/query_pci_id ${D}${bindir}/query_pci_id

	sed -i -e 's|/usr/local/bin|${bindir}|' \
		${D}${libdir}/python2.7/site-packages/sysinv/common/constants.py \
		${D}${libdir}/python2.7/site-packages/sysinv/puppet/common.py
}

FILES_sysinv = " \
	${bindir}/sysinv-agent \
	${bindir}/sysinv-utils \
	${bindir}/sysinv-conductor \
	${bindir}/sysinv-api \
	${bindir}/sysinv-helm \
	${bindir}/sysinv-dbsync \
	${bindir}/sysinv-dnsmasq-lease-update \
	${bindir}/sysinv-puppet \
	${bindir}/sysinv-rootwrap \
	${bindir}/sysinv-upgrade \
	${PYTHON_SITEPACKAGES_DIR}/sysinv/ \
	${bindir}/manage-partitions \
	${bindir}/query_pci_id \
	${bindir}/partition_info.sh \
	${libdir}/ocf \
	${libdir}/ocf/resource.d \
	${libdir}/ocf/resource.d/platform \
	${libdir}/ocf/resource.d/platform/sysinv-conductor \
	${libdir}/ocf/resource.d/platform/sysinv-api \
	${sysconfdir}/motd.d \
	${sysconfdir}/sysinv \
	${sysconfdir}/motd.d/10-system \
	${sysconfdir}/sysinv/profileSchema.xsd \
	${sysconfdir}/sysinv/crushmap-controller-model.txt \
	${sysconfdir}/sysinv/crushmap-storage-model.txt \
	${sysconfdir}/sysinv/crushmap-aio-sx.txt \
	${sysconfdir}/sysinv/policy.json \
	${sysconfdir}/sysinv/upgrades \
	${sysconfdir}/sysinv/upgrades/delete_load.sh \
	${sysconfdir}/goenabled.d/sysinv_goenabled_check.sh \
	${systemd_system_unitdir}/sysinv-api.service \
	${systemd_system_unitdir}/sysinv-conductor.service \
	"

#pkg_postinst_ontarget_sysinv() {
#}
