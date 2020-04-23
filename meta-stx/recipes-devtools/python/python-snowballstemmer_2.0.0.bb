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

DESCRIPTION = " \
	Snowball is a small string processing language designed for creating stemming algorithms \
	for use in Information Retrieval. This site describes Snowball, and presents several useful \
	stemmers which have been implemented using it. \
	"
HOMEPAGE = "https://github.com/snowballstem/snowball"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=2750797da77c1d784e7626b3f7d7ff3e"

SRC_URI[md5sum] = "c05ec4a897be3c953c8b8b844c4241d4"
SRC_URI[sha256sum] = "df3bac3df4c2c01363f3dd2cfa78cce2840a79b9f1c2d2de9ce8d31683992f52"

PYPI_PACKAGE = "snowballstemmer"
inherit setuptools pypi
