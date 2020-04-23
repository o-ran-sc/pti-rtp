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

SUMMARY = "User space components of the Ceph file system"
DESCRIPTION = "\
Ceph is a massively scalable, open-source, distributed storage system that runs \
on commodity hardware and delivers object, block and file system storage. \
"
HOMEPAGE = "https://ceph.io"

LICENSE = "LGPLv2.1 & GPLv2 & Apache-2.0 & MIT"
LIC_FILES_CHKSUM = "\
    file://COPYING-LGPL2.1;md5=fbc093901857fcd118f065f900982c24 \
    file://COPYING-GPL2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://COPYING;md5=92d301c8fccd296f2221a68a8dd53828 \
"

DEPENDS = "\
    boost rdma-core bzip2 curl expat \
    gperf-native keyutils libaio lz4 \
    nspr nss oath openldap openssl \
    python python-cython-native rocksdb \
    snappy udev valgrind xfsprogs zlib \
"

SRC_URI = "\
    http://download.ceph.com/tarballs/ceph-${PV}.tar.gz \
    file://0001-Correct-the-path-to-find-version.h-in-rocksdb.patch \
    file://0002-zstd-fix-error-for-cross-compile.patch \
    file://0003-ceph-add-pybind-support-in-OE.patch \
    file://0004-ceph-detect-init-correct-the-installation-for-OE.patch \
    file://0005-Add-hooks-for-orderly-shutdown-on-controller.patch \
    \
    file://ceph-init-wrapper.sh \
    file://ceph-manage-journal.py \
    file://ceph-preshutdown.sh \
    file://ceph-radosgw.service \
    file://ceph.conf \
    file://ceph.conf.pmon \
    file://ceph.service \
    file://ceph.sh \
    file://mgr-restful-plugin.py \
    file://mgr-restful-plugin.service \
    file://starlingx-docker-override.conf \
"
SRC_URI[md5sum] = "ce118be451dcb6b89e9e0a45057827dd"
SRC_URI[sha256sum] = "f3a61db4c90e00c38a2dac7239b956ec367ef56f601e07335ed3011f931d8840"

inherit cmake pythonnative python-dir systemd

DISTRO_FEATURES_BACKFILL_CONSIDERED_remove = "sysvinit"

SYSTEMD_SERVICE_${PN} = " \
    ceph-radosgw@.service \
    ceph-radosgw.target \
    ceph-mon@.service \
    ceph-mon.target \
    ceph-mds@.service \
    ceph-mds.target \
    ceph-disk@.service \
    ceph-osd@.service \
    ceph-osd.target \
    ceph.target \
    ceph-fuse@.service \
    ceph-fuse.target \
    ceph-rbd-mirror@.service \
    ceph-rbd-mirror.target \
    ceph-volume@.service \
    ceph-mgr@.service \
    ceph-mgr.target \
    rbdmap.service \
"

OECMAKE_GENERATOR = "Unix Makefiles"

EXTRA_OECMAKE = "\
    -DWITH_MANPAGE=OFF \
    -DWITH_FUSE=OFF \
    -DWITH_SPDK=OFF \
    -DWITH_LEVELDB=OFF \
    -DWITH_LTTNG=OFF \
    -DWITH_BABELTRACE=OFF \
    -DWITH_TESTS=OFF \
    -DDEBUG_GATHER=OFF \
    -DWITH_PYTHON2=ON \
    -DWITH_MGR=ON \
    -DMGR_PYTHON_VERSION=2.7 \
    -DWITH_MGR_DASHBOARD_FRONTEND=OFF \
    -DWITH_SYSTEM_BOOST=ON \
    -DWITH_SYSTEM_ROCKSDB=ON \
"

do_configure_prepend () {
    echo "set( CMAKE_SYSROOT \"${RECIPE_SYSROOT}\" )" >> ${WORKDIR}/toolchain.cmake
    echo "set( CMAKE_DESTDIR \"${D}\" )" >> ${WORKDIR}/toolchain.cmake
    echo "set( PYTHON_SITEPACKAGES_DIR \"${PYTHON_SITEPACKAGES_DIR}\" )" >> ${WORKDIR}/toolchain.cmake
    ln -sf ${STAGING_LIBDIR}/libboost_python27.so ${STAGING_LIBDIR}/libboost_python.so
}

