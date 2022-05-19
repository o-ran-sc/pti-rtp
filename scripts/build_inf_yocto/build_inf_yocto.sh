#!/bin/bash
#
# Copyright (C) 2019 Wind River Systems, Inc.
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

# Only one bsp is supported now, there will be more in the future
SUPPORTED_BSP="intel-corei7-64 "

SRC_ORAN_BRANCH="master"
SRC_STX_BRANCH="master"
SRC_YP_BRANCH="warrior"

SRC_ORAN_URL="https://gerrit.o-ran-sc.org/r/pti/rtp"

SRC_STX_URL="https://opendev.org/starlingx/meta-starlingx"

SRC_YP_URL="\
    git://git.yoctoproject.org/poky;commit=HEAD \
    git://git.openembedded.org/meta-openembedded;commit=HEAD \
    git://git.openembedded.org/meta-python2;commit=HEAD \
    git://git.yoctoproject.org/meta-virtualization;commit=HEAD \
    git://git.yoctoproject.org/meta-cloud-services;commit=HEAD \
    git://git.yoctoproject.org/meta-security;commit=HEAD \
    git://git.yoctoproject.org/meta-intel;commit=HEAD \
    git://git.yoctoproject.org/meta-selinux;commit=HEAD \
    git://git.yoctoproject.org/meta-dpdk;commit=HEAD \
    git://git.yoctoproject.org/meta-anaconda;commit=HEAD \
    https://github.com/intel-iot-devkit/meta-iot-cloud.git;commit=HEAD \
"

SUB_LAYER_META_OE="\
    meta-oe \
    meta-perl \
    meta-python \
    meta-networking \
    meta-filesystems \
    meta-webserver \
    meta-initramfs \
    meta-initramfs \
    meta-gnome \
"

SUB_LAYER_META_CLOUD_SERVICES="meta-openstack"
SUB_LAYER_STX="\
    meta-stx-cloud \
    meta-stx-distro \
    meta-stx-flock \
    meta-stx-integ \
    meta-stx-virt \
"

# For anaconda build
SUB_LAYER_META_OE_ANACONDA="\
    meta-oe \
    meta-python \
    meta-filesystems \
    meta-initramfs \
    meta-networking \
    meta-gnome \
"

SCRIPTS_DIR=$(dirname $(dirname $(readlink -f $0)))
TIMESTAMP=`date +"%Y%m%d_%H%M%S"`

#########################################################################
# Common Functions
#########################################################################

help_info () {
cat << ENDHELP
Usage:
$(basename $0) [-w WORKSPACE_DIR] [-b BSP] [-n] [-u] [-h] [-r Yes|No] [-e EXTRA_CONF]
where:
    -w WORKSPACE_DIR is the path for the project
    -b BPS is one of supported BSP: "${SUPPORTED_BSP}"
       (default is intel-corei7-64 if not specified.)
    -n dry-run only for bitbake
    -u update the repo if it exists
    -h this help info
    -e EXTRA_CONF is the pat for extra config file
    -r whether to inherit rm_work (default is Yes)
examples:
$0
$0 -w workspace_1234 -r no -e /path/to/extra_local.conf
ENDHELP
}

echo_step_start() {
    [ -n "$1" ] && msg_step=$1
    echo "#########################################################################################"
    echo "## STEP START: ${msg_step}"
    echo "#########################################################################################"
}

echo_step_end() {
    [ -n "$1" ] && msg_step=$1
    echo "#########################################################################################"
    echo "## STEP END: ${msg_step}"
    echo "#########################################################################################"
    echo
}

echo_info () {
    echo "INFO: $1"
}

echo_error () {
    echo "ERROR: $1"
}

echo_cmd () {
    echo
    echo_info "$1"
    echo "CMD: ${RUN_CMD}"
}

check_yn_rm_work () {
    yn="$1"
    case ${yn} in
        [Yy]|[Yy]es)
            RM_WORK="Yes"
            ;;
        [Nn]|[Nn]o)
            RM_WORK="No"
            ;;
        *)
            echo "Invalid arg for -r option."
            help_info
            exit 1
            ;;
    esac
}

