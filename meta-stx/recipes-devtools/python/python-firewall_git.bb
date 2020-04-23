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

DESCRIPTION = "Temporarily allow access to your current IP address into security groups, and automatically closes the hole when quitting. Convenient for SSH-ing into a box from a home or from any off-site location without worrying about leaving SSH open to the world or dealing with VPN."

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7f1e0f09645893c55fb92d53ccea0a57"

SRCREV = "59b63088b8b791cc495d809240f94fddaa87dc55"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/mattrobenolt/firewall;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
