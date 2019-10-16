#!/bin/sh

help_info () {
cat << ENDHELP
Usage:
$(basename $0) <-w WORKSPACE_DIR> [-n] [-h]
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
    echo "$1"
    echo "CMD: ${RUN_CMD}"
}

if [ $# -eq 0 ]; then
    echo "Missing options!"
    help_info
    exit
fi

DRYRUN=""

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

# Clone extra layers
echo_info "Cloning oran layer:"

cd ${SRC_ORAN_DIR}
RUN_CMD="git clone https://gerrit.o-ran-sc.org/r/pti/rtp"
echo_cmd "Cloing with:"
${RUN_CMD}

# Source the build env
cd ${SRC_WRL_DIR}
. ./environment-setup-x86_64-wrlinuxsdk-linux
set ${PRJ_BUILD_DIR}
. ./oe-init-build-env ${PRJ_BUILD_DIR}

# Add the meta-oran layer and required layers
cd ${PRJ_BUILD_DIR}
bitbake-layers add-layer ${SRC_ORAN_DIR}/rtp/meta-oran

# Add extra configs into local.conf
cat << EOF >> conf/local.conf
########################
# Configs for oran-inf #
########################
DISTRO = "oran-inf"
BB_NO_NETWORK = '0'
EOF

# Build the oran-inf-host image
mkdir -p logs
TIMESTAMP=`date +"%Y%m%d_%H%M%S"`
bitbake ${DRYRUN} oran-image-inf-host 2>&1|tee logs/bitbake_oran-image-inf-host_${TIMESTAMP}.log
