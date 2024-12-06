#!/bin/bash
#
# Copyright (C) 2023 Wind River Systems, Inc.
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

SRC_SCRIPTS_BRANCH="master"

SRC_SCRIPTS_URL="https://github.com/jackiehjm/stx-builds.git"

SCRIPTS_DIR=$(dirname $(readlink -f $0))
SCRIPTS_NAME=$(basename $0)
TIMESTAMP=`date +"%Y%m%d_%H%M%S"`

STX_PARALLEL="2"

STX_SRC_BRANCH_SUPPORTED="\
    master \
    r/stx.8.0 \
    r/stx.9.0 \
    TC_DEV_0008 \
    WRCP_22.12 \
    WRCP_22.12_PATCHING \
    WRCP_22.12_PRESTAGING \
    WRCP_22.12_V2_PATCHING \
    WRCP_22.12_MR2_PATCHING \
    WRCP_22.12_MR2PLUS_PATCHING \
    WRCP_22.12_MR2PLUS_PRESTAGING \
"
STX_SRC_BRANCH="master"
STX_MANIFEST_URL="https://opendev.org/starlingx/manifest"
STX_MANIFEST_URL_WRCP="ssh://git@vxgit.wrs.com:7999/cgcs/github.com.stx-staging.stx-manifest.git"

STX_ARCH_SUPPORTED="\
    x86-64 \
    arm64 \
"
STX_ARCH="x86-64"

# Source code fixes for ARM64
SRC_FIX_URL="https://github.com/jackiehjm"
SRC_FIX_BRANCH="arm64/20240305-stx90-native"
SRC_FIX_REPOS="\
    cgcs-root \
    stx-tools \
    stx/stx-puppet \
    stx/integ \
    stx/utilities \
    stx/fault \
    stx/containers \
    stx/kernel \
    stx/metal \
    stx/ansible-playbooks \
    stx/config \
    stx/nginx-ingress-controller-armada-app \
    stx/app-istio \
    stx/virt \
"

SDK_URL="http://ala-lpggp5:5088/3_open_source/stx/images-arm64/lat-sdk/lat-sdk-build_20230525/AppSDK.sh"

PEK_REPO_URL="https://mirrors.tuna.tsinghua.edu.cn/git/git-repo"
SETUP_ONLY=false
REBUILD_IMG=false

#########################################################################
# Common Functions
#########################################################################

help_info () {
cat << ENDHELP
Usage: ${SCRIPTS_NAME} [options]

Options:
  -w, --workspace WORKSPACE_DIR    Set the path of workspace for the project.
  -p, --parallel  PARALLEL_BUILD   Set the num of paralle build, default is 2.
  -a, --arch      STX_ARCH         Set the build arch, default is x86-64,
                                   only supports: 'x86-64' and 'arm64'.
  -b, --branch    STX_SRC_BRANCH   Set the branch for stx repos, default is master.
  -r, --rebuild                    Rebuild all the builder images if set.
  -s, --setup-only                 Only setup the build env, don't actually build.
  -h, --help                       Display this help message.

Examples:
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

check_valid_branch () {
    branch="$1"
    for b in ${STX_SRC_BRANCH_SUPPORTED}; do
        if [ "${branch}" == "${b}" ]; then
            BRANCH_VALID="${branch}"
            break
        fi
    done
    if [ -z "${BRANCH_VALID}" ]; then
        echo_error "'${branch}' is not a supported BRANCH, the supported BRANCHs are:"
        for b in ${STX_SRC_BRANCH_SUPPORTED}; do
            echo " - ${b}"
        done
        exit 1
    else
        STX_SRC_BRANCH=${BRANCH_VALID}
    fi
}

check_valid_arch () {
    arch="$1"
    for a in ${STX_ARCH_SUPPORTED}; do
        if [ "${arch}" == "${a}" ]; then
            ARCH_VALID="${arch}"
            break
        fi
    done
    if [ -z "${ARCH_VALID}" ]; then
        echo_error "'${arch}' is not a supported ARCH, the supported ARCHs are:"
        for a in ${STX_ARCH_SUPPORTED}; do
            echo " - ${a}"
        done
        exit 1
    else
        STX_ARCH=${ARCH_VALID}
    fi
}


#########################################################################
# Parse cmd options
#########################################################################

# Use getopt to handle options
OPTIONS=$(getopt -o w:p:a:b:rsh --long workspace:,parallel:,arch:,branch:,rebuild,setup-only,help -- "$@")
if [ $? -ne 0 ]; then
    help_info 
    exit 1
fi

# Parse options
eval set -- "$OPTIONS"

while true; do
    case "$1" in
        -w | --workspace)
            WORKSPACE=$(readlink -f "$2")
            shift 2
            ;;
        -p | --parallel)
            STX_PARALLEL="$2"
            shift 2
            ;;
        -a | --arch)
            check_valid_arch "$2"
            shift 2
            ;;
        -b | --branch)
            check_valid_branch "$2"
            shift 2
            ;;
        -r | --rebuild)
            REBUILD_IMG=true
            shift
	    ;;
        -s | --setup-only)
            SETUP_ONLY=true
            shift
	    ;;
        -h | --help)
            help_info
            exit 0
            ;;
        --)
            shift
            break
            ;;
        *)
            usage
            exit 1
            ;;
    esac