do_install_append () {
    mv ${D}${bindir}/ceph-disk ${D}${sbindir}/ceph-disk
    sed -i -e 's:${WORKDIR}.*python2.7:${bindir}/python:' ${D}${sbindir}/ceph-disk
    sed -i -e 's:${WORKDIR}.*python2.7:${bindir}/python:' ${D}${bindir}/ceph
    sed -i -e 's:${WORKDIR}.*python2.7:${bindir}/python:' ${D}${bindir}/ceph-detect-init
    find ${D} -name SOURCES.txt | xargs sed -i -e 's:${WORKDIR}::'

    install -d ${D}${systemd_unitdir}
    mv ${D}${libexecdir}/systemd/system ${D}${systemd_unitdir}
    mv ${D}${libexecdir}/ceph/ceph-osd-prestart.sh ${D}${libdir}/ceph
    install -m 0755 ${D}${libexecdir}/ceph/ceph_common.sh ${D}${libdir}/ceph

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
    install -m 0644 -D ${S}/udev/60-ceph-by-parttypeuuid.rules ${D}${libdir}/udev/rules.d/60-ceph-by-parttypeuuid.rules

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

    install -m 0755 -d ${D}/${sysconfdir}/tmpfiles.d
    echo "d ${localstatedir}/run/ceph 0755 ceph ceph -" >> ${D}/${sysconfdir}/tmpfiles.d/ceph.conf

    install -m 0750 -D ${S}/src/init-radosgw ${D}${sysconfdir}/rc.d/init.d/ceph-radosgw
    install -m 0750 -D ${S}/src/init-radosgw ${D}${sysconfdir}/init.d/ceph-radosgw
    sed -i '/### END INIT INFO/a SYSTEMCTL_SKIP_REDIRECT=1' ${D}${sysconfdir}/rc.d/init.d/ceph-radosgw
    sed -i '/### END INIT INFO/a SYSTEMCTL_SKIP_REDIRECT=1' ${D}${sysconfdir}/init.d/ceph-radosgw
    install -m 0750 -D ${S}/src/init-rbdmap ${D}${sysconfdir}/rc.d/init.d/rbdmap
    install -m 0750 -D ${S}/src/init-rbdmap ${D}${sysconfdir}/init.d/rbdmap
    install -m 0750 -D ${B}/bin/init-ceph ${D}${sysconfdir}/rc.d/init.d/ceph
    install -m 0750 -D ${B}/bin/init-ceph ${D}${sysconfdir}/init.d/ceph
    install -d -m 0750 ${D}${localstatedir}/log/radosgw 
}

PACKAGES += " \
    ${PN}-python \
"

FILES_${PN} += "\
    ${libdir}/rados-classes/*.so.* \
    ${libdir}/ceph/compressor/*.so \
    ${libdir}/rados-classes/*.so \
    ${libdir}/ceph/*.so \
    ${localstatedir} \
    ${docdir}/ceph/COPYING \
    ${libdir}/sysctl.d/90-ceph-osd.conf \
    ${libdir}/udev/rules.d/50-rbd.rules \
    ${libdir}/udev/rules.d/60-ceph-by-parttypeuuid.rules \
    ${systemd_system_unitdir}/mgr-restful-plugin.service \
    ${systemd_system_unitdir}/ceph-radosgw@.service \
    ${systemd_system_unitdir}/ceph.service \
    ${systemd_system_unitdir}/docker.service.d/starlingx-docker-override.conf \
"
FILES_${PN}-python = "\
    ${PYTHON_SITEPACKAGES_DIR}/* \
"

RDEPENDS_${PN} += "\
    bash \
    python \
    python-misc \
    python-modules \
    python-prettytable \
    rdma-core \
    xfsprogs-mkfs \
    ${PN}-python \
"

COMPATIBLE_HOST = "(x86_64).*"

INSANE_SKIP_${PN}-python += "ldflags"
INSANE_SKIP_${PN} += "dev-so"
