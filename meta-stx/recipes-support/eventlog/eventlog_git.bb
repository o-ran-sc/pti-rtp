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

SUMMARY = "syslog replacement"
DESCRIPTION = " \
	The EventLog library aims to be a replacement of the simple syslog() API \
	provided on UNIX systems. The major difference between EventLog and syslog \
	is that EventLog tries to add structure to messages. \
	 \
	Where you had a simple non-structrured string in syslog() you have a \
	combination of description and tag/value pairs. \
	 \
	EventLog provides an interface to build, format and output an event record. \
	The exact format and output method can be customized by the administrator \
	via a configuration file \
	  "

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=b8ba8e77bcda9a53fac0fe39fe957767"

SRCREV = "a5c19163ba131f79452c6dfe4e31c2b4ce4be741"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/balabit/eventlog.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit autotools pkgconfig
