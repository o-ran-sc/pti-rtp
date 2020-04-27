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


APACHE_PORT_NUM ?= "81"

do_install_append () {
	sed -i -e 's/80/${APACHE_PORT_NUM}/' ${D}/${sysconfdir}/${BPN}/httpd.conf
}

inherit useradd

USERADD_PACKAGES = "${PN}"

USERADD_PARAM_${PN} = "-c 'Apache' -u 48 -g apache -s /sbin/nologin -r -d /usr/share/httpd apache"
GROUPADD_PARAM_${PN} = "-g 48 -r apache"

# since we switch keystone from apache to gunicorn, disable it as default
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
