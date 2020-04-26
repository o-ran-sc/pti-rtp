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

SUMMARY = "gunicorn 'Green Unicorn' is a WSGI HTTP Server for UNIX"
DESCRIPTION = "\
Gunicorn 'Green Unicorn' is a Python WSGI HTTP Server for UNIX. \
It's a pre-fork worker model ported from Ruby's Unicorn project. \
The Gunicorn server is broadly compatible with various web frameworks, \
simply implemented, light on server resource usage, and fairly speedy. \
"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=03ccc2b0215ca2a4b1e5f3c5483807f9"


SRCREV = "328e509260ae70de6c04c5ba885ee17960b3ced5"
PROTOCOL = "https"
BRANCH = "19.x"
S = "${WORKDIR}/git"
PV = "19.7.1+git${SRCPV}"

SRC_URI = "git://github.com/benoitc/gunicorn.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
