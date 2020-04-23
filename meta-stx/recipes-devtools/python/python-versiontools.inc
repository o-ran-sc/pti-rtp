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

SUMMARY = "Smart replacement for plain tuple used in __version__"
SECTION = "devel/python"
HOMEPAGE = "https://launchpad.net/versiontools"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://setup.py;beginline=3;endline=20;md5=02193721a38fd8a05a4ddeb7df8e294d"

inherit pypi

SRC_URI[md5sum] = "602b7db8eea30dd29a1d451997adf251"
SRC_URI[sha256sum] = "a969332887a18a9c98b0df0ea4d4ca75972f24ca94f06fb87d591377e83414f6"
