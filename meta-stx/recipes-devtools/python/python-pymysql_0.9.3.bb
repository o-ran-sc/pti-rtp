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

SUMMARY = "Python MySQL client library"
DESCRIPTION = " This package contains a pure-Python MySQL client library, based on PEP 249." 

SECTION = "devel/python"
HOMEPAGE = "https://github.com/PyMySQL/PyMySQL"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=528175c84163bb800d23ad835c7fa0fc"

inherit pypi setuptools

PYPI_PACKAGE = "PyMySQL"

SRC_URI[md5sum] = "e5d9183cc0a775ac29f9e0365cca6556"
SRC_URI[sha256sum] = "d8c059dcd81dedb85a9f034d5e22dcb4442c0b201908bede99e306d65ea7c8e7"
