#
# Copyright (C) 2019 Wind River Systems, Inc.
#

require kubernetes.inc

PV = "1.15.2+git${SRCREV_kubernetes}"
SRCREV_kubernetes = "f6278300bebbb750328ac16ee6dd3aa7d3549568"
SRC_BRANCH = "release-1.15"

# The docker images included in docker-img-kubernetes-v1.15.2.tar.bz2:
# k8s.gcr.io/kube-scheduler                             v1.15.2               88fa9cb27bd2        2 months ago        81.1MB
# k8s.gcr.io/kube-proxy                                 v1.15.2               167bbf6c9338        2 months ago        82.4MB
# k8s.gcr.io/kube-controller-manager                    v1.15.2               9f5df470155d        2 months ago        159MB
# k8s.gcr.io/kube-apiserver                             v1.15.2               34a53be6c9a7        2 months ago        207MB
# k8s.gcr.io/coredns                                    1.3.1                 eb516548c180        9 months ago        40.3MB
# k8s.gcr.io/etcd                                       3.3.10                2c4adeb21b4f        10 months ago       258MB

SRC_URI += "\
    file://k8s.conf \
    file://docker-img-kubernetes-v1.15.2.tar.bz2;unpack=0 \
"

inherit go112

PACKAGES =+ "${PN}-img"

DOCKER_IMG = "/opt/docker_images/${BPN}"

do_install_append() {
    # Install the saved docker image
    install -d ${D}${DOCKER_IMG}
    install -m 644 ${WORKDIR}/docker-img-*.tar.bz2 ${D}${DOCKER_IMG}

    # Install the sysctl config for k8s
    install -d ${D}${sysconfdir}/sysctl.d/
    install -m 644 -D ${WORKDIR}/k8s.conf ${D}${sysconfdir}/sysctl.d/
}

FILES_${PN}-img = "${DOCKER_IMG}"
