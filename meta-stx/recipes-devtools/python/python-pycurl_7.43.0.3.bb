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

DESCRIPTION = "libcurl python bindings"
LICENSE = "LGPLv2 | MIT"
LIC_FILES_CHKSUM = " \
	file://COPYING-LGPL;md5=4fbd65380cdd255951079008b364516c \
	file://COPYING-MIT;md5=2df767ed35d8ea83de4a93feb55e7815 \
	"

SRC_URI[sha256sum] = "6f08330c5cf79fa8ef68b9912b9901db7ffd34b63e225dce74db56bb21deda8e"


PYPI_PACKAGE = "pycurl"
inherit pypi setuptools

export BUILD_SYS
export HOST_SYS
export STAGING_INCDIR
export STAGING_LIBDIR

DEPENDS = " curl python"

BBCLASSEXTEND = " native"
