#!/bin/bash
#
# Copyright (C) 2022 Wind River Systems, Inc.
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

# Ensure we fail the job if any steps fail.
set -e -o pipefail

#########################################################################
# Variables
#########################################################################

SRC_ORAN_BRANCH="master"

SRC_ORAN_URL="https://gerrit.o-ran-sc.org/r/pti/rtp"

STX_VER="10.0"
ORAN_REL="ORAN L-Release (${STX_VER})"

SCRIPTS_DIR=$(dirname $(readlink -f $0))
SCRIPTS_NAME=$(basename $0)
TIMESTAMP=`date +"%Y%m%d_%H%M%S"`

#########################################################################
# Common Functions
#########################################################################

help_info () {
cat << ENDHELP
Usage:
${SCRIPTS_NAME} [-w WORKSPACE_DIR] [-p PARALLEL_BUILD] [-m] [-n] [-u] [-h]
where:
    -w WORKSPACE_DIR is the path for the project
    -p PARALLEL_BUILD is the num of paralle build, default is 2
    -m use mirror for src and deb pkgs
    -n dry-run only for bitbake
    -h this help info
examples:
$0
$0 -w workspace_1234
ENDHELP
}

echo_step_start() {
    [ -n "$1" ] && msg_step=$1
    echo "#########################################################################################"
    echo "## ${SCRIPTS_NAME} - STEP START: ${msg_step}"
    echo "#########################################################################################"
}

echo_step_end() {
    [ -n "$1" ] && msg_step=$1
    echo "#########################################################################################"
    echo "## ${SCRIPTS_NAME} - STEP END: ${msg_step}"
    echo "#########################################################################################"
    echo
}

echo_info () {
    echo "INFO: $1"
}

echo_error () {
    echo "ERROR: $1"
}

run_cmd () {
    echo
    echo_info "$1"
    echo "CMD: ${RUN_CMD}"
    ${RUN_CMD}
}

#########################################################################
# Parse cmd options
#########################################################################

DRYRUN=""
USE_MIRROR="No"
REUSE_APTLY=""
STX_PARALLEL="2"

while getopts "w:p:mnh" OPTION; do
    case ${OPTION} in
        w)
            WORKSPACE=`readlink -f ${OPTARG}`
            ;;
        p)
            STX_PARALLEL="${OPTARG}"
            ;;
        n)
            DRYRUN="-n"
            ;;
        m)
            USE_MIRROR="Yes"
            REUSE_APTLY="--reuse"
            ;;
        h)
            help_info
            exit
            ;;
    esac
done

if [ -z ${WORKSPACE} ]; then
    echo_info "No workspace specified, a directory 'workspace' will be created in current directory as the workspace"
    WORKSPACE=`readlink -f workspace`
fi

#########################################################################
# Functions for each step
#########################################################################

# "_" can't be used in project name
PRJ_NAME=prj-oran-stx-deb

STX_SRC_BRANCH="r/stx.${STX_VER}"

# Temporary for master
#STX_TAG="master-1ba0db8"
#STX_SRC_BRANCH="master"

STX_LOCAL_DIR=${WORKSPACE}/localdisk
STX_LOCAL_SRC_DIR=${STX_LOCAL_DIR}/designer/${USER}/${PRJ_NAME}
STX_LOCAL_PRJ_DIR=${STX_LOCAL_DIR}/loadbuild/${USER}/${PRJ_NAME}
STX_SRC_DIR=${WORKSPACE}/src
STX_PRJ_DIR=${WORKSPACE}/${PRJ_NAME}
STX_PRJ_OUTPUT=${WORKSPACE}/prj_output
STX_MIRROR_DIR=${WORKSPACE}/mirrors
STX_APTLY_DIR=${WORKSPACE}/aptly
STX_MINIKUBE_HOME=${WORKSPACE}/minikube_home
STX_MANIFEST_URL="https://opendev.org/starlingx/manifest"

MIRROR_SRC_STX=infbuilder/inf-src-stx:${STX_VER}
#MIRROR_SRC_STX=infbuilder/inf-src-stx:${STX_TAG}
MIRROR_CONTAINER_IMG=infbuilder/inf-debian-mirror:2022.11-stx-${STX_VER}
MIRROR_APTLY_IMG=infbuilder/inf-debian-aptly:2022.11-stx-${STX_VER}

