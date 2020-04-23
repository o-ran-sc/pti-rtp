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

include haproxy.inc

SRC_URI += "file://haproxy-${PV}-tpm-support.patch"

SRC_URI[md5sum] = "25be5ad717a71da89a65c3c24250e2eb"
SRC_URI[sha256sum] = "d564b8e9429d1e8e13cb648bf4694926b472e36da1079df946bb732927b232ea"