done

if [ -z ${WORKSPACE} ]; then
    echo_info "No workspace specified, a directory 'workspace' will be created in current directory as the workspace"
    WORKSPACE=`readlink -f workspace`
fi

if [[ ${STX_SRC_BRANCH} =~ "WRCP" || ${STX_SRC_BRANCH} =~ "TC_DEV" ]]; then
    STX_MANIFEST_URL=${STX_MANIFEST_URL_WRCP}
fi

echo "workspace: ${WORKSPACE}"
echo "branch: ${STX_SRC_BRANCH}"
echo "arch: ${STX_ARCH}"
echo "parallel: ${STX_PARALLEL}"
#exit 0

#########################################################################
# Functions for each step
#########################################################################

# "_" can't be used in project name
PRJ_NAME=prj-stx-deb

STX_LOCAL_DIR=${WORKSPACE}/localdisk
STX_LOCAL_SRC_DIR=${STX_LOCAL_DIR}/designer/${USER}/${PRJ_NAME}
STX_LOCAL_PRJ_DIR=${STX_LOCAL_DIR}/loadbuild/${USER}/${PRJ_NAME}
STX_SRC_DIR=${WORKSPACE}/src
STX_PRJ_DIR=${WORKSPACE}/${PRJ_NAME}
STX_PRJ_OUTPUT=${WORKSPACE}/prj_output
STX_MIRROR_DIR=${WORKSPACE}/mirrors
STX_APTLY_DIR=${WORKSPACE}/aptly
STX_MINIKUBE_HOME=${WORKSPACE}/minikube_home

SRC_SCRIPTS_DIR=${STX_SRC_DIR}/stx-builds
SRC_META_PATCHES=${SRC_SCRIPTS_DIR}/build_stx_debian/meta-patches

ISO_STX_DEB_X86_DEPLOY=starlingx-intel-x86-64-cd.iso
ISO_STX_DEB_X86_OUTPUT=stx-image-debian-all-x86-64.iso
ISO_STX_DEB_ARM_DEPLOY=starlingx-qemuarm64-cd.iso
ISO_STX_DEB_ARM_OUTPUT=stx-image-debian-all-arm64.iso

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

    GIT_USER=`git config user.name`
    GIT_EMAIL=`git config user.email`

    if [ -z "${GIT_USER}" ]; then
        GIT_USER=${USER}
        git config --global user.name "${GIT_USER}"
    fi
    if [ -z "${GIT_EMAIL}" ]; then
        GIT_EMAIL=${USER}@windriver.com
        git config --global user.email "${GIT_EMAIL}"
    fi

    ENV_FILENAME=env.${PRJ_NAME}

    cat <<EOF > ${WORKSPACE}/${ENV_FILENAME}