STX_SHARED_REPO=http://mirror.starlingx.cengn.ca/mirror/starlingx/master/debian/monolithic/20221123T070000Z/outputs/aptly/deb-local-build/
STX_SHARED_SOURCE=http://mirror.starlingx.cengn.ca/mirror/starlingx/master/debian/monolithic/20221123T070000Z/outputs/aptly/deb-local-source/

SRC_ORAN_DIR=${STX_SRC_DIR}/rtp
SRC_META_PATCHES=${SRC_ORAN_DIR}/scripts/build_inf_debian/meta-patches

ISO_INF_DEB=inf-image-debian-all-x86-64.iso

prepare_workspace () {
    msg_step="Create workspace for the Debian build"
    echo_step_start

    mkdir -p ${STX_LOCAL_SRC_DIR} ${STX_LOCAL_PRJ_DIR} ${STX_MIRROR_DIR} \
        ${STX_APTLY_DIR} ${STX_PRJ_OUTPUT} ${STX_MINIKUBE_HOME}
    rm -f ${STX_SRC_DIR} ${STX_PRJ_DIR}
    ln -sf $(realpath --relative-to=${WORKSPACE} ${STX_LOCAL_SRC_DIR}) ${STX_SRC_DIR}
    ln -sf $(realpath --relative-to=${WORKSPACE} ${STX_LOCAL_PRJ_DIR}) ${STX_PRJ_DIR}

    echo_info "The following directories are created in your workspace(${WORKSPACE}):"
    echo_info "For all source repos: ${STX_SRC_DIR}"
    echo_info "For StarlingX deb pkgs mirror: ${STX_MIRROR_DIR}"
    echo_info "For StarlingX build project: ${STX_PRJ_DIR}"

    echo_step_end
}

create_env () {
    msg_step="Create env file for the Debian build"
    echo_step_start

    ENV_FILENAME=env.${PRJ_NAME}

    cat <<EOF > ${WORKSPACE}/${ENV_FILENAME}

export STX_BUILD_HOME=${WORKSPACE}
export PROJECT=${PRJ_NAME}
export STX_MIRROR_DIR=${STX_MIRROR_DIR}
export STX_REPO_ROOT=${STX_SRC_DIR}
#export STX_REPO_ROOT_SUBDIR="localdisk/designer/${USER}/${PRJ_NAME}"

export USER_NAME=${USER}
export USER_EMAIL=${USER}@windriver.com

# MINIKUBE
export STX_PLATFORM="minikube"
export STX_MINIKUBENAME="minikube-${USER}"
export MINIKUBE_HOME=${STX_MINIKUBE_HOME}

# Manifest/Repo Options:
export STX_MANIFEST_URL="${STX_MANIFEST_URL}"
export STX_MANIFEST_BRANCH="master"
export STX_MANIFEST="default.xml"

EOF

    echo_info "Env file created at ${WORKSPACE}/$ENV_FILENAME"
    cat ${WORKSPACE}/$ENV_FILENAME

    source ${WORKSPACE}/${ENV_FILENAME}

    git config --global user.email "${USER_EMAIL}"
    git config --global user.name "${USER_NAME}"
    git config --global color.ui false

    echo_step_end
}

repo_init_sync () {
    msg_step="Init the repo and sync"
    echo_step_start

    # Avoid the colorization prompt
    git config --global color.ui false

    if [ -d ${STX_REPO_ROOT}/.repo ]; then
        echo_info "the src repos already exists, skipping"
    else
        cd ${STX_REPO_ROOT}

        RUN_CMD="repo init -u ${STX_MANIFEST_URL} -b ${STX_SRC_BRANCH} -m ${STX_MANIFEST}"
        run_cmd "Init the repo from manifest"

        RUN_CMD="repo sync --force-sync"
        run_cmd "repo sync"

        touch .repo-init-done
    fi

    echo_step_end
}