check_valid_bsp () {
    bsp="$1"
    for b in ${SUPPORTED_BSP}; do
        if [ "${bsp}" == "${b}" ]; then
            BSP_VALID="${bsp}"
            break
        fi
    done
    if [ -z "${BSP_VALID}" ]; then
        echo_error "${bsp} is not a supported BSP, the supported BSPs are: ${SUPPORTED_BSP}"
        exit 1
    fi
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
        echo_cmd "Cloning the source of repo '${REPO_NAME}':"
        ${RUN_CMD}

        if [ -n "${REPO_COMMIT}" ]; then
            cd ${REPO_NAME}
            RUN_CMD="git checkout -b ${REPO_BRANCH}-${REPO_COMMIT} ${REPO_COMMIT}"
            echo_cmd "Checkout the repo ${REPO_NAME} to specific commit: ${REPO_COMMIT}"
            ${RUN_CMD}
            cd -
        fi
    fi
}

source_env () {
    build_dir=$1
    cd ${SRC_LAYER_DIR}/poky
    set ${build_dir}
    source ./oe-init-build-env ${build_dir}
}

#########################################################################
# Parse cmd options
#########################################################################

DRYRUN=""
EXTRA_CONF=""
SKIP_UPDATE="Yes"
RM_WORK="Yes"
GET_SSTATE="No"
BSP="intel-corei7-64"

while getopts "w:b:e:r:unsh" OPTION; do
    case ${OPTION} in
        w)
            WORKSPACE=`readlink -f ${OPTARG}`
            ;;
        b)
            check_valid_bsp ${OPTARG}
            ;;
        e)
            EXTRA_CONF=`readlink -f ${OPTARG}`
            ;;
        n)
            DRYRUN="-n"
            ;;
        u)
            SKIP_UPDATE="No"
            ;;
        r)
            check_yn_rm_work ${OPTARG}
            ;;
        s)
            GET_SSTATE="Yes"
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

if [ -n "${BSP_VALID}" ]; then
    BSP="${BSP_VALID}"
fi

IMG_ARCH=${BSP}
if [ "${BSP}" == "intel-corei7-64"  ]; then
    IMG_ARCH="x86-64"
fi

#########################################################################
# Functions for each step
#########################################################################
SRC_LAYER_DIR=${WORKSPACE}/src_layers
SRC_ORAN_DIR=${SRC_LAYER_DIR}/oran
SRC_STX_DIR=${SRC_LAYER_DIR}/meta-starlingx
PRJ_BUILD_DIR=${WORKSPACE}/prj_oran_stx
PRJ_BUILD_DIR_ANACONDA=${WORKSPACE}/prj_oran_inf_anaconda
PRJ_SHARED_DIR=${WORKSPACE}/prj_shared
PRJ_OUTPUT_DIR=${WORKSPACE}/prj_output
PRJ_SHARED_DL_DIR=${WORKSPACE}/prj_shared/downloads
PRJ_SHARED_SS_DIR=${WORKSPACE}/prj_shared/sstate-cache
SRC_SCRIPTS=${SRC_ORAN_DIR}/rtp/scripts/build_inf_yocto
SRC_META_PATCHES=${SRC_SCRIPTS}/meta-patches/src_stx
SRC_CONFIGS=${SRC_SCRIPTS}/configs
IMG_STX=stx-image-aio
IMG_ANACONDA=stx-image-aio-installer
IMG_INF=inf-image-aio-installer
ISO_STX=${PRJ_BUILD_DIR}/tmp/deploy/images/${BSP}/${IMG_STX}-${BSP}.iso
ISO_ANACONDA=${PRJ_BUILD_DIR_ANACONDA}/tmp-glibc/deploy/images/${BSP}/${IMG_ANACONDA}-${BSP}.iso
ISO_INF=${PRJ_BUILD_DIR_ANACONDA}/tmp-glibc/deploy/images/${BSP}/${IMG_INF}-${BSP}.iso
ISO_INF_ALIAS=${PRJ_OUTPUT_DIR}/inf-image-yocto-aio-${IMG_ARCH}.iso

SSTATE_CONTAINER_IMG=infbuilder/inf-yocto-sstate:2022.05

