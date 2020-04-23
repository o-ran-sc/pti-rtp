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

DESCRIPTION = "d2to1 (the ‘d’ is for ‘distutils’) allows using distutils2-like setup.cfg files for a package’s metadata with a distribute/setuptools setup.py script. It works by providing a distutils2-formatted setup.cfg file containing all of a package’s metadata, and a very minimal setup.py which will slurp its arguments from the setup.cfg."

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ec5c9882880dafb7f5a0b8d2642ea581"

SRCREV = "d16d8fdcec77a6d3fcbd10ce23aa8810d8dd3bf2"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/embray/d2to1;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
