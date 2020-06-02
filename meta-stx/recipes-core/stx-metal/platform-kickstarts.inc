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

PACKAGES += " \
	platform-kickstarts \
	platform-kickstarts-pxeboot \
	platform-kickstarts-extracfgs \
	"

feed_dir = "/www/pages/feed/rel-${STX_REL}"

DEPENDS += "perl-native"

inherit deploy

do_compile_prepend () {
	cd ${S}/bsp-files/
	./centos-ks-gen.pl --release ${STX_REL}
}

do_install_prepend () {
	cd ${S}/bsp-files/
	install -d -m 0755 ${D}${feed_dir}
	install -m 0444 generated/* ${D}${feed_dir}/

	install -d -m 0755 ${D}/pxeboot
	install -D -m 0444 pxeboot/* ${D}/pxeboot

	install -d -m 0755 ${D}/extra_cfgs
	install -D -m 0444 extra_cfgs/* ${D}/extra_cfgs
}

do_deploy () {
	mkdir -p ${DEPLOYDIR}/stx-kickstarts
	cp -f ${S}/bsp-files/generated/* ${DEPLOYDIR}/stx-kickstarts
}

addtask do_deploy after do_compile before do_build

FILES_platform-kickstarts = "${feed_dir}"
FILES_platform-kickstarts-pxeboot = "/pxeboot"
FILES_platform-kickstarts-extracfgs = "/extra_cfgs"
