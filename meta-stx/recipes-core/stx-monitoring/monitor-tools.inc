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

PACKAGES += " monitor-tools"

# RDEPENDS_monitor-tools += " initscripts-config"

do_configure_append () {
	:
} 

do_compile_append() {
	:
}

do_install_append() {

	cd ${S}/monitor-tools/scripts
	install -m 755 -d ${D}/${bindir}
	# support files ; service and pmon conf
	install -m 644 memtop  ${D}/${bindir}
	install -m 600 schedtop ${D}/${bindir}
	install -m 600 occtop  ${D}/${bindir}
}

FILES_monitor-tools  = " \
	${bindir}/memtop \
	${bindir}/schedtop \
	${bindir}/occtop \
	"
