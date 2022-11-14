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
SCRIPTS_NAME=$(basename $0)
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
${SCRIPTS_NAME} [-w WORKSPACE_DIR] [-n] [-h]
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
YP_ARGS="-s"

while getopts "w:b:e:r:unh" OPTION; do
    case ${OPTION} in
        w)
            WORKSPACE=`readlink -f ${OPTARG}`
            ;;
        n)
            DRYRUN="-n"
            YP_ARGS=""
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
WORKSPACE_DEB=${WORKSPACE}/workspace_debian
SCRIPT_YP=${SCRIPTS_DIR}/build_inf_yocto/build_inf_yocto.sh
SCRIPT_CENTOS=${SCRIPTS_DIR}/build_inf_centos/build_inf_centos.sh
SCRIPT_CENTOS_PRE=${SCRIPTS_DIR}/build_inf_centos/build_inf_prepare_jenkins.sh
SCRIPT_DEB=${SCRIPTS_DIR}/build_inf_debian/build_inf_debian.sh
SCRIPT_DEB_PRE=${SCRIPTS_DIR}/build_inf_debian/build_inf_prepare_jenkins.sh

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

build_yocto () {
    msg_step="Yocto builds"
    echo_step_start

    RUN_CMD="${SCRIPT_YP} -w ${WORKSPACE_YP} ${DRYRUN} ${YP_ARGS}"
    run_cmd "Start Yocto builds"

    echo_step_end
}

build_centos () {
    # dry-run is not supported yet for CentOS build
    if [ -z "${DRYRUN}" ]; then
        msg_step="CentOS builds"
        echo_step_start

        if [ "$CI" = "true" ]; then
            RUN_CMD="${SCRIPT_CENTOS_PRE} -w ${WORKSPACE_CENTOS}"
            run_cmd "Prepare for CentOS builds"
        fi
        RUN_CMD="${SCRIPT_CENTOS} -w ${WORKSPACE_CENTOS} ${DRYRUN}"
        run_cmd "Start CentOS builds"

        echo_step_end
    fi
}

build_debian () {
    if [ -z "${DRYRUN}" ]; then
        msg_step="Debian builds"
        echo_step_start

        if [ "$CI" = "true" ]; then
            RUN_CMD="${SCRIPT_DEB_PRE} -w ${WORKSPACE_DEB}"
            run_cmd "Prepare for Debian builds"
        fi

        RUN_CMD="${SCRIPT_DEB} -w ${WORKSPACE_DEB} ${DRYRUN}"
        run_cmd "Start Yocto builds"

        echo_step_end
    fi
}


#########################################################################
# Main process
#########################################################################

prepare_workspace
if [ "$CI" = "true" ]; then
    get_debug_info
fi

#build_yocto
#build_centos
build_debian

