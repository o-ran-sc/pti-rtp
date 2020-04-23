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

PACKAGES += " logrotate-config"

RDEPENDS_logrotate-config_append = " bash"

do_configure_append () {
	:
} 

do_compile_append () {
	:
}

do_install_append () {
	cd ${S}/logging/logrotate-config/files/
	install -d ${D}/${datadir}/starlingx
	mkdir -p ${D}/${sysconfdir}/cron.d
	install -m 644 logrotate-cron.d ${D}/${sysconfdir}/cron.d/logrotate
	install -m 644 logrotate.conf ${D}/${datadir}/starlingx/logrotate.conf
}

FILES_logrotate-config_append = " \
	${sysconfdir}/cron.d/logrotate \
	${datadir}/starlingx/logrotate.conf \
	"
