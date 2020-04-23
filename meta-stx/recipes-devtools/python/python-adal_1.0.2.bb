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

DESCRIPTION = "Microsoft Azure Active Directory Authentication Library (ADAL) for Python"
HOMEPAGE = "https://github.com/AzureAD/azure-activedirectory-library-for-python"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://setup.py;beginline=2;endline=27;md5=43b81fae9d7baa1f0b1c9774a68ca33a"


inherit pypi setuptools

PYPI_PACKAGE = "adal"

SRC_URI[md5sum] = "895791621c696fbbb00dee975260f890" 
SRC_URI[sha256sum] = "4c020807b3f3cfd90f59203077dd5e1f59671833f8c3c5028ec029ed5072f9ce"

RDEPENDS_${PN} += " \
	${PYTHON_PN}-requests \
	${PYTHON_PN}-dateutil \
	${PYTHON_PN}-pyjwt \
	${PYTHON_PN}-crypt \
	${PYTHON_PN}-datetime \
	${PYTHON_PN}-json \
	${PYTHON_PN}-logging \
	${PYTHON_PN}-netclient \
	${PYTHON_PN}-threading \
	${PYTHON_PN}-xml \
	"
