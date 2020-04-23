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

DESCRIPTION = "This is a Client library that can talk to the HPE LeftHand/StoreVirtual Storage array. The HPE LeftHand storage array has a REST web service interface as well as runs SSH. This client library implements a simple interface to talk with that REST interface using the python Requests http library and communicates via SSH using Pytohn’s paramiko library."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI[sha256sum] = "efdea5cdd2ecce20f9bb482dd0a6f55bcefc462df42eaef4d85258c04e991b20"

SRCREV = "db9773f0f97c5af04da89fcb7dca2a6ddf8d5e4a"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/hpe-storage/python-lefthandclient.git;rev=${SRCREV};branch=${BRANCH}"

inherit setuptools distutils
