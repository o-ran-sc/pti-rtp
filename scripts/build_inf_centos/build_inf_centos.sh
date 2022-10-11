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

STX_VER="6.0"
ORAN_REL="ORAN G-Release (${STX_VER})"

SCRIPTS_DIR=$(dirname $(readlink -f $0))
SCRIPTS_NAME=$(basename $0)
TIMESTAMP=`date +"%Y%m%d_%H%M%S"`

#########################################################################
# Common Functions
#########################################################################

help_info () {
cat << ENDHELP
Usage:
${SCRIPTS_NAME} [-w WORKSPACE_DIR] [-m] [-n] [-u] [-h]
where:
    -w WORKSPACE_DIR is the path for the project
    -m use mirror for src and rpm pkgs
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

while getopts "w:mnh" OPTION; do
    case ${OPTION} in
        w)
            WORKSPACE=`readlink -f ${OPTARG}`
            ;;
        n)
            DRYRUN="-n"
            ;;
        m)
            USE_MIRROR="Yes"
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
PRJ_NAME=prj_oran_stx_centos

STX_SRC_BRANCH="r/stx.${STX_VER}"
STX_LOCAL_DIR=${WORKSPACE}/localdisk
STX_LOCAL_SRC_DIR=${STX_LOCAL_DIR}/designer/${USER}/${PRJ_NAME}
STX_LOCAL_PRJ_DIR=${STX_LOCAL_DIR}/loadbuild/${USER}/${PRJ_NAME}
STX_SRC_DIR=${WORKSPACE}/src
STX_PRJ_DIR=${WORKSPACE}/${PRJ_NAME}
STX_PRJ_OUTPUT=${WORKSPACE}/prj_output
STX_MIRROR_DIR=${WORKSPACE}/mirror
STX_MANIFEST_URL="https://opendev.org/starlingx/manifest"

MIRROR_SRC_STX=infbuilder/inf-src-stx:${STX_VER}
MIRROR_CONTAINER_IMG=infbuilder/inf-centos-mirror:2022.05-stx-${STX_VER}

SRC_META_PATCHES=${SCRIPTS_DIR}/meta-patches

ISO_INF_COS=inf-image-centos-all-x86-64.iso

prepare_workspace () {
    msg_step="Create workspace for the CentOS build"
    echo_step_start

    mkdir -p ${STX_LOCAL_SRC_DIR} ${STX_LOCAL_PRJ_DIR} ${STX_MIRROR_DIR} ${STX_PRJ_OUTPUT}
    rm -f ${STX_SRC_DIR} ${STX_PRJ_DIR}
    ln -sf $(realpath --relative-to=${WORKSPACE} ${STX_LOCAL_SRC_DIR}) ${STX_SRC_DIR}
    ln -sf $(realpath --relative-to=${WORKSPACE} ${STX_LOCAL_PRJ_DIR}) ${STX_PRJ_DIR}

    echo_info "The following directories are created in your workspace(${WORKSPACE}):"
    echo_info "For all layers source: ${STX_SRC_DIR}"
    echo_info "For StarlingX rpm mirror: ${STX_MIRROR_DIR}"
    echo_info "For StarlingX build project: ${STX_PRJ_DIR}"

    echo_step_end
}

create_env () {
    msg_step="Create env file for the CentOS build"
    echo_step_start

    ENV_FILENAME=env.${PRJ_NAME}

    cat <<EOF > ${WORKSPACE}/${ENV_FILENAME}

export STX_MIRROR_DIR=${STX_MIRROR_DIR}

#######################################
#       Upstream variables
#######################################

export LC_ALL=en_US.UTF-8
export PROJECT=${PRJ_NAME}
export SRC_BUILD_ENVIRONMENT=tis-r6-pike
export MY_LOCAL_DISK=${STX_SRC_DIR}
export MY_REPO_ROOT_DIR=\${MY_LOCAL_DISK}
export MY_REPO=\${MY_REPO_ROOT_DIR}/cgcs-root
export CGCSDIR=\${MY_REPO}/stx
export MY_WORKSPACE=${WORKSPACE}/\${PROJECT}
export MY_BUILD_ENVIRONMENT=\${USER}-\${PROJECT}-\${SRC_BUILD_ENVIRONMENT}
export MY_BUILD_ENVIRONMENT_FILE=\${MY_BUILD_ENVIRONMENT}.cfg
export MY_BUILD_ENVIRONMENT_FILE_STD=\${MY_BUILD_ENVIRONMENT}-std.cfg
export MY_BUILD_ENVIRONMENT_FILE_RT=\${MY_BUILD_ENVIRONMENT}-rt.cfg
export MY_BUILD_ENVIRONMENT_FILE_STD_B0=\${MY_WORKSPACE}/std/configs/\${MY_BUILD_ENVIRONMENT}-std/\${MY_BUILD_ENVIRONMENT}-std.b0.cfg
export MY_BUILD_ENVIRONMENT_FILE_RT_B0=\${MY_WORKSPACE}/rt/configs/\${MY_BUILD_ENVIRONMENT}-rt/\${MY_BUILD_ENVIRONMENT}-rt.b0.cfg
export MY_BUILD_DIR=${WORKSPACE}/\${PROJECT}
export MY_SRC_RPM_BUILD_DIR=\${MY_BUILD_DIR}/rpmbuild
export MY_BUILD_CFG=\${MY_WORKSPACE}/\${MY_BUILD_ENVIRONMENT_FILE}
export MY_BUILD_CFG_STD=\${MY_WORKSPACE}/std/\${MY_BUILD_ENVIRONMENT_FILE_STD}
export MY_BUILD_CFG_RT=\${MY_WORKSPACE}/rt/\${MY_BUILD_ENVIRONMENT_FILE_RT}
export PATH=\${MY_REPO}/build-tools:\${MY_LOCAL_DISK}/bin:\${CGCSDIR}/stx-update/extras/scripts:\${PATH}
export CGCSPATCH_DIR=\${CGCSDIR}/stx-update/cgcs-patch
export BUILD_ISO_USE_UDEV=1

# WRCP/WRA/WRO do not support layered builds at this time.
export LAYER=""

# StarlingX since 4.0 supports layered builds (compiler, distro, flock) as an option.
# Note: Only flock layer builds an iso at this time.
# Note: You may leave LAYER="", this will build everything, also known as a 'monolithic' build.
# export LAYER=compiler
# export LAYER=distro
# export LAYER=flock

# Upstream issue seems to have been corrected
# export REPO_VERSION="--repo-branch=repo-1"
export REPO_VERSION=

# In order to avoid running out of space in your home directory
export XDG_CACHE_HOME=\${MY_LOCAL_DISK}/.cache;
export XDG_DATA_HOME=\${MY_LOCAL_DISK}

#/bin/title "\${HOSTNAME} \${PROJECT}"

alias patch_build=\${MY_REPO}/stx/update/extras/scripts/patch_build.sh

alias cdrepo="cd \$MY_REPO_ROOT_DIR"
alias cdbuild="cd \$MY_BUILD_DIR"

cd \${MY_REPO_ROOT_DIR}

EOF

    echo_info "Env file created at ${WORKSPACE}/$ENV_FILENAME"

    source ${WORKSPACE}/${ENV_FILENAME}

    echo_step_end
}

