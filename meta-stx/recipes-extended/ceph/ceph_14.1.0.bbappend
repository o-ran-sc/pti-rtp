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

FILESEXTRAPATHS_prepend := "${THISDIR}/${BP}:${THISDIR}/files:"

inherit python3native python3-dir

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

SRC_URI += "\
	file://0001-ceph-rebase-on-stx.3.0-and-warrior.patch \
	file://ceph.conf \
	file://ceph-init-wrapper.sh \
        file://ceph-preshutdown.sh \
        file://ceph.service \
        file://mgr-restful-plugin.py \
        file://starlingx-docker-override.conf \
        file://ceph.conf.pmon \
        file://ceph-manage-journal.py \
        file://ceph-radosgw.service \
        file://ceph.sh \
        file://mgr-restful-plugin.service \
	file://rados.runtime.decode.error.patch \
	"
DEPENDS = "boost rdma-core bzip2 curl expat gperf-native \
		keyutils libaio lz4 \
		nspr nss oath openldap openssl \
		python3 python3-cython-native rocksdb snappy udev \
		python-cython-native valgrind xfsprogs zlib \
		rabbitmq-c \
		"
RDEPENDS_${PN} += " rdma-core python3-core python3 xfsprogs-mkfs python3-prettytable"


EXTRA_OECMAKE = "-DWITH_MANPAGE=OFF \
                 -DWITH_FUSE=OFF \
		 -DWITH_SPDK=OFF \
		 -DWITH_LEVELDB=OFF \
		 -DWITH_LTTNG=OFF \
		 -DWITH_BABELTRACE=OFF \
		 -DWITH_TESTS=OFF \
		 -DWITH_MGR=ON \
		 -DWITH_PYTHON2=OFF \
		 -DWITH_PYTHON3=ON \
		 -DMGR_PYTHON_VERSION=3 \
		 -DWITH_MGR_DASHBOARD_FRONTEND=OFF \
		 -DWITH_SYSTEM_BOOST=ON \
		 -DWITH_SYSTEM_ROCKSDB=ON \
		 -DWITH_RDMA=OFF \
		 -DWITH_RADOSGW_AMQP_ENDPOINT=OFF \
		 "

# TODO: Should be fixed in either boost package or CMake files. 
#do_configure_prepend() {
#	ln -f -s ${WORKDIR}/recipe-sysroot/usr/lib/libboost_python35.so \
#		${WORKDIR}/recipe-sysroot/usr/lib/libboost_python.so
#}

