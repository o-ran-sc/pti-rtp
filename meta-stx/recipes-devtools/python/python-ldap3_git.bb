#
# Copyright (C) 2019 Wind River Systems, Inc.
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

DESCRIPTION = "ldap3 is a strictly RFC 4510 conforming LDAP V3 pure Python client library. The same codebase runs in Python 2, Python 3, PyPy and PyPy3."

LICENSE = "LGPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=32be5282b8f7cafe30b89d2980fbc7d0"

SRCREV = "08810e435392a1f1a96d5e65bb444b9cb9e96ae3"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/cannatag/ldap3;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