export STX_BUILD_HOME=${WORKSPACE}
export PROJECT=${PRJ_NAME}
export STX_MIRROR_DIR=${STX_MIRROR_DIR}
export STX_REPO_ROOT=${STX_SRC_DIR}
#export STX_REPO_ROOT_SUBDIR="localdisk/designer/${USER}/${PRJ_NAME}"

export USER_NAME="${USER}"
export USER_EMAIL="${GIT_EMAIL}"

# MINIKUBE
export STX_PLATFORM="minikube"
export STX_MINIKUBENAME="minikube-${USER}"
export MINIKUBE_HOME=${STX_MINIKUBE_HOME}

# Manifest/Repo Options:
export STX_MANIFEST_URL="${STX_MANIFEST_URL}"
export STX_MANIFEST_BRANCH="${STX_SRC_BRANCH}"
export STX_MANIFEST="default.xml"

EOF


    if [[ ${HOST} =~ "pek-" ]]; then
        cat << EOF >> ${WORKSPACE}/${ENV_FILENAME}

export http_proxy=http://147.11.252.42:9090
export https_proxy=http://147.11.252.42:9090
export no_proxy=localhost,127.0.0.1,10.96.0.0/12,192.168.0.0/16

EOF
    fi

    if [ ${STX_ARCH} = "arm64" ]; then
    	cat << EOF >> ${WORKSPACE}/${ENV_FILENAME}

export STX_PREBUILT_BUILDER_IMAGE_PREFIX=stx4arm/
export STX_PREBUILT_BUILDER_IMAGE_TAG=master-20240229

EOF

    else
    	echo "export STX_PREBUILT_BUILDER_IMAGE_TAG=master-debian-latest" >> ${WORKSPACE}/${ENV_FILENAME}
    fi

    echo_info "Env file created at ${WORKSPACE}/$ENV_FILENAME"
    cat ${WORKSPACE}/$ENV_FILENAME

    source ${WORKSPACE}/${ENV_FILENAME}

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
        if [[ ${HOST} =~ "pek-" ]]; then
            export REPO_URL=${PEK_REPO_URL}
        fi

        RUN_CMD="repo init -u ${STX_MANIFEST_URL} -b ${STX_SRC_BRANCH} -m ${STX_MANIFEST}"
        run_cmd "Init the repo from manifest"

        RUN_CMD="repo sync --force-sync"
        run_cmd "repo sync"

        touch .repo-init-done
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

    # Clone the stx-builds repo if it's not already cloned
    # Check if the script is inside the repo
    if cd ${SCRIPTS_DIR} && git rev-parse --is-inside-work-tree > /dev/null 2>&1; then
        CLONED_SCRIPTS_REPO=`git rev-parse --show-toplevel`
        echo_info "Use the cloned stx-builds repo: ${CLONED_SCRIPTS_REPO}"
        cd ${STX_SRC_DIR}
        ln -sf ${CLONED_SCRIPTS_REPO}
    else
        echo_info "Cloning stx-builds repo:"
        cd ${STX_SRC_DIR}
        clone_update_repo ${SRC_SCRIPTS_BRANCH} ${SRC_SCRIPTS_URL}
    fi

    repo_init_sync
    patch_src

    echo_step_end
}