do_install_append () {
    install -d ${D}${sysconfdir}/ceph
    install -m 0644 ${WORKDIR}/ceph.conf ${D}${sysconfdir}/ceph/
    install -m 0644 ${WORKDIR}/ceph-radosgw.service ${D}${systemd_system_unitdir}/ceph-radosgw@.service
    install -m 0644 ${WORKDIR}/ceph.service ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/mgr-restful-plugin.service ${D}${systemd_system_unitdir}

    install -m 0700 ${WORKDIR}/ceph-manage-journal.py ${D}${sbindir}/ceph-manage-journal
    install -Dm 0750 ${WORKDIR}/mgr-restful-plugin.py  ${D}${sysconfdir}/rc.d/init.d/mgr-restful-plugin
    install -Dm 0750 ${WORKDIR}/mgr-restful-plugin.py  ${D}${sysconfdir}/init.d/mgr-restful-plugin
    install -m 0750 ${WORKDIR}/ceph.conf.pmon ${D}${sysconfdir}/ceph/

    install -d -m 0750 ${D}${sysconfdir}/services.d/controller
    install -d -m 0750 ${D}${sysconfdir}/services.d/storage
    install -d -m 0750 ${D}${sysconfdir}/services.d/worker

    install -m 0750 ${WORKDIR}/ceph.sh ${D}${sysconfdir}/services.d/controller
    install -m 0750 ${WORKDIR}/ceph.sh ${D}${sysconfdir}/services.d/storage
    install -m 0750 ${WORKDIR}/ceph.sh ${D}${sysconfdir}/services.d/worker

    install -Dm 0750 ${WORKDIR}/ceph-init-wrapper.sh ${D}${sysconfdir}/rc.d/init.d/ceph-init-wrapper
    install -Dm 0750 ${WORKDIR}/ceph-init-wrapper.sh ${D}${sysconfdir}/init.d/ceph-init-wrapper
    sed -i -e 's|/usr/lib64|${libdir}|' ${D}${sysconfdir}/rc.d/init.d/ceph-init-wrapper ${D}${sysconfdir}/init.d/ceph-init-wrapper

    install -m 0700 ${WORKDIR}/ceph-preshutdown.sh ${D}${sbindir}/ceph-preshutdown.sh
    
    install -Dm 0644 ${WORKDIR}/starlingx-docker-override.conf ${D}${systemd_system_unitdir}/docker.service.d/starlingx-docker-override.conf

    install -m 0644 -D ${S}/src/etc-rbdmap ${D}${sysconfdir}/ceph/rbdmap 
    install -m 0644 -D ${S}/etc/sysconfig/ceph ${D}${sysconfdir}/sysconfig/ceph
    install -m 0644 -D ${S}/src/logrotate.conf ${D}${sysconfdir}/logrotate.d/ceph

    install -m 0644 -D ${S}/COPYING ${D}${docdir}/ceph/COPYING    
    install -m 0644 -D ${S}/etc/sysctl/90-ceph-osd.conf ${D}${libdir}/sysctl.d/90-ceph-osd.conf
    install -m 0644 -D ${S}/udev/50-rbd.rules ${D}${libdir}/udev/rules.d/50-rbd.rules
    # install -m 0644 -D ${S}/udev/60-ceph-by-parttypeuuid.rules ${D}${libdir}/udev/rules.d/60-ceph-by-parttypeuuid.rules

    mkdir -p ${D}${localstatedir}/ceph
    mkdir -p ${D}${localstatedir}/log/ceph
    mkdir -p ${D}${localstatedir}/lib/ceph/tmp
    mkdir -p ${D}${localstatedir}/lib/ceph/mon
    mkdir -p ${D}${localstatedir}/lib/ceph/osd
    mkdir -p ${D}${localstatedir}/lib/ceph/mds
    mkdir -p ${D}${localstatedir}/lib/ceph/mgr
    mkdir -p ${D}${localstatedir}/lib/ceph/radosgw
    mkdir -p ${D}${localstatedir}/lib/ceph/bootstrap-osd
    mkdir -p ${D}${localstatedir}/lib/ceph/bootstrap-mds
    mkdir -p ${D}${localstatedir}/lib/ceph/bootstrap-rgw
    mkdir -p ${D}${localstatedir}/lib/ceph/bootstrap-mgr
    mkdir -p ${D}${localstatedir}/lib/ceph/bootstrap-rbd
    mkdir -p ${D}${localstatedir}/lib/ceph/crash/posted

    install -m 0755 -d ${D}/${sysconfdir}/tmpfiles.d
    echo "d ${localstatedir}/run/ceph 0755 ceph ceph -" >> ${D}/${sysconfdir}/tmpfiles.d/ceph.conf

    install -m 0755 ${D}${libdir}/ceph/ceph_common.sh ${D}${libexecdir}/ceph

    install -m 0750 -D ${S}/src/init-radosgw ${D}${sysconfdir}/rc.d/init.d/ceph-radosgw
    install -m 0750 -D ${S}/src/init-radosgw ${D}${sysconfdir}/init.d/ceph-radosgw
    sed -i '/### END INIT INFO/a SYSTEMCTL_SKIP_REDIRECT=1' ${D}${sysconfdir}/rc.d/init.d/ceph-radosgw
    sed -i '/### END INIT INFO/a SYSTEMCTL_SKIP_REDIRECT=1' ${D}${sysconfdir}/init.d/ceph-radosgw
    install -m 0750 -D ${S}/src/init-rbdmap ${D}${sysconfdir}/rc.d/init.d/rbdmap
    install -m 0750 -D ${S}/src/init-rbdmap ${D}${sysconfdir}/init.d/rbdmap
    install -m 0750 -D ${B}/bin/init-ceph ${D}${sysconfdir}/rc.d/init.d/ceph
    install -m 0750 -D ${B}/bin/init-ceph ${D}${sysconfdir}/init.d/ceph
    install -d -m 0750 ${D}${localstatedir}/log/radosgw 

    sed -i -e 's:${WORKDIR}.*python3:${bindir}/python3:' ${D}${bindir}/ceph
    # sed -i -e 's:${WORKDIR}.*python3:${bindir}/python3:' ${D}${bindir}/ceph-disk
    # sed -i -e 's:${WORKDIR}.*python3:${bindir}/python3:' ${D}${bindir}/ceph-detect-init

    sed -i -e 's:${WORKDIR}.*python3:${bindir}/python3:' ${D}${bindir}/ceph-crash
    sed -i -e 's:${WORKDIR}.*python3:${bindir}/python3:' ${D}${bindir}/ceph-volume
    sed -i -e 's:${WORKDIR}.*python3:${bindir}/python3:' ${D}${bindir}/ceph-volume-systemd
    #sed -i -e '1s:python$:python3:' ${D}${bindir}/ceph-volume
    #sed -i -e '1s:python$:python3:' ${D}${bindir}/ceph-volume-systemd
    sed -i -e 's:/sbin/:/bin/:' ${D}${systemd_system_unitdir}/ceph-volume@.service
}

TARGET_CC_ARCH += "${LDFLAGS}"
RDEPENDS_${PN} += "\
        bash \
"

FILES_${PN} += "\
        ${localstatedir} \
	${docdir}/ceph/COPYING \
	${libdir}/sysctl.d/90-ceph-osd.conf \
	${libdir}/udev/rules.d/50-rbd.rules \
	${libdir}/udev/rules.d/60-ceph-by-parttypeuuid.rules \
        ${systemd_system_unitdir}/mgr-restful-plugin.service \
        ${systemd_system_unitdir}/ceph-radosgw@.service \
        ${systemd_system_unitdir}/ceph.service \
        ${systemd_system_unitdir}/docker.service.d/starlingx-docker-override.conf \
	home/root/cluster/ceph-mon_config.sh \
	home/root/cluster/ceph-mgr_manual.sh \
	home/root/cluster/ceph-volume_manual.sh \
"
# /run/ceph
