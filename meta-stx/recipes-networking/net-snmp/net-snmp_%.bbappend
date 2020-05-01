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

inherit useradd

USERADD_PACKAGES = "net-snmp-server-snmpd"
USERADD_PARAM_net-snmp-server-snmpd = "-r -g snmpd -d /usr/share/snmp -s /sbin/nologin -c 'net-snmp' snmpd"
GROUPADD_PARAM_net-snmp-server-snmpd = "-r snmpd"

SYSTEMD_AUTO_ENABLE_${PN}-server-snmpd = "disable"
SYSTEMD_AUTO_ENABLE_${PN}-server-snmptrapd = "disable"
