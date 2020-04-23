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

DESCRIPTION = " stx-integ-config-files"

require config-files/audit-config.inc
require config-files/io-scheduler.inc
require config-files/docker-config.inc
require config-files/iptables-config.inc
require config-files/memcached-custom.inc
require config-files/ntp-config.inc
require config-files/pam-config.inc
require config-files/shadow-utils-config.inc
require config-files/rsync-config.inc
require config-files/sudo-config.inc
require config-files/syslog-ng-config.inc
require config-files/util-linux-config.inc
