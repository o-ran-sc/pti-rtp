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

inherit update-alternatives
ALTERNATIVE_PRIORITY = "70"
ALTERNATIVE_${PN} = "drbd.sh"
ALTERNATIVE_LINK_NAME[drbd.sh] = "${datadir}/cluster/drbd.sh"

do_install_append() {
	mv ${D}${datadir}/cluster/drbd.sh ${D}${datadir}/cluster/drbd.sh.${PN}
}
