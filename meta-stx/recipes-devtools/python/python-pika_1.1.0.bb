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

DESCRIPTION = "Pika is a pure-Python implementation of the AMQP 0-9-1 protocol including RabbitMQ’s extensions."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=535836bf0a5de515a7bfee026075701d"

SRC_URI[sha256sum] = "9fa76ba4b65034b878b2b8de90ff8660a59d925b087c5bb88f8fdbb4b64a1dbf"


PYPI_PACKAGE = "pika"
inherit pypi setuptools
