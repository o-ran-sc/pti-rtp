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

help_info () {
cat << ENDHELP
This script is used to genereate package-index and output the path
of rpm packages and index files, it must be ran after build_oran.sh
Usage:
$(basename $0) [-w WORKSPACE_DIR] [-n] [-h]
where:
    -w WORKSPACE_DIR is the path for the project
    -n dry-run only for bitbake
    -h this help info
ENDHELP
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

DRYRUN=""
RECIPE_LIST=""
SCRIPTS_DIR=`dirname $0`
SCRIPTS_DIR=`readlink -f $SCRIPTS_DIR`

while getopts "w:nh" OPTION; do
    case ${OPTION} in
        w)
            WORKSPACE=`readlink -f ${OPTARG}`
            ;;
        n)
            DRYRUN="-n"
            ;;
        h)
            help_info
            exit
            ;;
    esac
done

if [ -z ${WORKSPACE} ]; then
    echo_info "No workspace specified, assume './workspace'"
    WORKSPACE=`readlink -f workspace`
fi

if [ ! -d ${WORKSPACE} ]; then
    echo_error "The workspace ${WORKSPACE} doesn't exist!!"
    echo_error "You need to run build_oran.sh to create a valid worksapce and build image first."
    echo_error "Then run this script with -w option to specify the correct path of the worksapce."
    help_info
    exit 1
fi

SRC_WRL_DIR=${WORKSPACE}/src_wrl1018
SRC_ORAN_DIR=${WORKSPACE}/src_oran
PRJ_BUILD_DIR=${WORKSPACE}/prj_oran-inf
RPM_REPO_LIST=${SRC_ORAN_DIR}/rtp/scripts/rpm_repo_list.txt
RPM_DEPLOY_DIR=${PRJ_BUILD_DIR}/tmp-glibc/deploy/rpm
RPM_REPODATA=${RPM_DEPLOY_DIR}/repodata

echo_info "For wrlinux1018 source: ${SRC_WRL_DIR}"
echo_info "For oran layer source: ${SRC_ORAN_DIR}"
echo_info "For build project: ${PRJ_BUILD_DIR}"

# Source the build env
cd ${SRC_WRL_DIR}
. ./environment-setup-x86_64-wrlinuxsdk-linux
set ${PRJ_BUILD_DIR}
. ./oe-init-build-env ${PRJ_BUILD_DIR}

mkdir -p logs
TIMESTAMP=`date +"%Y%m%d_%H%M%S"`

RECIPE_LIST=`grep -v '^#' ${RPM_REPO_LIST}|awk '{print $1}'|sort|uniq`
RPM_PKG_LIST=`grep -v '^#' ${RPM_REPO_LIST}|awk '{print $2}'|sort|uniq`

if [ -z "${RECIPE_LIST}" ]; then
    echo_info "The recipes list is empty, nothing to do!!"
    exit 0
fi

# Build the recipes
RUN_CMD="bitbake ${DRYRUN} ${RECIPE_LIST}"
echo_cmd "Build the recipes: '${RECIPE_LIST}'"
bitbake ${DRYRUN} ${RECIPE_LIST} 2>&1|tee logs/bitbake_recipes_${TIMESTAMP}.log

# Build the package-index
RUN_CMD="bitbake ${DRYRUN} package-index"
echo_cmd "Build the package-index'"
bitbake ${DRYRUN} package-index 2>&1|tee logs/bitbake_package-index_${TIMESTAMP}.log

echo_info "Build succeeded"

echo_info "Package index files"
find ${RPM_REPODATA}

echo_info "RPM files"
for i in ${RPM_PKG_LIST}; do
    readlink -f ${RPM_DEPLOY_DIR}/*/$i
done
