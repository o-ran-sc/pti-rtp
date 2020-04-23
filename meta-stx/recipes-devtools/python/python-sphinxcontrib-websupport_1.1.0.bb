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
	sphinxcontrib-websupport provides a Python API to easily integrate Sphinx\
	documentation into your Web application. \
	"
HOMEPAGE = "https://www.sphinx-doc.org/en/master/"
SECTION = "devel/python"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=819a10ab58e77e03e61a584de6339f7c"

SRC_URI[md5sum] = "ca6435e7b4eb9408df4f54972361e9d3"
SRC_URI[sha256sum] = "9de47f375baf1ea07cdb3436ff39d7a9c76042c10a769c52353ec46e4e8fc3b9"

PYPI_PACKAGE = "sphinxcontrib-websupport"
inherit setuptools pypi
