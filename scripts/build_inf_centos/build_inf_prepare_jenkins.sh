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

SCRIPTS_DIR=$(dirname $(readlink -f $0))
SCRIPTS_NAME=$(basename $0)

TOOLS_DIR="${SCRIPTS_DIR}/tools"
STEP_STATUS_DIR="${SCRIPTS_DIR}/step_status"

BUILD_GROUP="jenkins"
WGET_OPENDEV="wget --no-check-certificate"

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
# Functions
#########################################################################
LOCALDISK="${WORKSPACE}/localdisk"
MIRROR_DIR="${WORKSPACE}/mirror"

prepare_env () {
    msg_step="Prepare build directories"
    echo_step_start

    mkdir -p ${LOCALDISK}/loadbuild/mock-cache
    mkdir -p ${LOCALDISK}/loadbuild/mock
    mkdir -p ${LOCALDISK}/designer
    mkdir -p ${LOCALDISK}/loadbuild
    mkdir -p ${STEP_STATUS_DIR}

    sudo chmod 775 ${LOCALDISK}/loadbuild/mock
    sudo chown root:mock ${LOCALDISK}/loadbuild/mock
    sudo chmod 775 ${LOCALDISK}/loadbuild/mock-cache
    sudo chown root:mock ${LOCALDISK}/loadbuild/mock-cache

    TMP_OPT_DIR=${SCRIPTS_DIR}/opt
    mkdir -p ${TMP_OPT_DIR}

    echo_step_end
}