prepare_workspace () {
    msg_step="Create workspace for the build"
    echo_step_start

    mkdir -p ${PRJ_BUILD_DIR} ${SRC_ORAN_DIR} ${PRJ_BUILD_DIR_ANACONDA} \
             ${PRJ_SHARED_DL_DIR} ${PRJ_SHARED_SS_DIR} ${PRJ_OUTPUT_DIR}

    echo_info "The following directories are created in your workspace(${WORKSPACE}):"
    echo_info "For all layers source: ${SRC_LAYER_DIR}"
    echo_info "For StarlingX build project: ${PRJ_BUILD_DIR}"
    echo_info "For anaconda (installer) build project: ${PRJ_BUILD_DIR_ANACONDA}"

    echo_step_end
}

# This is tend to be used for CI Jenkins build only
get_sstate () {
    msg_step="Get sstate cache from dockerhub image"
    echo_step_start

    for i in {1..5}; do
        docker pull ${SSTATE_CONTAINER_IMG}-${i}
        docker create -ti --name inf-yocto-sstate-${i} ${SSTATE_CONTAINER_IMG}-${i} sh
        docker cp inf-yocto-sstate-${i}:/sstate${i} ${PRJ_SHARED_SS_DIR}/sstate${i}
        docker rm inf-yocto-sstate-${i}
    done
    mv ${PRJ_SHARED_SS_DIR}/sstate*/* ${PRJ_SHARED_SS_DIR}
    #rm -rf ${PRJ_SHARED_SS_DIR}/sstate*

    echo_step_end
}

prepare_src () {
    msg_step="Get the source code repos"
    echo_step_start

    # Clone the oran layer if it's not already cloned
    # Check if the script is inside the repo
    if cd ${SCRIPTS_DIR} && git rev-parse --is-inside-work-tree > /dev/null 2>&1; then
        CLONED_ORAN_REPO=`dirname ${SCRIPTS_DIR}`
        echo_info "Use the cloned oran repo: ${CLONED_ORAN_REPO}"
        mkdir -p ${SRC_ORAN_DIR}/rtp
        cd ${SRC_ORAN_DIR}/rtp
        rm -rf meta-stx-oran scripts
        ln -sf ${CLONED_ORAN_REPO}/scripts scripts
        ln -sf ${CLONED_ORAN_REPO}/meta-stx-oran meta-stx-oran
    else
        echo_info "Cloning oran layer:"
        cd ${SRC_ORAN_DIR}
        clone_update_repo ${SRC_ORAN_BRANCH} ${SRC_ORAN_URL} rtp
    fi

    echo_info "Cloning or update Yocto layers:"

    cd ${SRC_LAYER_DIR}
    for layer_url in ${SRC_YP_URL}; do
        layer_name=$(basename ${layer_url%%;commit*})
        layer_commit=$(basename ${layer_url##*;commit=})
        clone_update_repo ${SRC_YP_BRANCH} ${layer_url%%;commit*} ${layer_name} ${layer_commit}
    done

    echo_info "Cloning or update meta-starlingx layers:"
    clone_update_repo ${SRC_STX_BRANCH} ${SRC_STX_URL} $(basename ${SRC_STX_URL})

    echo_step_end

# Not andy meta-patch is needed for the time being, but new ones may be needed and added
# sometime in the future, so just leave these code commented out here.
#    # Apply meta patches
#    for l in $(ls -1 ${SRC_META_PATCHES}); do
#        msg_step="Apply meta patches for ${l}"
#        echo_step_start
#        cd ${SRC_LAYER_DIR}/${l}
#
#        # backup current branch
#        local_branch=$(git rev-parse --abbrev-ref HEAD)
#        git branch -m "${local_branch}_${TIMESTAMP}"
#        git checkout -b ${local_branch} ${local_branch##*-}
#
#        for p in $(ls -1 ${SRC_META_PATCHES}/${l}); do
#            echo_info "Apllying patch: ${SRC_META_PATCHES}/${l}/${p}"
#            git am ${SRC_META_PATCHES}/${l}/${p}
#        done
#        echo_step_end
#    done
}

add_layer_stx_build () {
    msg_step="Add required layers to the StarlingX build project"
    echo_step_start

    source_env ${PRJ_BUILD_DIR}
    SRC_LAYERS=""
    for layer_url in ${SRC_YP_URL} ${SRC_STX_URL}; do
        layer_name=$(basename ${layer_url%%;commit*})
        case ${layer_name} in
        poky)
            continue
            ;;
        meta-openembedded)
            for sub_layer in ${SUB_LAYER_META_OE}; do
                SRC_LAYERS="${SRC_LAYERS} ${SRC_LAYER_DIR}/${layer_name}/${sub_layer}"
            done
            ;;
        meta-cloud-services)
            SRC_LAYERS="${SRC_LAYERS} ${SRC_LAYER_DIR}/${layer_name}"
            for sub_layer in ${SUB_LAYER_META_CLOUD_SERVICES}; do
                SRC_LAYERS="${SRC_LAYERS} ${SRC_LAYER_DIR}/${layer_name}/${sub_layer}"
            done
            ;;
        meta-starlingx)
            for sub_layer in ${SUB_LAYER_STX}; do
                SRC_LAYERS="${SRC_LAYERS} ${SRC_STX_DIR}/${sub_layer}"
            done
            ;;
        *)
            SRC_LAYERS="${SRC_LAYERS} ${SRC_LAYER_DIR}/${layer_name}"
            ;;

        esac
    done
    SRC_LAYERS="${SRC_LAYERS} ${SRC_ORAN_DIR}/rtp/meta-stx-oran"


    for src_layer in ${SRC_LAYERS}; do
        RUN_CMD="bitbake-layers add-layer ${src_layer}"
        echo_cmd "Add the ${src_layer} layer into the build project"
        ${RUN_CMD}
    done

    echo_step_end
}

add_configs_stx_build () {
    msg_step="Add extra configs into local.conf for StarlingX build"
    echo_step_start

    cd ${PRJ_BUILD_DIR}
    echo_info "Adding the following extra configs into local.conf"
    cat ${SRC_CONFIGS}/local-stx.conf | \
        sed -e "s/@BSP@/${BSP}/" | tee -a conf/local.conf
    cat ${SRC_CONFIGS}/local-mirrors.conf | tee -a conf/local.conf

    echo "DL_DIR = '${PRJ_SHARED_DL_DIR}'" | tee -a conf/local.conf
    echo "SSTATE_DIR = '${PRJ_SHARED_SS_DIR}'" | tee -a conf/local.conf

    if [ "${RM_WORK}" == "Yes" ]; then
        echo "INHERIT += 'rm_work'" | tee -a conf/local.conf
        echo "RM_WORK_EXCLUDE += '${IMG_STX}'" | tee -a conf/local.conf
    fi


    if [ "${EXTRA_CONF}" != "" ] && [ -f "${EXTRA_CONF}" ]; then
        cat ${EXTRA_CONF} | tee -a conf/local.conf
    fi
    echo_step_end
}

setup_stx_build () {
    echo_step_start "Setup StarlingX build project"

    add_layer_stx_build

    cd ${PRJ_BUILD_DIR}
    if ! grep -q 'Configs for StarlingX' conf/local.conf; then
        add_configs_stx_build
    else
        echo_info "Nothing is added into local.conf"
    fi

    echo_step_end "Setup StarlingX build project"
}

build_stx_image () {
    msg_step="Build StarlingX images"
    echo_step_start

    source_env ${PRJ_BUILD_DIR}

    mkdir -p logs

    RUN_CMD="bitbake ${DRYRUN} ${IMG_STX}"
    echo_cmd "Build the ${IMG_STX} image"
    bitbake ${DRYRUN} ${IMG_STX} 2>&1|tee logs/bitbake_${IMG_STX}_${TIMESTAMP}.log

    echo_step_end

    echo_info "Build succeeded, you can get the image in ${ISO_STX}"
}

add_layer_anaconda_build () {
    msg_step="Add required layers to the anaconda (installer) build project"
    echo_step_start

    source_env ${PRJ_BUILD_DIR_ANACONDA}
    SRC_LAYERS=""
    for sub_layer in ${SUB_LAYER_META_OE_ANACONDA}; do
        SRC_LAYERS="${SRC_LAYERS} ${SRC_LAYER_DIR}/meta-openembedded/${sub_layer}"
    done
    SRC_LAYERS="${SRC_LAYERS} ${SRC_LAYER_DIR}/meta-intel"
    SRC_LAYERS="${SRC_LAYERS} ${SRC_LAYER_DIR}/meta-anaconda"
    SRC_LAYERS="${SRC_LAYERS} ${SRC_STX_DIR}/meta-stx-distro"
    SRC_LAYERS="${SRC_LAYERS} ${SRC_STX_DIR}/meta-stx-integ"
    SRC_LAYERS="${SRC_LAYERS} ${SRC_ORAN_DIR}/rtp/meta-stx-oran"

    for src_layer in ${SRC_LAYERS}; do
        RUN_CMD="bitbake-layers add-layer ${src_layer}"
        echo_cmd "Add the ${src_layer} layer into the build project"
        ${RUN_CMD}
    done

    echo_step_end
}

add_configs_anaconda_build () {
    msg_step="Add extra configs into local.conf for anaconda (installer) build"
    echo_step_start

    cd ${PRJ_BUILD_DIR_ANACONDA}
    echo_info "Adding the following extra configs into local.conf"
    cat ${SRC_CONFIGS}/local-anaconda.conf | \
        sed -e "s/@BSP@/${BSP}/" \
            -e "s|@TARGET_BUILD@|${PRJ_BUILD_DIR}|" \
            | tee -a conf/local.conf
    cat ${SRC_CONFIGS}/local-mirrors.conf | tee -a conf/local.conf

    echo "DL_DIR = '${PRJ_SHARED_DL_DIR}'" | tee -a conf/local.conf
    echo "SSTATE_DIR = '${PRJ_SHARED_SS_DIR}'" | tee -a conf/local.conf

    if [ "${RM_WORK}" == "Yes" ]; then
        echo "INHERIT += 'rm_work'" | tee -a conf/local.conf
        echo "RM_WORK_EXCLUDE += '${IMG_ANACONDA}'" | tee -a conf/local.conf
    fi

    if [ "${EXTRA_CONF}" != "" ] && [ -f "${EXTRA_CONF}" ]; then
        cat ${EXTRA_CONF} | tee -a conf/local.conf
    fi

    echo_step_end
}

setup_anaconda_build () {
    echo_step_start "Setup anaconda (installer) build project"

    add_layer_anaconda_build

    cd ${PRJ_BUILD_DIR_ANACONDA}
    if ! grep -q 'Configs for anaconda' conf/local.conf; then
        add_configs_anaconda_build
    else
        echo_info "Nothing is added into local.conf"
    fi

    echo_step_end "Setup anaconda build project"
}

build_anaconda_image () {
    echo_step_start "Build anaconda (installer) images"
    source_env ${PRJ_BUILD_DIR_ANACONDA}

    mkdir -p logs

    if [ -f ${ISO_ANACONDA} ]; then
        bitbake ${DRYRUN} -c clean ${IMG_ANACONDA}
    fi
    RUN_CMD="bitbake ${DRYRUN} ${IMG_ANACONDA}"
    echo_cmd "Build the ${IMG_ANACONDA} image"
    bitbake ${DRYRUN} ${IMG_ANACONDA} 2>&1|tee logs/bitbake_${IMG_ANACONDA}_${TIMESTAMP}.log

    if [ -z "${DRYRUN}" ]; then
        cp -Pf ${ISO_ANACONDA} ${ISO_INF}
        cp -f ${ISO_ANACONDA} ${ISO_INF_ALIAS}
    fi

    echo_step_end

    echo_info "Build succeeded, you can get the image in ${ISO_INF_ALIAS}"
}

#########################################################################
# Main process
#########################################################################

prepare_workspace
if [ "${GET_SSTATE}" == "Yes" ]; then
    get_sstate
fi
prepare_src
setup_stx_build
setup_anaconda_build
build_stx_image
build_anaconda_image
