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

python __anonymous() {
   if bb.utils.contains('DEPENDS', 'openssl', True, False, d) or \
      bb.utils.contains('DEPENDS', 'openssl-native', True, False, d):
          d.setVar('DEPENDS', d.getVar('DEPENDS').replace('openssl', 'openssl10'))
          d.setVar('DEPENDS', d.getVar('DEPENDS').replace('openssl-native', 'openssl10-native'))
}


python do_ssl10_mk_symlink() {

    import shutil
    l = d.getVar("STAGING_INCDIR") + "/openssl"

    if os.path.islink(l):
        os.unlink(l)
    elif os.path.isdir(l):
        shutil.rmtree(l)

    os.symlink("openssl10/openssl",l)

    l = d.getVar("STAGING_LIBDIR")
    if os.path.islink(l + "/libssl.so"):
        os.unlink(l + "/libssl.so")
        os.unlink(l + "/libcrypto.so")

    os.symlink("libssl.so.1.0.2", l + "/libssl.so")
    os.symlink("libcrypto.so.1.0.2", l + "/libcrypto.so")
}

addtask ssl10_mk_symlink before do_configure after do_prepare_recipe_sysroot
