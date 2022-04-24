#!/bin/sh

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

#
# Common env
#
# username to build
export MYUNAME=stxbuilder
export MYUID=1001
export MY_EMAIL="oran.inf@windriver.com"
export WGET_OPENDEV="wget --no-check-certificate"
export LOCALDISK="/localdisk"
export MIRROR_DIR="/import/mirrors"

groupadd cgts
echo "mock:x:751:root" >> /etc/group
echo "mockbuild:x:9001:" >> /etc/group

useradd -r -u $MYUID -g cgts -m $MYUNAME
echo "Li69nux*"|sudo passwd --stdin ${MYUNAME}

gpasswd -a $MYUNAME mock

mkdir -p ${LOCALDISK}/loadbuild/mock-cache
mkdir -p ${LOCALDISK}/loadbuild/mock
mkdir -p ${LOCALDISK}/designer
mkdir -p ${MIRROR_DIR}/CentOS

chmod 775 ${LOCALDISK}/loadbuild/mock
chown root:mock ${LOCALDISK}/loadbuild/mock
chmod 775 ${LOCALDISK}/loadbuild/mock-cache
chown root:mock ${LOCALDISK}/loadbuild/mock-cache


# Proxy configuration
export http_proxy="http://147.11.252.42:9090"
export https_proxy="http://147.11.252.42:9090"
export ftp_proxy="http://147.11.252.42:9090"

echo "proxy=$http_proxy" >> /etc/yum.conf && \
    echo -e "export http_proxy=$http_proxy\nexport https_proxy=$https_proxy\n\
export ftp_proxy=$ftp_proxy" >> /root/.bashrc


# CentOS & EPEL URLs that match the base image
# Override these with --build-arg if you have a mirror
CENTOS_7_8_URL=https://vault.centos.org/centos/7.8.2003
CENTOS_7_9_URL=http://mirror.centos.org/centos-7/7.9.2009
EPEL_7_8_URL=https://archives.fedoraproject.org/pub/archive/epel/7.2020-04-20

