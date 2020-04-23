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

SUMMARY = "Python interface to MySQL"
HOMEPAGE = "https://github.com/farcepest/MySQLdb1"
SECTION = "devel/python"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://GPL-2.0;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "mysql5"

SRCNAME = "MySQL-python"

SRC_URI = "https://pypi.python.org/packages/source/M/${SRCNAME}/${SRCNAME}-${PV}.zip \
           file://0001-_mysql.c-fix-compilation-with-MariaDB-with-10.3.13.patch \
"
SRC_URI[md5sum] = "654f75b302db6ed8dc5a898c625e030c"
SRC_URI[sha256sum] = "811040b647e5d5686f84db415efd697e6250008b112b6909ba77ac059e140c74"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit setuptools
