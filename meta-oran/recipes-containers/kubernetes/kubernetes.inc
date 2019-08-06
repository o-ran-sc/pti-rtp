#
# Copyright (C) 2019 Wind River Systems, Inc.
#

require recipes-containers/kubernetes/kubernetes_git.bb

SRC_URI = " \
    git://github.com/kubernetes/kubernetes.git;branch=${SRC_BRANCH};name=kubernetes \
    file://0001-hack-lib-golang.sh-use-CC-from-environment.patch \
    file://0001-cross-don-t-build-tests-by-default.patch \
    "
