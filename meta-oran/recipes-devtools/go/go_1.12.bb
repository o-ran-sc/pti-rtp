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

require go-${PV}.inc
require recipes-devtools/go/go-target.inc

export GOBUILDMODE=""

# Add pie to GOBUILDMODE to satisfy "textrel" QA checking, but mips
# doesn't support -buildmode=pie, so skip the QA checking for mips and its
# variants.
python() {
    if 'mips' in d.getVar('TARGET_ARCH'):
        d.appendVar('INSANE_SKIP_%s' % d.getVar('PN'), " textrel")
    else:
        d.setVar('GOBUILDMODE', 'pie')
}
