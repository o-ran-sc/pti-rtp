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

S = "${WORKDIR}/git"
BRANCH = "stx/v${PV}"
SRCREV = "7567060fea1f8d719d317277f1eb01161cf3bfef"

BRANCH_lua = "lua-5.3-ceph"
SRCREV_lua = "1fce39c6397056db645718b8f5821571d97869a4"
DESTSUF_lua = "git/src/lua"

BRANCH_ceph-object-corpus = "master"
SRCREV_ceph-object-corpus = "e32bf8ca3dc6151ebe7f205ba187815bc18e1cef"
DESTSUF_ceph-object-corpus = "git/ceph-object-corpus"

BRANCH_civetweb = "ceph-mimic"
SRCREV_civetweb = "ff2881e2cd5869a71ca91083bad5d12cccd22136"
DESTSUF_civetweb = "git/src/civetweb"

BRANCH_jerasure = "v2-ceph"
SRCREV_jerasure = "96c76b89d661c163f65a014b8042c9354ccf7f31"
DESTSUF_jerasure = "git/src/erasure-code/jerasure/jerasure"

BRANCH_gf-complete = "v3-ceph"
SRCREV_gf-complete = "7e61b44404f0ed410c83cfd3947a52e88ae044e1"
DESTSUF_gf-complete = "git/src/erasure-code/jerasure/gf-complete"

BRANCH_xxHash = "master"
SRCREV_xxHash = "1f40c6511fa8dd9d2e337ca8c9bc18b3e87663c9"
DESTSUF_xxHash = "git/src/xxHash"

# the tag v1.3.2 is not on any branch
BRANCH_zstd = "nobranch=1"
SRCREV_zstd = "f4340f46b2387bc8de7d5320c0b83bb1499933ad"
DESTSUF_zstd = "git/src/zstd"

BRANCH_rocksdb = "ceph-mimic"
SRCREV_rocksdb = "f4a857da0b720691effc524469f6db895ad00d8e"
DESTSUF_rocksdb = "git/src/rocksdb"

BRANCH_ceph-erasure-code-corpus = "master"
SRCREV_ceph-erasure-code-corpus = "2d7d78b9cc52e8a9529d8cc2d2954c7d375d5dd7"
DESTSUF_ceph-erasure-code-corpus = "git/ceph-erasure-code-corpus"

BRANCH_spdk = "wip-25032-mimic"
SRCREV_spdk = "f474ce6930f0a44360e1cc4ecd606d2348481c4c"
DESTSUF_spdk = "git/src/spdk"

BRANCH_isa-l = "master"
SRCREV_isa-l = "7e1a337433a340bc0974ed0f04301bdaca374af6"
DESTSUF_isa-l = "git/src/isa-l"

BRANCH_blkin = "master"
SRCREV_blkin = "f24ceec055ea236a093988237a9821d145f5f7c8"
DESTSUF_blkin = "git/src/blkin"

BRANCH_rapidjson = "master"
SRCREV_rapidjson = "f54b0e47a08782a6131cc3d60f94d038fa6e0a51"
DESTSUF_rapidjson = "git/src/rapidjson"

BRANCH_googletest = "ceph-release-1.7.x"
SRCREV_googletest = "fdb850479284e2aae047b87df6beae84236d0135"
DESTSUF_googletest = "git/src/googletest"

BRANCH_crypto = "master"
SRCREV_crypto = "603529a4e06ac8a1662c13d6b31f122e21830352"
DESTSUF_crypto = "git/src/crypto/isa-l/isa-l_crypto"

BRANCH_rapidjson-gtest = "ceph-release-1.7.x"
SRCREV_rapidjson-gtest = "0a439623f75c029912728d80cb7f1b8b48739ca4"
DESTSUF_rapidjson-gtest = "git/src/rapidjson/thirdparty/gtest"