get_mirror_src () {
    msg_step="Get src mirror from dockerhub image"
    echo_step_start

    if [ -d ${STX_REPO_ROOT}/.repo ]; then
        echo_info "the src repos already exists, skipping"
    else
        docker pull ${MIRROR_SRC_STX}
        docker create -ti --name inf-src-stx ${MIRROR_SRC_STX} sh
        #docker cp inf-src-stx:/stx-${STX_VER}.tar.bz2 ${STX_REPO_ROOT}
        docker cp inf-src-stx:/stx-${STX_TAG}.tar.bz2 ${STX_REPO_ROOT}
        docker rm inf-src-stx

        cd ${STX_REPO_ROOT}
        #tar xf stx-${STX_VER}.tar.bz2
        #mv stx-${STX_VER}/* stx-${STX_VER}/.repo .
        #rm -rf stx-${STX_VER} stx-${STX_VER}.tar.bz2
        tar xf stx-${STX_TAG}.tar.bz2
        mv stx-${STX_SRC_BRANCH}/* stx-${STX_SRC_BRANCH}/.repo .
        rm -rf stx-${STX_SRC_BRANCH} stx-${STX_TAG}.tar.bz2
        touch .repo-init-done

    fi

    echo_step_end
}

get_mirror_pkg () {
    msg_step="Get deb mirror from dockerhub image"
    echo_step_start

    if [ -d ${STX_MIRROR_DIR}/starlingx ]; then
        echo_info "The deb mirror already exists, skipping"
    else
        docker pull ${MIRROR_CONTAINER_IMG}
        docker create -ti --name inf-debian-mirror ${MIRROR_CONTAINER_IMG} sh
        docker cp inf-debian-mirror:/mirror-stx-${STX_VER}/. ${STX_MIRROR_DIR}
        docker rm inf-debian-mirror
    fi

    echo_step_end
}

get_mirror_aptly () {
    msg_step="Get deb mirror aptly from dockerhub image"
    echo_step_start

    if [ -f ${STX_APTLY_DIR}/aptly.conf ]; then
        echo_info "The deb aptly already exists, skipping"
    else
        docker pull ${MIRROR_APTLY_IMG}
        docker create -ti --name inf-debian-aptly ${MIRROR_APTLY_IMG} sh
        docker cp inf-debian-aptly:/aptly-stx-${STX_VER}/. ${STX_APTLY_DIR}
        docker rm inf-debian-aptly
    fi

    echo_step_end
}

clone_update_repo () {
    REPO_BRANCH=$1
    REPO_URL=$2
    REPO_NAME=$3
    REPO_COMMIT=$4

    if [ -d ${REPO_NAME}/.git ]; then
        if [ "${SKIP_UPDATE}" == "Yes" ]; then
            echo_info "The repo ${REPO_NAME} exists, skip updating for the branch ${REPO_BRANCH}"
        else
            echo_info "The repo ${REPO_NAME} exists, updating for the branch ${REPO_BRANCH}"
            cd ${REPO_NAME}
            git checkout ${REPO_BRANCH}
            git pull
            cd -
        fi
    else
        RUN_CMD="git clone --branch ${REPO_BRANCH} ${REPO_URL} ${REPO_NAME}"
        run_cmd "Cloning the source of repo '${REPO_NAME}':"

        if [ -n "${REPO_COMMIT}" ]; then
            cd ${REPO_NAME}
            RUN_CMD="git checkout -b ${REPO_BRANCH}-${REPO_COMMIT} ${REPO_COMMIT}"
            run_cmd "Checkout the repo ${REPO_NAME} to specific commit: ${REPO_COMMIT}"
            cd -
        fi
    fi
}


prepare_src () {
    msg_step="Get the source code repos"
    echo_step_start

    # Clone the oran inf source if it's not already cloned
    # Check if the script is inside the repo
    if cd ${SCRIPTS_DIR} && git rev-parse --is-inside-work-tree > /dev/null 2>&1; then
        CLONED_ORAN_REPO=`git rev-parse --show-toplevel`
        echo_info "Use the cloned oran repo: ${CLONED_ORAN_REPO}"
        mkdir -p ${SRC_ORAN_DIR}
        cd ${SRC_ORAN_DIR}
        rm -rf scripts
        ln -sf ${CLONED_ORAN_REPO}/scripts scripts
    else
        echo_info "Cloning oran layer:"
        cd ${STX_SRC_DIR}
        clone_update_repo ${SRC_ORAN_BRANCH} ${SRC_ORAN_URL} rtp
    fi

    if [ "${USE_MIRROR}" == "Yes" ]; then
        get_mirror_src
        get_mirror_pkg
        get_mirror_aptly
    else
        repo_init_sync
    fi
    patch_src

    echo_step_end
}


patch_src () {
    echo_step_start "Patching source codes for INF project"

    STX_ISSUE_DIR="${STX_REPO_ROOT}/cgcs-root/stx/config-files/debian-release-config/files"
    echo_info "Patching for the ${STX_ISSUE_DIR}"
    grep -q "${ORAN_REL}" ${STX_ISSUE_DIR}/issue* \
        || sed -i "s/\(@PLATFORM_RELEASE@\)/\1 - ${ORAN_REL}/" ${STX_ISSUE_DIR}/issue*

    STX_BUILDER="${STX_REPO_ROOT}/stx-tools/stx/lib/stx/stx_build.py"
    echo_info "Patching for the ${STX_BUILDER}"
    grep -q "\-\-parallel" ${STX_BUILDER} \
        || sed -i "s/\(build-pkgs -a \)/\1 --parallel ${STX_PARALLEL} ${REUSE_APTLY}/" \
        ${STX_BUILDER}

    STX_LOCALRC="${STX_REPO_ROOT}/stx-tools/stx/stx-build-tools-chart/stx-builder/configmap/localrc.sample"
    echo_info "Patching for the localrc file"
    echo "export STX_SHARED_REPO=${STX_SHARED_REPO}" >> ${STX_LOCALRC}
    echo "export STX_SHARED_SOURCE=${STX_SHARED_SOURCE}" >> ${STX_LOCALRC}

    # Apply meta patches
    if [ -d ${SRC_META_PATCHES} ]; then
        cd ${SRC_META_PATCHES}
        src_dirs=$(find . -type f -printf "%h\n"|uniq)
        for d in ${src_dirs}; do
            cd ${STX_REPO_ROOT}/${d}

            # backup current branch
            local_branch=$(git rev-parse --abbrev-ref HEAD)
            if [ "${local_branch}" = "HEAD" ]; then
                git checkout ${STX_SRC_BRANCH}
                local_branch=$(git rev-parse --abbrev-ref HEAD)
            fi
            git branch -m "${local_branch}_${TIMESTAMP}"
            git checkout ${STX_SRC_BRANCH}

            for p in $(ls -1 ${SRC_META_PATCHES}/${d}); do
                echo_info "Apllying patch: ${SRC_META_PATCHES}/${d}/${p}"
                git am ${SRC_META_PATCHES}/${d}/${p}
            done
        done
    fi

    echo_step_end
}

init_stx_tool () {
    echo_step_start "Init stx tool"

    cd ${STX_REPO_ROOT}
    cd stx-tools
    cp stx.conf.sample stx.conf
    source import-stx

    # Update stx config
    # Align the builder container to use your user/UID
    stx config --add builder.myuname $(id -un)
    stx config --add builder.uid $(id -u)

    # Embedded in ~/localrc of the build container
    stx config --add project.gituser ${USER_NAME}
    stx config --add project.gitemail ${USER_EMAIL}

    # This will be included in the name of your build container and the basename for $STX_REPO_ROOT
    stx config --add project.name ${PRJ_NAME}

    #stx config --add project.proxy true
    #stx config --add project.proxyserver 147.11.252.42
    #stx config --add project.proxyport 9090

    stx config --show

    echo_step_end
}

build_image () {
    echo_step_start "Build Debian images"

    cd ${STX_REPO_ROOT}/stx-tools
    RUN_CMD="./stx-init-env"
    run_cmd "Run stx-init-env script"

    stx control status

    # wait for all the pods running
    sleep 600
    stx control status

    RUN_CMD="stx build prepare"
    run_cmd "Build prepare"

    RUN_CMD="stx build download"
    run_cmd "Download packges"

    RUN_CMD="stx repomgr list"
    run_cmd "repomgr list"

    RUN_CMD="stx build world"
    run_cmd "Build-pkgs"

    RUN_CMD="stx build image"
    run_cmd "Build ISO image"

    cp -f ${STX_LOCAL_DIR}/deploy/starlingx-intel-x86-64-cd.iso ${STX_PRJ_OUTPUT}/${ISO_INF_DEB}

    echo_step_end

    echo_info "Build succeeded, you can get the image in ${STX_PRJ_OUTPUT}/${ISO_INF_DEB}"
}

#########################################################################
# Main process
#########################################################################

prepare_workspace
create_env
prepare_src
init_stx_tool
build_image
