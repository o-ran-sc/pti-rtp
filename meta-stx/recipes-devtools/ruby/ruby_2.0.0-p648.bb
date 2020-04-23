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

require ruby.inc
inherit openssl10

DEPENDS += " libnsl2"

# file://ruby-CVE-2017-9226.patch # Not applicable
SRC_URI += " \
           file://ruby-CVE-2017-9228.patch \
	   file://ext.socket.extmk.patch \
           "
#  file://run-ptest 

SRC_URI[md5sum] = "05db49992d01731fee023cad09bb4e52"
SRC_URI[sha256sum] = "8690bd6b4949c333b3919755c4e48885dbfed6fd055fe9ef89930bde0d2376f8"

# it's unknown to configure script, but then passed to extconf.rb
# maybe it's not really needed as we're hardcoding the result with
# 0001-socket-extconf-hardcode-wide-getaddr-info-test-outco.patch
UNKNOWN_CONFIGURE_WHITELIST += "--enable-wide-getaddrinfo"

PACKAGECONFIG ??= ""

PACKAGECONFIG[valgrind] = "--with-valgrind=yes, --with-valgrind=no, valgrind"
#PACKAGECONFIG[gmp] = "--with-gmp=yes, --with-gmp=no, gmp"

EXTRA_AUTORECONF += "--exclude=aclocal"

#    --disable-versioned-paths 
EXTRA_OECONF ?= " "
EXTRA_OECONF = "\
    --disable-install-doc \
    --disable-rpath \
    --disable-dtrace \
    --enable-shared \
    --enable-load-relative \
"

EXTRA_OEMAKE = " \
    LIBRUBYARG='-lruby-static' \
"

do_configure_prepend() {
    cd ${S}
    rm -rf spec/rubyspec
    git clone git://github.com/ruby/rubyspec.git spec/rubyspec
    cd ${B}
}

do_install() {
    oe_runmake 'DESTDIR=${D}' install
}

do_install_append_class-target () {
    # Find out rbconfig.rb from .installed.list
    rbconfig_rb=`grep rbconfig.rb ${B}/.installed.list`
    # Remove build host directories
    sed -i -e 's:--sysroot=${STAGING_DIR_TARGET}::g' \
           -e s:'--with-libtool-sysroot=${STAGING_DIR_TARGET}'::g \
           -e 's|${DEBUG_PREFIX_MAP}||g' \
           -e 's:${HOSTTOOLS_DIR}/::g' \
           -e 's:${RECIPE_SYSROOT_NATIVE}::g' \
           -e 's:${RECIPE_SYSROOT}::g' \
           -e 's:${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}::g' \
        ${D}$rbconfig_rb

}

do_install_ptest () {
    cp -rf ${S}/test ${D}${PTEST_PATH}/
    cp -r ${S}/include ${D}/${libdir}/ruby/
    test_case_rb=`grep rubygems/test_case.rb ${B}/.installed.list`
    sed -i -e 's:../../../test/:../../../ptest/test/:g' ${D}/$test_case_rb
}

FILES_${PN} += "${datadir}/rubygems"


BBCLASSEXTEND = "native nativesdk"

# INSANE_SKIP_${PN} += "ldflags"
