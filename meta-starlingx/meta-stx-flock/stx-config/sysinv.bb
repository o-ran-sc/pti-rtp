require config-common.inc

SUBPATH0 = "sysinv/sysinv/sysinv"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRC_URI += "file://0001-stx-config-remove-argparse-requirement-from-sysinv.patch \
	file://0002-cgts-client-handle-exceptions-other-than-CalledProcessErr.patch;striplevel=4 \
	file://sriovph-bring-up.patch;striplevel=4 \
	"

RDEPENDS_${PN}_append = " python bash"
DEPENDS += " \
	python-pbr-native \
	"
RDEPENDS_${PN}_append  = " \
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

inherit setuptools python-dir systemd useradd
SYSTEMD_PACKAGES += "${PN}"
SYSTEMD_SERVICE_${PN} = "sysinv-api.service sysinv-conductor.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

USERADD_PACKAGES = "sysinv"
USERADD_PARAM_sysinv = "-r -g sysinv -u 168 -d /var/lib/sysinv -s /sbin/nologin -c 'sysinv Daemons' sysinv"
GROUPADD_PARAM_sysinv = "-r -g 168 sysinv"

do_install_append() {

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

