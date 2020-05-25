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

require recipes-containers/kubernetes/kubernetes_git.bb

SRC_URI = " \
    git://github.com/kubernetes/kubernetes.git;branch=${SRC_BRANCH};name=kubernetes \
    file://0001-hack-lib-golang.sh-use-CC-from-environment.patch \
    file://0001-cross-don-t-build-tests-by-default.patch \
    "
