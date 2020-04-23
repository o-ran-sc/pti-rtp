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

RDEPENDS_namespace-utils_append = " bash"

do_configure_append() {
	:
}

do_compile_append() {
	cd ${S}/utilities/namespace-utils/namespace-utils
	${CC} ${TARGET_CFLAGS} -c -o bashns.o bashns.c
	${CC} ${TARGET_LDFLAGS} -o bashns bashns.o
}

do_install_append() {
	cd ${S}/utilities/namespace-utils/namespace-utils

	install -d ${D}/${sbindir}
	install -m 500 bashns  ${D}/${sbindir}
	install -m 500 umount-in-namespace ${D}/${sbindir}
}

FILES_namespace-utils_append = "\
	${sbindir}/bashns \
	${sbindir}/umount-in-namespace \
	"
