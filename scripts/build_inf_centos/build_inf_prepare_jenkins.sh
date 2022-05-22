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
MIRROR_VER=stx-6.0
MIRROR_CONTAINER_IMG=infbuilder/inf-centos-mirror:2022.05-${MIRROR_VER}

#########################################################################
# Common Functions
#########################################################################

help_info () {
cat << ENDHELP
Usage:
$(basename $0) [-w WORKSPACE_DIR] [-h]
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

get_mirror () {
    msg_step="Get rpm mirror from dockerhub image"
    echo_step_start

    docker pull ${MIRROR_CONTAINER_IMG}
    docker create -ti --name inf-centos-mirror ${MIRROR_CONTAINER_IMG} sh
    docker cp inf-centos-mirror:/mirror_${MIRROR_VER} ${MIRROR_DIR}
    docker rm inf-centos-mirror

    echo_step_end
}


#########################################################################
# Main process
#########################################################################
msg_step="Prepare for jenkins build"

set -x
export BUILD_GROUP="jenkins"
export WGET_OPENDEV="wget --no-check-certificate"
export LOCALDISK="${WORKSPACE}/localdisk"
export MIRROR_DIR="${WORKSPACE}/mirror"

mkdir -p ${LOCALDISK}/loadbuild/mock-cache
mkdir -p ${LOCALDISK}/loadbuild/mock
mkdir -p ${LOCALDISK}/designer
mkdir -p ${LOCALDISK}/loadbuild

#sudo mkdir -p ${MIRROR_DIR}/CentOS
get_mirror

sudo chmod 775 ${LOCALDISK}/loadbuild/mock
sudo chown root:mock ${LOCALDISK}/loadbuild/mock
sudo chmod 775 ${LOCALDISK}/loadbuild/mock-cache
sudo chown root:mock ${LOCALDISK}/loadbuild/mock-cache

# Download required dependencies by mirror/build processes.
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
    docker-client \
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

# clone the tools repo
cd ~
git clone https://opendev.org/starlingx/tools.git

# mock custumizations
# forcing chroots since a couple of packages naughtily insist on network access and
# we dont have nspawn and networks happy together.
sudo useradd -s /sbin/nologin -u 9001 -g 9001 mockbuild
sudo rmdir /var/lib/mock
sudo ln -s ${LOCALDISK}/loadbuild/mock /var/lib/mock
sudo rmdir /var/cache/mock
sudo ln -s ${LOCALDISK}/loadbuild/mock-cache /var/cache/mock
echo "config_opts['use_nspawn'] = False" | sudo tee -a /etc/mock/site-defaults.cfg
echo "config_opts['rpmbuild_networking'] = True" | sudo tee -a /etc/mock/site-defaults.cfg
echo | sudo tee -a /etc/mock/site-defaults.cfg

# cpan modules, installing with cpanminus to avoid stupid questions since cpan is whack
sudo cpanm --notest Fatal
sudo cpanm --notest XML::SAX
sudo cpanm --notest XML::SAX::Expat
sudo cpanm --notest XML::Parser
sudo cpanm --notest XML::Simple

# Install repo tool
sudo wget https://storage.googleapis.com/git-repo-downloads/repo -O /usr/local/bin/repo
sudo chmod a+x /usr/local/bin/repo

# installing go and setting paths
export GOPATH="/usr/local/go"
export PATH="${GOPATH}/bin:${PATH}"
sudo yum install -y golang
sudo mkdir -p ${GOPATH}/bin
curl https://raw.githubusercontent.com/golang/dep/master/install.sh | sudo sh

# pip installs
# Install required python modules globally; versions are in the constraints file.
# Be careful not to replace modules provided by RPMs as it may break
# other system packages. Look for warnings similar to "Uninstalling a
# distutils installed project has been deprecated" from pip.
sudo pip install -c ~/tools/toCOPY/builder-constraints.txt \
    testrepository \
    fixtures \
    pbr \
    git-review \
    python-subunit \
    junitxml \
    testtools

# Create a sane py27 virtualenv
virtualenv /opt/py27 && \
    source /opt/py27/bin/activate && \
    sudo pip install -c ~/tools/toCOPY/builder-opt-py27-constraints.txt \
            tox \
        && \
    for prog in tox ; do \
        ln -s /opt/py27/bin/$prog /usr/bin ; \
    done

# Inherited  tools for mock stuff
# we at least need the mock_cache_unlock tool
# they install into /usr/bin
sudo cp -rf ~/tools/toCOPY/mock_overlay /opt/mock_overlay
cd /opt/mock_overlay
make
sudo make install

# This image requires a set of scripts and helpers
# for working correctly, in this section they are
# copied inside the image.
sudo cp ~/tools/toCOPY/finishSetup.sh /usr/local/bin
sudo cp ~/tools/toCOPY/populate_downloads.sh /usr/local/bin
sudo cp ~/tools/toCOPY/generate-local-repo.sh /usr/local/bin
sudo cp ~/tools/toCOPY/generate-centos-repo.sh /usr/local/bin
sudo cp ~/tools/toCOPY/lst_utils.sh /usr/local/bin

# centos locales are broken. this needs to be run after the last yum install/update
sudo localedef -i en_US -f UTF-8 en_US.UTF-8

# setup
sudo mkdir -p /www/run
sudo mkdir -p /www/logs
sudo mkdir -p /www/home
sudo mkdir -p /www/root/htdocs/localdisk
sudo ln -s ${LOCALDISK}/loadbuild /www/root/htdocs/localdisk/loadbuild
sudo ln -s ${MIRROR_DIR}/CentOS /www/root/htdocs/CentOS
sudo ln -s ${LOCALDISK}/designer /www/root/htdocs/localdisk/designer
sudo ln -s ${WORKSPACE} /www/root/htdocs/workspace

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
    -e "s/dir-listing.activate/#dir-listing.activate/g" \
    /etc/lighttpd/lighttpd.conf

echo "server.port = 8088" | sudo tee -a /etc/lighttpd/lighttpd.conf 
echo "server.use-ipv6 = \"disable\"" | sudo tee -a /etc/lighttpd/lighttpd.conf 
echo "server.username = \"$USER\"" | sudo tee -a /etc/lighttpd/lighttpd.conf 
echo "server.groupname = \"$BUILD_GROUP\"" | sudo tee -a /etc/lighttpd/lighttpd.conf 
echo "server.bind = \"localhost\"" | sudo tee -a /etc/lighttpd/lighttpd.conf 
echo "server.document-root   = \"/www/root/htdocs\"" | sudo tee -a /etc/lighttpd/lighttpd.conf 
echo "dir-listing.activate = \"enable\"" | sudo tee -a /etc/lighttpd/conf.d/dirlisting.conf

sudo /usr/sbin/lighttpd  -f /etc/lighttpd/lighttpd.conf

sudo chmod a+x /usr/local/bin/*

# Customizations for mirror creation
sudo rm -f /etc/yum.repos.d/*
sudo cp -f ~/tools/centos-mirror-tools/yum.repos.d/* /etc/yum.repos.d/
sudo cp -f ~/tools/centos-mirror-tools/rpm-gpg-keys/* /etc/pki/rpm-gpg/

# Import GPG keys
sudo rpm --import /etc/pki/rpm-gpg/RPM-GPG-KEY*

# Try to continue a yum command even if a StarlingX repo is unavailable.
sudo yum-config-manager --setopt=StarlingX\*.skip_if_unavailable=1 --save

echo_step_end