install_pkgs () {
    msg_step="Install/downlaod/config required dependencies by mirror/build processes."
    echo_step_start

    echo_info "Install required packages"
    sudo yum install -y \
        anaconda \
        anaconda-runtime \
        autoconf-archive \
        autogen \
        automake \
        bc \
        bind \
        bind-utils \
        bison \
        cpanminus \
        createrepo \
        createrepo_c \
        deltarpm \
        expat-devel \
        flex \
        isomd5sum \
        gcc \
        gettext \
        libguestfs-tools \
        libtool \
        libxml2 \
        lighttpd \
        lighttpd-fastcgi \
        lighttpd-mod_geoip \
        net-tools \
        mkisofs \
        mongodb \
        mongodb-server \
        pax \
        perl-CPAN \
        python-deltarpm \
        python-pep8 \
        python-pip \
        python-psutil \
        python2-psutil \
        python36-psutil \
        python36-requests \
        python3-devel \
        python-sphinx \
        python-subunit \
        python-virtualenv \
        python-yaml \
        python2-ruamel-yaml \
        postgresql \
        qemu-kvm \
        quilt \
        rpm-build \
        rpm-sign \
        rpm-python \
        squashfs-tools \
        sudo \
        systemd \
        syslinux \
        udisks2 \
        vim-enhanced \
        wget

    echo_info "Install required cpan modules"
    # cpan modules, installing with cpanminus to avoid stupid questions since cpan is whack
    sudo cpanm --notest Fatal
    sudo cpanm --notest XML::SAX
    sudo cpanm --notest XML::SAX::Expat
    sudo cpanm --notest XML::Parser
    sudo cpanm --notest XML::Simple

    echo_info "Install repo tool"
    sudo wget https://storage.googleapis.com/git-repo-downloads/repo -O /usr/local/bin/repo
    sudo chmod a+x /usr/local/bin/repo

    echo_info "Install go and setting paths"
    export GOPATH="/usr/local/go"
    export PATH="${GOPATH}/bin:${PATH}"
    sudo yum install -y golang
    sudo mkdir -p ${GOPATH}/bin
    curl https://raw.githubusercontent.com/golang/dep/master/install.sh | sudo -E sh

    echo_info "Install pip packages"
    # Install required python modules globally; versions are in the constraints file.
    # Be careful not to replace modules provided by RPMs as it may break
    # other system packages. Look for warnings similar to "Uninstalling a
    # distutils installed project has been deprecated" from pip.
    sudo pip install -c ${TOOLS_DIR}/toCOPY/builder-constraints.txt \
        testrepository \
        fixtures \
        pbr \
        git-review \
        python-subunit \
        junitxml \
        testtools

    # This image requires a set of scripts and helpers
    # for working correctly, in this section they are
    # copied inside the image.
    sudo cp ${TOOLS_DIR}/toCOPY/finishSetup.sh /usr/local/bin
    sudo cp ${TOOLS_DIR}/toCOPY/populate_downloads.sh /usr/local/bin
    sudo cp ${TOOLS_DIR}/toCOPY/generate-local-repo.sh /usr/local/bin
    sudo cp ${TOOLS_DIR}/toCOPY/generate-centos-repo.sh /usr/local/bin
    sudo cp ${TOOLS_DIR}/toCOPY/lst_utils.sh /usr/local/bin

    sudo chmod a+x /usr/local/bin/*

    echo_step_end
}

get_tools_repo () {
    msg_step="Clone the tools repo"
    echo_step_start

    cd ${SCRIPTS_DIR}
    if [ -d tools/.git ]; then
        echo_info "The 'tools' repo already exists, skipping"
    else
        git clone https://opendev.org/starlingx/tools.git
    fi

    echo_step_end
}

config_mock () {
    msg_step="mock custumizations"
    echo_step_start
    # forcing chroots since a couple of packages naughtily insist on network access and
    # we dont have nspawn and networks happy together.
    sudo groupadd -f -g 9001 mockbuild
    sudo useradd -s /sbin/nologin -u 9001 -g 9001 mockbuild || echo "User mockbuild already exists."
    [ -L /var/lib/mock ] || {
        sudo rmdir /var/lib/mock
        sudo ln -s ${LOCALDISK}/loadbuild/mock /var/lib/mock
    }

    [ -L /var/cache/mock ] || {
        sudo mv /var/cache/mock/* ${LOCALDISK}/loadbuild/mock-cache/
        sudo rmdir /var/cache/mock
        sudo ln -s ${LOCALDISK}/loadbuild/mock-cache /var/cache/mock
    }
    echo "config_opts['use_nspawn'] = False" | sudo tee -a /etc/mock/site-defaults.cfg
    echo "config_opts['rpmbuild_networking'] = True" | sudo tee -a /etc/mock/site-defaults.cfg
    echo | sudo tee -a /etc/mock/site-defaults.cfg

    echo_step_end
}

install_mock_overlay () {
    msg_step="Inherited tools for mock stuff"
    echo_step_start

    # we at least need the mock_cache_unlock tool
    # they install into /usr/bin
    cp -rf ${TOOLS_DIR}/toCOPY/mock_overlay ${TMP_OPT_DIR}/mock_overlay
    cd ${TMP_OPT_DIR}/mock_overlay
    make
    sudo make install

    echo_step_end
}

config_yum () {
    msg_step="configs for yum and rpm"
    echo_step_start

    # Customizations for mirror creation
    sudo rm -f /etc/yum.repos.d/*
    sudo cp -f ${TOOLS_DIR}/centos-mirror-tools/yum.repos.d/* /etc/yum.repos.d/
    sudo cp -f ${TOOLS_DIR}/centos-mirror-tools/rpm-gpg-keys/* /etc/pki/rpm-gpg/

    # Import GPG keys
    sudo rpm --import /etc/pki/rpm-gpg/RPM-GPG-KEY*

    # Try to continue a yum command even if a StarlingX repo is unavailable.
    sudo yum-config-manager --setopt=StarlingX\*.skip_if_unavailable=1 --save

    echo_step_end
}

create_py27_venv () {
    msg_step="Create a sane py27 virtualenv"
    echo_step_start

    virtualenv ${TMP_OPT_DIR}/py27
    source ${TMP_OPT_DIR}/py27/bin/activate
    pip install -c ${TOOLS_DIR}/toCOPY/builder-opt-py27-constraints.txt tox
    sudo ln -s ${TMP_OPT_DIR}/py27/bin/tox /usr/bin

    echo_step_end
}

setup_lighttpd () {
    msg_step="Setup for lighttpd"
    echo_step_start

    sudo mkdir -p /www
    sudo chown ${USER}:${BUILD_GROUP} /www
    mkdir -p /www/run
    mkdir -p /www/logs
    mkdir -p /www/home
    mkdir -p /www/root/htdocs/localdisk
    mkdir -p /www/root/htdocs/$(dirname ${WORKSPACE})
    ln -s ${LOCALDISK}/loadbuild /www/root/htdocs/localdisk/loadbuild
    ln -s ${LOCALDISK}/designer /www/root/htdocs/localdisk/designer
    ln -s ${MIRROR_DIR}/CentOS /www/root/htdocs/CentOS
    ln -s ${WORKSPACE} /www/root/htdocs/${WORKSPACE}

    # lighthttpd setup
    # chmod for /var/log/lighttpd fixes a centos issue
    # in place sed for server root since it's expanded soon thereafter
    #     echo "server.bind = \"localhost\"" >> /etc/lighttpd/lighttpd.conf && \
    sudo mkdir -p  /var/log/lighttpd
    sudo chmod a+rwx /var/log/lighttpd/
    sudo sed -i -e 's%^var\.log_root.*$%var.log_root = "/www/logs"%g' \
        -e 's%^var\.server_root.*$%var.server_root = "/www/root"%g' \
        -e 's%^var\.home_dir.*$%var.home_dir = "/www/home"%g' \
        -e 's%^var\.state_dir.*$%var.state_dir = "/www/run"%g' \
        -e "s/server.port/#server.port/g" \
        -e "s/server.use-ipv6/#server.use-ipv6/g" \
        -e "s/server.username/#server.username/g" \
        -e "s/server.groupname/#server.groupname/g" \
        -e "s/server.bind/#server.bind/g" \
        -e "s/server.document-root/#server.document-root/g" \
        -e "s/server.dirlisting/#server.dirlisting/g" \
        /etc/lighttpd/lighttpd.conf

    echo "server.port = 8088" | sudo tee -a /etc/lighttpd/lighttpd.conf
    echo "server.use-ipv6 = \"disable\"" | sudo tee -a /etc/lighttpd/lighttpd.conf
    echo "server.username = \"$USER\"" | sudo tee -a /etc/lighttpd/lighttpd.conf
    echo "server.groupname = \"$BUILD_GROUP\"" | sudo tee -a /etc/lighttpd/lighttpd.conf
    echo "server.bind = \"localhost\"" | sudo tee -a /etc/lighttpd/lighttpd.conf
    echo "server.document-root   = \"/www/root/htdocs\"" | sudo tee -a /etc/lighttpd/lighttpd.conf

    sudo sed -i "s/dir-listing.activate/#dir-listing.activate/g" \
        /etc/lighttpd/conf.d/dirlisting.conf

    echo "dir-listing.activate = \"enable\"" | sudo tee -a /etc/lighttpd/conf.d/dirlisting.conf

    sudo /usr/sbin/lighttpd  -f /etc/lighttpd/lighttpd.conf

    echo_step_end
}

#########################################################################
# Main process
#########################################################################

# centos locales are broken. this needs to be run after the last yum install/update
sudo localedef -i en_US -f UTF-8 en_US.UTF-8

prepare_env
get_tools_repo
install_pkgs
config_mock
install_mock_overlay
setup_lighttpd
config_yum
create_py27_venv
