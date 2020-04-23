#
## Copyright (C) 2019 Wind River Systems, Inc.
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

DESCRIPTION = "Client library for OpenStack Compute API"
HOMEPAGE = "https://github.com/openstack/python-novaclient"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7cdb54622cacc9bc9b2883091e6dd669"

SRC_URI = "git://github.com/openstack/python-novaclient.git;branch=stable/pike"

SRCREV = "62bf8809c660ed0675f301c235b1d434caeaf580"
SRCNAME = "python-novaclient"
PROTOCOL = "https"
BRANCH = "stable/train"
S = "${WORKDIR}/git"
PV = "13.0.0+git${SRCPV}"

SRC_URI = "git://github.com/openstack/${SRCNAME}.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit setuptools python-dir

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
        python-pbr-native \
        "

RDEPENDS_${PN} += " \
        python-pbr \
        python-keystoneauth1 \
        python-iso8601 \
        python-oslo.i18n \
        python-oslo.serialization \
        python-oslo.utils \
        python-prettytable \
        python-simplejson \
        python-six \
        python-babel \
        "

PACKAGECONFIG ?= "bash-completion"
PACKAGECONFIG[bash-completion] = ",,bash-completion,bash-completion ${BPN}-bash-completion"

do_install_append() {
	install -d ${D}/${sysconfdir}/bash_completion.d
	install -m 664 ${S}/tools/nova.bash_completion ${D}/${sysconfdir}/bash_completion.d

	mv ${D}/${bindir}/nova ${D}/${bindir}/nova-${PYTHON_BASEVERSION}
	ln -s ./nova-${PYTHON_BASEVERSION}  ${D}/${bindir}/nova-2
	ln -s ./nova-2  ${D}/${bindir}/nova

	if [ -e "${D}/${PYTHON_SITEPACKAGES_DIR}/novaclient/tests/v1_1/test_servers.py" ]; then
		sed -e "s:%PYTHON_SITEPACKAGES_DIR%:${PYTHON_SITEPACKAGES_DIR}:g" \
		    -i ${D}/${PYTHON_SITEPACKAGES_DIR}/novaclient/tests/v1_1/test_servers.py
	fi

}

PACKAGES =+ "${BPN}-bash-completion"
FILES_${BPN}-bash-completion = "${sysconfdir}/bash_completion.d/*"
