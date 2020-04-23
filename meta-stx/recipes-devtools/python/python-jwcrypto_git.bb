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

DESCRIPTION = "An implementation of the JOSE Working Group documents: RFC 7515 - JSON Web Signature (JWS) RFC 7516 - JSON Web Encryption (JWE) RFC 7517 - JSON Web Key (JWK) RFC 7518 - JSON Web Algorithms (JWA) RFC 7519 - JSON Web Token (JWT) RFC 7520 - Examples of Protecting Content Using JSON Object Signing and Encryption (JOSE)"

LICENSE = "LGPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e6a600fd5e1d9cbde2d983680233ad02"

SRCREV = "437ea86caef224cf769e30cafe30f1c0b4e0f3e2"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/latchset/jwcrypto.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"



inherit setuptools distutils
