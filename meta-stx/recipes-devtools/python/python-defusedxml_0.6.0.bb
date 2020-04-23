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

SUMMARY = "defusedxml -- defusing XML bombs and other exploits"
HOMEPAGE = "https://github.com/tiran/defusedxml"

LICENSE = "Python-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=056fea6a4b395a24d0d278bf5c80249e"
#a5c8025e305fb49e6d405769358851f6

SRC_URI[md5sum] = "a59741f675c4cba649de40a99f732897"
SRC_URI[sha256sum] = "f684034d135af4c6cbb949b8a4d2ed61634515257a67299e5f940fbaa34377f5"

inherit pypi setuptools
