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

# Update log with the message: adopted from upstream e1f2e1ae5f27d889e3d4bef1b23edcf8a6d804c0
DESCRIPTION = " \
	PrettyTable is a simple Python library designed to make it quick and easy to represent tabular data \
	in visually appealing ASCII tables. It was inspired by the ASCII tables used in the PostgreSQL shell \
	psql. PrettyTable allows for selection of which columns are to be printed, independent alignment of \
	columns (left or right justified or centred) and printing of “sub-tables” by specifying a row range.\
	"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=3e73500ffa52de5071cff65990055282"

SRC_URI[md5sum] = "0c1361104caff8b09f220748f9d69899"
SRC_URI[sha256sum] = "a53da3b43d7a5c229b5e3ca2892ef982c46b7923b51e98f0db49956531211c4f"

SRCNAME = "prettytable"

SRC_URI = "https://pypi.python.org/packages/source/P/PrettyTable/${SRCNAME}-${PV}.zip"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit setuptools3
do_install_append() {
    find "${D}${PYTHON_SITEPACKAGES_DIR}/" -name "*.txt" -o -name "PKG-INFO" -exec chmod 644 {} +
}

BBCLASSEXTEND = "native nativesdk"