SRC_URI = "\
    git://github.com/starlingx-staging/stx-ceph.git;branch=${BRANCH} \
    git://github.com/ceph/lua;name=lua;branch=${BRANCH_lua};destsuffix=${DESTSUF_lua} \
    git://github.com/ceph/ceph-object-corpus;name=ceph-object-corpus;branch=${BRANCH_ceph-object-corpus};destsuffix=${DESTSUF_ceph-object-corpus} \
    git://github.com/ceph/civetweb;name=civetweb;branch=${BRANCH_civetweb};destsuffix=${DESTSUF_civetweb} \
    git://github.com/ceph/jerasure;name=jerasure;branch=${BRANCH_jerasure};destsuffix=${DESTSUF_jerasure} \
    git://github.com/ceph/gf-complete;name=gf-complete;branch=${BRANCH_gf-complete};destsuffix=${DESTSUF_gf-complete} \
    git://github.com/ceph/xxHash;name=xxHash;branch=${BRANCH_xxHash};destsuffix=${DESTSUF_xxHash} \
    git://github.com/facebook/zstd;name=zstd;${BRANCH_zstd};destsuffix=${DESTSUF_zstd} \
    git://github.com/ceph/rocksdb;name=rocksdb;branch=${BRANCH_rocksdb};destsuffix=${DESTSUF_rocksdb} \
    git://github.com/ceph/ceph-erasure-code-corpus;name=ceph-erasure-code-corpus;branch=${BRANCH_ceph-erasure-code-corpus};destsuffix=${DESTSUF_ceph-erasure-code-corpus} \
    git://github.com/ceph/spdk;name=spdk;branch=${BRANCH_spdk};destsuffix=${DESTSUF_spdk} \
    git://github.com/ceph/isa-l;name=isa-l;branch=${BRANCH_isa-l};destsuffix=${DESTSUF_isa-l} \
    git://github.com/intel/isa-l_crypto;name=crypto;branch=${BRANCH_crypto};destsuffix=${DESTSUF_crypto} \
    git://github.com/ceph/blkin;name=blkin;branch=${BRANCH_blkin};destsuffix=${DESTSUF_blkin} \
    git://github.com/ceph/rapidjson;name=rapidjson;branch=${BRANCH_rapidjson};destsuffix=${DESTSUF_rapidjson} \
    git://github.com/ceph/googletest;name=googletest;branch=${BRANCH_googletest};destsuffix=${DESTSUF_googletest} \
    git://github.com/ceph/googletest;name=rapidjson-gtest;branch=${BRANCH_rapidjson-gtest};destsuffix=${DESTSUF_rapidjson-gtest} \
    \
    file://0001-Correct-the-path-to-find-version.h-in-rocksdb.patch \
    file://0002-zstd-fix-error-for-cross-compile.patch \
    file://0003-ceph-add-pybind-support-in-OE.patch \
    file://0004-ceph-detect-init-correct-the-installation-for-OE.patch \
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
SYSTEMD_AUTO_ENABLE = "disable"

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
    -DCMAKE_INSTALL_INITCEPH=${sysconfdir}/init.d \
"

do_configure_prepend () {
    echo "set( CMAKE_SYSROOT \"${RECIPE_SYSROOT}\" )" >> ${WORKDIR}/toolchain.cmake
    echo "set( CMAKE_DESTDIR \"${D}\" )" >> ${WORKDIR}/toolchain.cmake
    echo "set( PYTHON_SITEPACKAGES_DIR \"${PYTHON_SITEPACKAGES_DIR}\" )" >> ${WORKDIR}/toolchain.cmake
    ln -sf ${STAGING_LIBDIR}/libboost_python27.so ${STAGING_LIBDIR}/libboost_python.so
    echo ${SRCREV} > ${S}/src/.git_version
    echo v${PV} >> ${S}/src/.git_version
}

do_install_append () {
    mv ${D}${bindir}/ceph-disk ${D}${sbindir}/ceph-disk
    sed -i -e 's:${WORKDIR}.*python2.7:${bindir}/python:' ${D}${sbindir}/ceph-disk
    sed -i -e 's:${sbindir}/service:${bindir}/service:' ${D}/${libdir}/python2.7/site-packages/ceph_disk/main.py
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
    sed -i -e 's|/usr/lib64|${libdir}|' ${D}${sysconfdir}/init.d/ceph ${D}${sysconfdir}/rc.d/init.d/ceph
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
