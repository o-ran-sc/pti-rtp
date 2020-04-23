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

do_install_append () {
	# remove the symlinks
	rm ${D}/www/logs
	rm ${D}/www/var

	# use tmpfile to create dirs
	install -d ${D}${sysconfdir}/tmpfiles.d/
	echo "d /www/var 0755 www root -" > ${D}${sysconfdir}/tmpfiles.d/${BPN}.conf
	echo "d /www/var/log 0755 www root -" >> ${D}${sysconfdir}/tmpfiles.d/${BPN}.conf
}
