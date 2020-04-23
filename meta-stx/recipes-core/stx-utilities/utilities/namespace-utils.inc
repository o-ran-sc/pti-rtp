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

PACKAGES += " namespace-utils"

RDEPENDS_namespace-utils += " bash"

DESCRIPTION_namspace-utils = "Titanium Cloud namespace utilities"
SUMMARY_namespace-utils = "namespace utils"

do_configure_append() {
	:
}
do_compile_append() {
	cd ${S}/utilities/namespace-utils/namespace-utils
	$CC ${LDFLAGS} ${CFLAGS} -o bashns bashns.c
}

do_install_append() {
	cd ${S}/utilities/namespace-utils/namespace-utils

	install -d -m0755 ${D}/${sbindir}
	install -m0500 bashns ${D}/${sbindir}
	install -m0500 umount-in-namespace  ${D}/${sbindir}
}

FILES_namespace-utils = "  \
	${sbindir}/bashns \
	${sbindir}/umount-in-namespace \
	"
