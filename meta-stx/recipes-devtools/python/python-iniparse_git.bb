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

DESCRIPTION = "Iniparse is a INI parser for Python"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=52f28065af11d69382693b45b5a8eb54"

SRCREV = "0305d08121461776222e515ae21a8405ee68eef4"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/candlepin/python-iniparse.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"



inherit setuptools distutils
