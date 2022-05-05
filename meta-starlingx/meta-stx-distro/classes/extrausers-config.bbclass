
# This bbclass creates users based on EXTRA_USER_PARAMS through
# extrausers bbclass and is intended to take config options for 
# configuring users uniq environment.

inherit extrausers


PACKAGE_INSTALL_append = " ${@['', 'base-passwd shadow'][bool(d.getVar('EXTRA_USERS_PARAMS_CONFIG'))]}"

ROOTFS_POSTPROCESS_COMMAND_append = " set_user_group_config;"

set_user_group_config () {

# FIXME: parse EXTRA_USERS_PARAMS_CONFIG for options
	
	EXTRA_USERS_PARAMS=" ${EXTRA_USERS_PARAMS_CONFIG}"
	set_user_group

	#Extend path variable for sysadmin
	echo 'PATH=/sbin:/usr/sbin:$PATH' >> ${IMAGE_ROOTFS}/home/sysadmin/.bashrc
	chown sysadmin:sys_protected ${IMAGE_ROOTFS}/home/sysadmin/.bashrc
}
