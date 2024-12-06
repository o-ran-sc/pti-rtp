#!/bin/bash
#
# Copyright (C) 2023 Wind River Systems, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Ensure we fail the job if any steps fail.
set -e -o pipefail

#########################################################################
# Variables
#########################################################################
SCRIPTS_NAME=$(basename $0)
SCRIPTS_DIR=$(dirname $(readlink -f $0))
WORKSPACE="${SCRIPTS_DIR}"

LOCAL_BIN="/usr/local/bin"
USE_SUDO="sudo"

STX_ARCH_SUPPORTED="\
    x86-64 \
    arm64 \
"
STX_ARCH="x86-64"
STX_ARCH_NAME="amd64"

#########################################################################
# Common Functions
#########################################################################

help_info () {
cat << ENDHELP
Usage:
${SCRIPTS_NAME} [-w WORKSPACE_DIR] [-a ARCH] [-l LOCAL_BIN] [-h]
where:
    -w WORKSPACE_DIR is the path for the builds
    -a STX_ARCH is the build arch, default is x86-64, only supports: 'x86-64' and 'arm64'
    -l LOCAL_BIN is the path for local bin, default is /usr/local/bin
    -h this help info
examples:
$0
$0 -w workspace_1234
ENDHELP
}

echo_info () {
    echo "INFO: $1"
}

check_valid_arch () {
    arch="$1"
    for a in ${STX_ARCH_SUPPORTED}; do
        if [ "${arch}" = "${a}" ]; then
            ARCH_VALID="${arch}"
            break
        fi
    done
    if [ -z "${ARCH_VALID}" ]; then
        echo_error "${arch} is not a supported ARCH, the supported ARCHs are: ${STX_ARCH_SUPPORTED}"
        exit 1
    else
        STX_ARCH=${ARCH_VALID}
    fi
}

while getopts "w:a:l:h" OPTION; do
    case ${OPTION} in
        w)
            WORKSPACE=`readlink -f ${OPTARG}`
            ;;
        l)
            LOCAL_BIN=`readlink -f ${OPTARG}`
	    ;;
        a)
            check_valid_arch ${OPTARG}
            ;;
        h)
            help_info
            exit
            ;;
    esac
done

if [ -d ${LOCAL_BIN} ]; then
    touch ${LOCAL_BIN}/test && USE_SUDO="" && rm ${LOCAL_BIN}/test
else
    echo "ERROR: ${LOCAL_BIN} doesn't exists!!"
    exit
fi

if [ ${STX_ARCH} = "arm64" ]; then
    STX_ARCH_NAME="arm64"
fi
DL_MINIKUBE_URL="https://storage.googleapis.com/minikube/releases/latest"
DL_MINIKUBE="minikube-linux-${STX_ARCH_NAME}"
DL_HELM_URL="https://get.helm.sh"
DL_HELM="helm-v3.6.2-linux-${STX_ARCH_NAME}.tar.gz"
DL_REPO_URL="https://storage.googleapis.com/git-repo-downloads/repo"

#########################################################################
# Main process
#########################################################################
echo_info "Install minikube"
mkdir -p ${WORKSPACE}/dl-tools
cd ${WORKSPACE}/dl-tools

if [ ! -f ${LOCAL_BIN}/minikube ]; then
    curl -LO ${DL_MINIKUBE_URL}/${DL_MINIKUBE}
    ${USE_SUDO} install ${DL_MINIKUBE} ${LOCAL_BIN}/minikube
fi
minikube version

echo_info "Install helm"
if [ ! -f ${LOCAL_BIN}/helm ]; then
    curl -LO ${DL_HELM_URL}/${DL_HELM}
    tar xvf ${DL_HELM}
    ${USE_SUDO} mv linux-${STX_ARCH_NAME}/helm ${LOCAL_BIN}/
fi

echo_info "Install repo tool"
if [ ! -f ${LOCAL_BIN}/repo ]; then
    ${USE_SUDO} wget ${DL_REPO_URL} -O ${LOCAL_BIN}/repo
    ${USE_SUDO} chmod a+x ${LOCAL_BIN}/repo
fi
