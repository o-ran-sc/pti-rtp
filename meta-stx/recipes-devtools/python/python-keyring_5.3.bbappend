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

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
      file://python-keyring/no_keyring_password.patch \
      file://python-keyring/lock_keyring_file.patch \
      file://python-keyring/lock_keyring_file2.patch \
      file://python-keyring/use_new_lock.patch \
      file://python-keyring/fix_keyring_lockfile_location.patch \
      file://python-keyring/use_temporary_file.patch \
      file://python-keyring/chown_keyringlock_file.patch \
      file://python-keyring/chmod_keyringlock2.patch \
      file://python-keyring/keyring_path_change.patch \
      file://python-keyring/remove-reader-lock.patch \
      file://python-keyring/remove_others_perms_on_keyringcfg_file.patch \
"

DEPENDS += " python-hgtools-native"
