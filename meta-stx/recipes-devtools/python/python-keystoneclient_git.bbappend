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

SRC_URI = "\
	git://github.com/openstack/python-keystoneclient.git;branch=stable/rocky \
	"

PV = "3.17.0+git${SRCPV}"
SRCREV = "234ea50d5dfa3c6b71c15d32223a2ddf84c1aa1e"
DEPENDS += " \
        python-pip \
        python-pbr \
        "

RDEPENDS_${PN}_append = " \
	python-keystone \
	keystone-setup \
	keystone-cronjobs \
	keystone \
	"
