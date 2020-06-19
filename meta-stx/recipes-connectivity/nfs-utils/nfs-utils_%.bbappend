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

do_install_append() {
	mv ${D}/${sbindir}/sm-notify ${D}/${sbindir}/nfs-utils-client_sm-notify

	# install nfs.conf and enable udp proto
	install -m 0755 ${S}/nfs.conf ${D}${sysconfdir}
	sed -i -e 's/#\(\[nfsd\]\)/\1/' -e 's/#\( udp=\).*/\1y/' ${D}${sysconfdir}/nfs.conf

	# add initial exports file
	echo "# Initial exports for nfs" > ${D}${sysconfdir}/exports
}

SYSTEMD_AUTO_ENABLE = "disable"