patch_src_arm () {
    for repo in ${SRC_FIX_REPOS}; do
        if [ $repo = "cgcs-root" ]; then
            fix_repo="stx-cgcs-root"
        elif [ $repo = "stx/stx-puppet" ]; then
	    fix_repo="stx-puppet"
        else
            fix_repo="${repo/\//-}"
        fi
        if [ -d ${STX_REPO_ROOT}/${repo} ]; then
            echo_info "Patching for the ${repo}"
            cd ${STX_REPO_ROOT}/${repo}
        elif [ -d ${STX_REPO_ROOT}/cgcs-root/${repo} ]; then
            echo_info "Patching for the cgcs-root/${repo}"
            cd ${STX_REPO_ROOT}/cgcs-root/${repo}
        else
            echo_error "The repo ${repo} is not found!!"
        fi

        git remote add hjm-github ${SRC_FIX_URL}/${fix_repo}
        git fetch hjm-github
        git checkout -b ${SRC_FIX_BRANCH} hjm-github/${SRC_FIX_BRANCH} || true 
    done

    PKG_BUILDER="${STX_REPO_ROOT}/stx-tools/stx/toCOPY/pkgbuilder/debbuilder.conf"
    sed -i '/@STX_MIRROR_URL@/ d' ${PKG_BUILDER}
}

patch_src () {
    echo_step_start "Patching source codes for stx project"

    if [ ${STX_ARCH} = "arm64" ]; then
        patch_src_arm
    fi

    if [[ ${HOST} =~ "pek-" ]]; then
    	sed -i '/^FROM/a \
    	    \n# Proxy configuration\nENV https_proxy "http://147.11.252.42:9090"\n' \
    	    ${STX_REPO_ROOT}/stx-tools/stx/dockerfiles/stx*Dockerfile
    fi

    STX_BUILDER="${STX_REPO_ROOT}/stx-tools/stx/lib/stx/stx_build.py"
    echo_info "Patching for the ${STX_BUILDER}"
    grep -q "\-\-parallel" ${STX_BUILDER} \
        || sed -i "s/\(build-pkgs -a \)/\1 --parallel ${STX_PARALLEL} /" \
        ${STX_BUILDER}

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

    if [ ${STX_ARCH} = "arm64" ]; then
        stx config --add project.debian_snapshot_base http://snapshot.debian.org/archive/debian
        stx config --add project.debian_security_snapshot_base http://snapshot.debian.org/archive/debian-security

	# options: cengn_first(default), cengn, upstream_first, upstream
	# For now, there is no any packages for ARM on cengn, so only opiton
	# can be used is "upstream"
        stx config --add repomgr.cengnstrategy upstream
    fi

    stx config --show

    echo_step_end
}

build_image () {
    echo_step_start "Build Debian images"

    cd ${STX_REPO_ROOT}/stx-tools
    RUN_CMD="./stx-init-env -D"
    run_cmd "Run 'stx-init-env -D' to delete existing minikube profile."

    if [ "$REBUILD_IMG" = true ]; then
        RUN_CMD="./stx-init-env --rebuild"
    else
        RUN_CMD="./stx-init-env"
    fi
    run_cmd "Run stx-init-env script to initialize build environment & (re-)start builder pods."

    stx control status

    # wait for all the pods running
    sleep 600
    stx control status

    RUN_CMD="stx build prepare"
    run_cmd "Build prepare"

    if [ "$SETUP_ONLY" = true ]; then
        echo "The build env is setup successfully."
        exit 0
    fi

    RUN_CMD="stx build download"
    run_cmd "Download packges"

    RUN_CMD="stx repomgr list"
    run_cmd "repomgr list"

    RUN_CMD="stx build world"
    run_cmd "Build-pkgs"

    RUN_CMD="stx build image"
    run_cmd "Build ISO image"

    if [ ${STX_ARCH} = "arm64" ]; then
        cp -f ${STX_LOCAL_DIR}/deploy/${ISO_STX_DEB_ARM_DEPLOY} ${STX_PRJ_OUTPUT}/${ISO_STX_DEB_ARM_OUTPUT}
    else
        cp -f ${STX_LOCAL_DIR}/deploy/${ISO_STX_DEB_X86_DEPLOY} ${STX_PRJ_OUTPUT}/${ISO_STX_DEB_X86_OUTPUT}
    fi

    echo_step_end

    echo_info "Build succeeded, you can get the image in ${STX_PRJ_OUTPUT}/${ISO_STX_DEB}"
}

#########################################################################
# Main process
#########################################################################

prepare_workspace
create_env
prepare_src
init_stx_tool
build_image
