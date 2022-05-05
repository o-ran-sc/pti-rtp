
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
