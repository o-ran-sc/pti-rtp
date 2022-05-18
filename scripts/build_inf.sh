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

SCRIPTS_DIR=$(dirname $(readlink -f $0))
TIMESTAMP=`date +"%Y%m%d_%H%M%S"`

#########################################################################
# Common Functions
#########################################################################

help_info () {
cat << ENDHELP
Note:
This is a wrapper script to build both Yocto based and CentOS based images
with default options, and tend to be used by ORAN CI build only.
Usage:
$(basename $0) [-w WORKSPACE_DIR] [-n] [-h]
where:
    -w WORKSPACE_DIR is the path for the project
    -n dry-run only for bitbake
    -h this help info
examples:
$0
$0 -w workspace
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


#########################################################################
# Parse cmd options
#########################################################################

DRYRUN=""

while getopts "w:b:e:r:unh" OPTION; do
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
    echo_info "No workspace specified, a directory 'workspace' will be created in current directory as the workspace"
    WORKSPACE=`readlink -f workspace`
fi

#########################################################################
# Functions for each step
#########################################################################
WORKSPACE_YP=${WORKSPACE}/workspace_yocto
WORKSPACE_CENTOS=${WORKSPACE}/workspace_centos
SCRIPT_YP=${SCRIPTS_DIR}/build_inf_yocto/build_inf_yocto.sh
SCRIPT_CENTOS=${SCRIPTS_DIR}/build_inf_centos/build_inf_centos.sh

prepare_workspace () {
    msg_step="Create workspace for the multi-os builds"
    echo_step_start

    mkdir -p ${WORKSPACE_YP} ${WORKSPACE_CENTOS}

    echo_info "The following directories are created in your workspace(${WORKSPACE}):"
    echo_info "For Yocto buid: ${WORKSPACE_YP}"
    echo_info "For CentOS buid: ${WORKSPACE_CENTOS}"

    echo_step_end
}

# debug for CI Jenkins build
get_debug_info () {
    msg_step="Get debug info for CI Jenkins build"
    echo_step_start

    echo_info "=== Get env ==="
    env
    echo_info "==============="

    set -x
    df -h
    groups
    uname -a
    cat /etc/*release
    lscpu
    free -h
    rpm -qa|grep mock
    mock --debug-config
    docker version
    set +x

    echo_step_end
}


#########################################################################
# Main process
#########################################################################

prepare_workspace
if [ "$CI" = "true" ]; then
    get_debug_info
fi

# dry-run is not supported yet for CentOS build
if [ -z "${DRYRUN}" ]; then
    ${SCRIPT_CENTOS} -w ${WORKSPACE_CENTOS} ${DRYRUN}
fi

${SCRIPT_YP} -w ${WORKSPACE_YP} ${DRYRUN}