repo_init_sync () {
    msg_step="Init the repo and sync"
    echo_step_start

    # Avoid the colorization prompt
    git config --global color.ui false

    cd ${MY_REPO_ROOT_DIR}
    STX_MANIFEST="default.xml"
    if [ "$LAYER" != "" ]; then
        STX_MANIFEST=${LAYER}.xml
    fi

    RUN_CMD="repo init ${REPO_VERSION} -u ${STX_MANIFEST_URL} -b ${STX_SRC_BRANCH} -m ${STX_MANIFEST}"
    run_cmd "Init the repo from manifest"

    RUN_CMD="repo sync --force-sync"
    run_cmd "repo sync"

    echo_step_end
}

get_mirror_src () {
    msg_step="Get src mirror from dockerhub image"
    echo_step_start

    if [ -d ${MY_REPO_ROOT_DIR}/.repo ]; then
        echo_info "The src repos already exists, skipping"
    else
        docker pull ${MIRROR_SRC_STX}
        docker create -ti --name inf-src-stx ${MIRROR_SRC_STX} sh
        docker cp inf-src-stx:/stx-${STX_VER}.tar.bz2 ${MY_REPO_ROOT_DIR}
        docker rm inf-src-stx

        cd ${MY_REPO_ROOT_DIR}
        tar xf stx-${STX_VER}.tar.bz2
        mv stx-${STX_VER}/* stx-${STX_VER}/.repo .
        rm -rf stx-${STX_VER} stx-${STX_VER}.tar.bz2
    fi

    echo_step_end
}

get_mirror_pkg () {
    msg_step="Get rpm mirror from dockerhub image"
    echo_step_start

    docker pull ${MIRROR_CONTAINER_IMG}
    docker create -ti --name inf-centos-mirror ${MIRROR_CONTAINER_IMG} sh
    docker cp inf-centos-mirror:/mirror_stx-${STX_VER}/. ${STX_MIRROR_DIR}
    docker rm inf-centos-mirror

    echo_step_end
}

patch_src () {
    echo_step_start "Some source codes need to be patched for INF project"

    sed -i "s|/import/mirrors|${STX_MIRROR_DIR}|" \
        ${MY_REPO}/stx/metal/installer/pxe-network-installer/centos/build_srpm.data

    grep -q "${ORAN_REL}" \
        ${MY_REPO}/stx/config-files/centos-release-config/files/issue* \
        || sed -i "s/\(@PLATFORM_RELEASE@\)/\1 - ${ORAN_REL}/" \
        ${MY_REPO}/stx/config-files/centos-release-config/files/issue*

    # Apply meta patches
    cd ${SRC_META_PATCHES}
    src_dirs=$(find . -type f -printf "%h\n"|uniq)
    for d in ${src_dirs}; do
        cd ${MY_REPO_ROOT_DIR}/${d}

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

    echo_step_end
}

populate_dl () {
    ${MY_REPO_ROOT_DIR}/stx-tools/toCOPY/generate-centos-repo.sh ${STX_MIRROR_DIR}/stx-${STX_VER}
    ${MY_REPO_ROOT_DIR}/stx-tools/toCOPY/populate_downloads.sh ${STX_MIRROR_DIR}/stx-${STX_VER}
}

build_image () {
    echo_step_start "Build CentOS images"

    mkdir -p ${STX_PRJ_OUTPUT}
    cd ${MY_BUILD_DIR}
    RUN_CMD="build-pkgs --build-avoidance"
    run_cmd "Build pkgs"

    # The first run always fail
    build-iso || sleep 60

    RUN_CMD="build-iso"
    run_cmd "Build ISO image"

    cp -f ${MY_BUILD_DIR}/export/bootimage.iso ${STX_PRJ_OUTPUT}/${ISO_INF_COS}

    echo_step_end

    echo_info "Build succeeded, you can get the image in ${STX_PRJ_OUTPUT}/${ISO_INF_COS}"
}

#########################################################################
# Main process
#########################################################################

prepare_workspace
create_env
if [ "${USE_MIRROR}" == "Yes" ]; then
    get_mirror_src
    get_mirror_pkg
else
    repo_init_sync
fi
patch_src
populate_dl
build_image
