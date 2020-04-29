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

PACKAGES += " nfv-vim"

inherit setuptools

do_configure_prepend () {
	cd ${S}/nfv/nfv-vim
	sed -i -e 's|@SYSCONFDIR@|${sysconfdir}|g' \
		scripts/vim \
		scripts/vim-api \
		scripts/vim-webserver \
		nfv_vim/config.ini
	sed -i -e 's|@PYTHONROOT@|${libdir}/python2.7/site-packages|g' nfv_vim/config.ini

	distutils_do_configure
} 

do_compile_prepend () {
	cd ${S}/nfv/nfv-vim
	distutils_do_compile
}

do_install_prepend () {
	cd ${S}/nfv/nfv-vim
	distutils_do_install
	install -d -m 755 ${D}/usr/lib/ocf/resource.d/nfv
	install -p -D -m 755 scripts/vim ${D}/usr/lib/ocf/resource.d/nfv/vim
	install -p -D -m 755 scripts/vim-api ${D}/usr/lib/ocf/resource.d/nfv/vim-api
	install -p -D -m 755 scripts/vim-webserver ${D}/usr/lib/ocf/resource.d/nfv/vim-webserver
	install -d -m 755 ${D}/${sysconfdir}/nfv/
	install -d -m 755 ${D}/${sysconfdir}/nfv/vim/
	install -p -D -m 600 nfv_vim/config.ini ${D}/${sysconfdir}/nfv/vim/config.ini
	install -p -D -m 600 nfv_vim/debug.ini ${D}/${sysconfdir}/nfv/vim/debug.ini

}

#pkg_postinst_ontarget_${PN} () {

FILES_nfv-vim_append = " \
	${sysconfdir}/nfv/vim \
	${libdir}/ocf/resource.d/nfv/vim \
	${libdir}/ocf/resource.d/nfv/vim-api \
	${libdir}/ocf/resource.d/nfv/vim-webserver \
	${bindir}/nfv-vim-webserver \
	${bindir}/nfv-vim-api \
	${bindir}/nfv-vim-manage \
	${bindir}/nfv-vim \
	${libdir}/python2.7/site-packages/nfv_vim \
	${libdir}/python2.7/site-packages/nfv_vim*egg-info \
	"
