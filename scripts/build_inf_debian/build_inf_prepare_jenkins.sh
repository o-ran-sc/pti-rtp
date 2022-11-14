#!/bin/sh
#
# Copyright (C) 2022 Wind River Systems, Inc.
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
WORKSPACE=""

SCRIPTS_NAME=$(basename $0)

#########################################################################
# Common Functions
#########################################################################

help_info () {
cat << ENDHELP
Usage:
${SCRIPTS_NAME} [-w WORKSPACE_DIR] [-h]
where:
    -w WORKSPACE_DIR is the path for the builds
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

while getopts "w:h" OPTION; do
    case ${OPTION} in
        w)
            WORKSPACE=`readlink -f ${OPTARG}`
            ;;
        h)
            help_info
            exit
            ;;
    esac
done

#########################################################################
# Main process
#########################################################################
msg_step="Prepare build directories"
echo_step_start

echo_info "Install minikube"
mkdir -p ${WORKSPACE}/dl-tools
cd ${WORKSPACE}/dl-tools
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube
minikube version

echo_info "Install helm"
curl -LO https://get.helm.sh/helm-v3.6.2-linux-amd64.tar.gz
tar xvf helm-v3.6.2-linux-amd64.tar.gz
sudo mv linux-amd64/helm /usr/local/bin/

echo_info "Install repo tool"
sudo wget https://storage.googleapis.com/git-repo-downloads/repo -O /usr/local/bin/repo
sudo chmod a+x /usr/local/bin/repo

echo_step_end