# Lock down centos & epel repos
rm -f /etc/yum.repos.d/*
cd /etc/yum.repos.d/
${WGET_OPENDEV} https://opendev.org/starlingx/tools/raw/branch/master/toCOPY/yum.repos.d/centos-7.9.repo
${WGET_OPENDEV} https://opendev.org/starlingx/tools/raw/branch/master/toCOPY/yum.repos.d/centos.repo
${WGET_OPENDEV} https://opendev.org/starlingx/tools/raw/branch/master/toCOPY/yum.repos.d/epel.repo

cd /etc/pki/rpm-gpg/
${WGET_OPENDEV} https://opendev.org/starlingx/tools/raw/branch/master/centos-mirror-tools/rpm-gpg-keys/RPM-GPG-KEY-EPEL-7

cd -
rpm --import /etc/pki/rpm-gpg/RPM-GPG-KEY* && \
    echo "http_caching=packages" >> /etc/yum.conf && \
    echo "skip_missing_names_on_install=0" >>/etc/yum.conf && \
    # yum variables must be in lower case ; \
    echo "$CENTOS_7_8_URL" >/etc/yum/vars/centos_7_8_url && \
    echo "$EPEL_7_8_URL" >/etc/yum/vars/epel_7_8_url && \
    echo "$CENTOS_7_9_URL" >/etc/yum/vars/centos_7_9_url && \
    # disable fastestmirror plugin because we are not using mirrors ; \
    # FIXME: use a mirrorlist URL for centos/vault/epel archives. I couldn't find one.
    sed -i 's/enabled=1/enabled=0/' /etc/yum/pluginconf.d/fastestmirror.conf && \
    echo "[main]" >> /etc/yum/pluginconf.d/subscription-manager.conf && \
    echo "enabled=0" >> /etc/yum/pluginconf.d/subscription-manager.conf && \
    yum clean all && \
    yum makecache && \
    yum install -y deltarpm


# root CA cert expired on October 1st, 2021
yum update -y --enablerepo=centos-7.9-updates ca-certificates

# Download required dependencies by mirror/build processes.
yum install -y \
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
        git \
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

# Finally install a locked down version of mock
yum install -y https://mirrors.xlhy1.com/centos/7/updates/x86_64/Packages/python36-rpm-4.11.3-7.el7.x86_64.rpm
yum install -y \
    http://mirror.starlingx.cengn.ca/mirror/centos/epel/dl.fedoraproject.org/pub/epel/7/x86_64/Packages/m/mock-1.4.16-1.el7.noarch.rpm \
    http://mirror.starlingx.cengn.ca/mirror/centos/epel/dl.fedoraproject.org/pub/epel/7/x86_64/Packages/m/mock-core-configs-31.6-1.el7.noarch.rpm

# mock custumizations
# forcing chroots since a couple of packages naughtily insist on network access and
# we dont have nspawn and networks happy together.
useradd -s /sbin/nologin -u 9001 -g 9001 mockbuild && \
    rmdir /var/lib/mock && \
    ln -s ${LOCALDISK}/loadbuild/mock /var/lib/mock && \
    rmdir /var/cache/mock && \
    ln -s ${LOCALDISK}/loadbuild/mock-cache /var/cache/mock && \
    echo "config_opts['use_nspawn'] = False" >> /etc/mock/site-defaults.cfg && \
    echo "config_opts['rpmbuild_networking'] = True" >> /etc/mock/site-defaults.cfg && \
    echo  >> /etc/mock/site-defaults.cfg

# cpan modules, installing with cpanminus to avoid stupid questions since cpan is whack
cpanm --notest Fatal && \
    cpanm --notest XML::SAX  && \
    cpanm --notest XML::SAX::Expat && \
    cpanm --notest XML::Parser && \
    cpanm --notest XML::Simple

# Install repo tool
curl https://storage.googleapis.com/git-repo-downloads/repo > /usr/local/bin/repo && \
    chmod a+x /usr/local/bin/repo

# installing go and setting paths
export GOPATH="/usr/local/go"
export PATH="${GOPATH}/bin:${PATH}"
yum install -y golang && \
    mkdir -p ${GOPATH}/bin && \
    curl https://raw.githubusercontent.com/golang/dep/master/install.sh | sh

# Uprev git, repo
yum install -y dh-autoreconf curl-devel expat-devel gettext-devel openssl-devel perl-devel zlib-devel asciidoc xmlto docbook2X && \
    cd /tmp && \
    wget https://github.com/git/git/archive/v2.29.2.tar.gz -O git-2.29.2.tar.gz && \
    tar xzvf git-2.29.2.tar.gz && \
    cd git-2.29.2 && \
    make configure && \
    ./configure --prefix=/usr/local && \
    make all doc && \
    make install install-doc && \
    cd /tmp && \
    rm -rf git-2.29.2.tar.gz git-2.29.2

# Systemd Enablement
#(cd /lib/systemd/system/sysinit.target.wants/; for i in *; do [ $i == systemd-tmpfiles-setup.service ] || rm -f $i; done); \
#    rm -f /lib/systemd/system/multi-user.target.wants/*;\
#    rm -f /etc/systemd/system/*.wants/*;\
#    rm -f /lib/systemd/system/local-fs.target.wants/*; \
#    rm -f /lib/systemd/system/sockets.target.wants/*udev*; \
#    rm -f /lib/systemd/system/sockets.target.wants/*initctl*; \
#    rm -f /lib/systemd/system/basic.target.wants/*;\
#    rm -f /lib/systemd/system/anaconda.target.wants/*

# pip installs
# Install required python modules globally; versions are in the constraints file.
# Be careful not to replace modules provided by RPMs as it may break
# other system packages. Look for warnings similar to "Uninstalling a
# distutils installed project has been deprecated" from pip.
pip install -c ~/tools/toCOPY/builder-constraints.txt \
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
    pip install -c ~/tools/toCOPY/builder-opt-py27-constraints.txt \
            tox \
        && \
    for prog in tox ; do \
        ln -s /opt/py27/bin/$prog /usr/bin ; \
    done

# Inherited  tools for mock stuff
# we at least need the mock_cache_unlock tool
# they install into /usr/bin
cp -rf ~/tools/toCOPY/mock_overlay /opt/mock_overlay
cd /opt/mock_overlay && \
    make && \
    make install

# This image requires a set of scripts and helpers
# for working correctly, in this section they are
# copied inside the image.
cp ~/tools/toCOPY/finishSetup.sh /usr/local/bin
cp ~/tools/toCOPY/populate_downloads.sh /usr/local/bin
cp ~/tools/toCOPY/generate-local-repo.sh /usr/local/bin
cp ~/tools/toCOPY/generate-centos-repo.sh /usr/local/bin
cp ~/tools/toCOPY/lst_utils.sh /usr/local/bin
cp ~/tools/toCOPY/.inputrc /home/$MYUNAME/
chown $MYUNAME:cgts /home/$MYUNAME/.inputrc

# Thes are included for backward compatibility, and
# should be removed after a reasonable time.
cp ~/tools/toCOPY/generate-cgcs-tis-repo /usr/local/bin
cp ~/tools/toCOPY/generate-cgcs-centos-repo.sh /usr/local/bin

#  ENV setup
echo "# Load stx-builder configuration" >> /etc/profile.d/stx-builder-conf.sh && \
    echo "if [[ -r \${HOME}/buildrc ]]; then" >> /etc/profile.d/stx-builder-conf.sh && \
    echo "    source \${HOME}/buildrc" >> /etc/profile.d/stx-builder-conf.sh && \
    echo "    export PROJECT SRC_BUILD_ENVIRONMENT MYPROJECTNAME MYUNAME" >> /etc/profile.d/stx-builder-conf.sh && \
    echo "    export MY_BUILD_CFG MY_BUILD_CFG_RT MY_BUILD_CFG_STD MY_BUILD_DIR MY_BUILD_ENVIRONMENT MY_BUILD_ENVIRONMENT_FILE MY_BUILD_ENVIRONMENT_FILE_RT MY_BUILD_ENVIRONMENT_FILE_STD MY_DEBUG_BUILD_CFG_RT MY_DEBUG_BUILD_CFG_STD MY_LOCAL_DISK MY_MOCK_ROOT MY_REPO MY_REPO_ROOT_DIR MY_SRC_RPM_BUILD_DIR MY_RELEASE MY_WORKSPACE LAYER" >> /etc/profile.d/stx-builder-conf.sh && \
    echo "fi" >> /etc/profile.d/stx-builder-conf.sh && \
    echo "export FORMAL_BUILD=0" >> /etc/profile.d/stx-builder-conf.sh && \
    echo "export PATH=\$MY_REPO/build-tools:\$PATH" >> /etc/profile.d/stx-builder-conf.sh

# centos locales are broken. this needs to be run after the last yum install/update
localedef -i en_US -f UTF-8 en_US.UTF-8

# setup
mkdir -p /www/run && \
    mkdir -p /www/logs && \
    mkdir -p /www/home && \
    mkdir -p /www/root/htdocs/localdisk && \
    chown -R $MYUID:cgts /www && \
    ln -s ${LOCALDISK}/loadbuild /www/root/htdocs/localdisk/loadbuild && \
    ln -s ${MIRROR_DIR}/CentOS /www/root/htdocs/CentOS && \
    ln -s ${LOCALDISK}/designer /www/root/htdocs/localdisk/designer

# lighthttpd setup
# chmod for /var/log/lighttpd fixes a centos issue
# in place sed for server root since it's expanded soon thereafter
#     echo "server.bind = \"localhost\"" >> /etc/lighttpd/lighttpd.conf && \
echo "$MYUNAME ALL=(ALL:ALL) NOPASSWD:ALL" >> /etc/sudoers && \
    mkdir -p  /var/log/lighttpd  && \
    chmod a+rwx /var/log/lighttpd/ && \
    sed -i 's%^var\.log_root.*$%var.log_root = "/www/logs"%g' /etc/lighttpd/lighttpd.conf  && \
    sed -i 's%^var\.server_root.*$%var.server_root = "/www/root"%g' /etc/lighttpd/lighttpd.conf  && \
    sed -i 's%^var\.home_dir.*$%var.home_dir = "/www/home"%g' /etc/lighttpd/lighttpd.conf  && \
    sed -i 's%^var\.state_dir.*$%var.state_dir = "/www/run"%g' /etc/lighttpd/lighttpd.conf  && \
    sed -i "s/server.port/#server.port/g" /etc/lighttpd/lighttpd.conf  && \
    sed -i "s/server.use-ipv6/#server.use-ipv6/g" /etc/lighttpd/lighttpd.conf && \
    sed -i "s/server.username/#server.username/g" /etc/lighttpd/lighttpd.conf && \
    sed -i "s/server.groupname/#server.groupname/g" /etc/lighttpd/lighttpd.conf && \
    sed -i "s/server.bind/#server.bind/g" /etc/lighttpd/lighttpd.conf && \
    sed -i "s/server.document-root/#server.document-root/g" /etc/lighttpd/lighttpd.conf && \
    sed -i "s/server.dirlisting/#server.dirlisting/g" /etc/lighttpd/lighttpd.conf && \
    echo "server.port = 8088" >> /etc/lighttpd/lighttpd.conf && \
    echo "server.use-ipv6 = \"disable\"" >> /etc/lighttpd/lighttpd.conf && \
    echo "server.username = \"$MYUNAME\"" >> /etc/lighttpd/lighttpd.conf && \
    echo "server.groupname = \"cgts\"" >> /etc/lighttpd/lighttpd.conf && \
    echo "server.bind = \"localhost\"" >> /etc/lighttpd/lighttpd.conf && \
    echo "server.document-root   = \"/www/root/htdocs\"" >> /etc/lighttpd/lighttpd.conf && \
    sed -i "s/dir-listing.activate/#dir-listing.activate/g" /etc/lighttpd/conf.d/dirlisting.conf && \
    echo "dir-listing.activate = \"enable\"" >> /etc/lighttpd/conf.d/dirlisting.conf

#systemctl enable lighttpd
#systemctl start lighttpd
/usr/sbin/lighttpd  -f /etc/lighttpd/lighttpd.conf


echo "export PATH=/usr/local/bin:${LOCALDISK}/designer/$MYUNAME/bin:\$PATH" >> /home/$MYUNAME/.bashrc
chmod a+x /usr/local/bin/*

# Genrate a git configuration file in order to save an extra step
# for end users, this file is required by "repo" tool.
cd /home/$MYUNAME/
runuser -u $MYUNAME -- git config --global user.email $MY_EMAIL && \
    runuser -u $MYUNAME -- git config --global user.name $MYUNAME && \
    runuser -u $MYUNAME -- git config --global color.ui false

# Customizations for mirror creation
rm -f /etc/yum.repos.d/*
cp -f ~/tools/centos-mirror-tools/yum.repos.d/* /etc/yum.repos.d/
cp -f ~/tools/centos-mirror-tools/rpm-gpg-keys/* /etc/pki/rpm-gpg/

# Import GPG keys
rpm --import /etc/pki/rpm-gpg/RPM-GPG-KEY*

# Try to continue a yum command even if a StarlingX repo is unavailable.
yum-config-manager --setopt=StarlingX\*.skip_if_unavailable=1 --save
