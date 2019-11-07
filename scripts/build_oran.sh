#!/bin/bash

# Ensure we fail the job if any steps fail.
set -e -o pipefail

help_info () {
cat << ENDHELP
Usage:
$(basename $0) <-w WORKSPACE_DIR> [-n] [-h]
where:
    -w WORKSPACE_DIR is the path for the project
    -n dry-run only for bitbake
    -h this help info
    -e EXTRA_CONF is the pat for extra config file
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

if [ $# -eq 0 ]; then
    echo "Missing options!"
    help_info
    exit
fi

DRYRUN=""
EXTRA_CONF=""

SCRIPTS_DIR=`dirname $0`
SCRIPTS_DIR=`readlink -f $SCRIPTS_DIR`

while getopts "w:e:nh" OPTION; do
    case ${OPTION} in
        w)
            WORKSPACE=`readlink -f ${OPTARG}`
            ;;
        e)
            EXTRA_CONF=`readlink -f ${OPTARG}`
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

SRC_WRL_DIR=${WORKSPACE}/src_wrl1018
SRC_ORAN_DIR=${WORKSPACE}/src_oran
PRJ_BUILD_DIR=${WORKSPACE}/prj_oran-inf

mkdir -p ${SRC_WRL_DIR} ${PRJ_BUILD_DIR} ${SRC_ORAN_DIR}

echo_info "The following directories are created in your workspace(${WORKSPACE}):"
echo_info "For wrlinux1018 source: ${SRC_WRL_DIR}"
echo_info "For oran layer source: ${SRC_ORAN_DIR}"
echo_info "For build project: ${PRJ_BUILD_DIR}"

# Clone the source of WRLinux BASE 10.18 from github and setup
RUN_CMD="git clone --branch WRLINUX_10_18_BASE git://github.com/WindRiver-Labs/wrlinux-x.git"
echo_cmd "Cloning wrlinux 1018 source from github:"
cd ${SRC_WRL_DIR}
${RUN_CMD}

RUN_CMD="./wrlinux-x/setup.sh --machines intel-x86-64 --layers meta-cloud-services"
echo_cmd "Setup wrlinux build project:"
${RUN_CMD}

# Clone the oran layer if it's not already cloned
# Check if the script is inside the repo
if cd ${SCRIPTS_DIR} && git rev-parse --is-inside-work-tree > /dev/null 2>&1; then
    CLONED_ORAN_REPO=`dirname ${SCRIPTS_DIR}`
    echo_info "Use the cloned oran repo: ${CLONED_ORAN_REPO}"
    cd ${SRC_ORAN_DIR}
    ln -sf ${CLONED_ORAN_REPO} rtp
else
    echo_info "Cloning oran layer:"
    cd ${SRC_ORAN_DIR}
    RUN_CMD="git clone https://gerrit.o-ran-sc.org/r/pti/rtp"
    echo_cmd "Cloing with:"
    ${RUN_CMD}
fi

# Source the build env
cd ${SRC_WRL_DIR}
. ./environment-setup-x86_64-wrlinuxsdk-linux
set ${PRJ_BUILD_DIR}
. ./oe-init-build-env ${PRJ_BUILD_DIR}

# Add the meta-oran layer
cd ${PRJ_BUILD_DIR}
RUN_CMD="bitbake-layers add-layer ${SRC_ORAN_DIR}/rtp/meta-oran"
echo_cmd "Add the meta-oran layer into the build project"
${RUN_CMD}

# Add extra configs into local.conf
cat << EOF >> conf/local.conf
########################
# Configs for oran-inf #
########################
DISTRO = "oran-inf"
BB_NO_NETWORK = '0'
WRTEMPLATE += "feature/oran-host-rt-tune"
EOF

if [ "${EXTRA_CONF}" != "" ] && [ -f "${EXTRA_CONF}" ]; then
    cat ${EXTRA_CONF} >> conf/local.conf
fi

# Build the oran-inf-host image
mkdir -p logs
TIMESTAMP=`date +"%Y%m%d_%H%M%S"`
RUN_CMD="bitbake ${DRYRUN} oran-image-inf-host"
echo_cmd "Build the oran-image-inf-host image"
bitbake ${DRYRUN} oran-image-inf-host 2>&1|tee logs/bitbake_oran-image-inf-host_${TIMESTAMP}.log

echo_info "Build succeeded, you can get the image in ${PRJ_BUILD_DIR}/tmp-glibc/deploy/images/intel-x86-64/oran-image-inf-host-intel-x86-64.iso"
