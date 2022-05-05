
SRCREV_FORMAT = "configfiles"
SRCREV_configfiles = "d778e862571957ece3c404c0c37d325769772fde"

SRC_URI += "\
    git://opendev.org/starlingx/config-files.git;protocol=https;branch=r/stx.3.0;destsuffix=stx_configfiles;name=configfiles;subpath=audit-config \
"

do_unpack_append () {
    bb.build.exec_func('do_copy_audit_config', d)
}

do_copy_audit_config () {
    cp -f ${WORKDIR}/stx_configfiles/files/syslog.conf ${S}/audisp/plugins/builtins/syslog.conf
}
