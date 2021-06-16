inherit stx-metadata

STX_REPO = "config-files"
STX_SUBPATH = "audit-config"

do_unpack_append () {
    bb.build.exec_func('do_copy_audit_config', d)
}

do_copy_audit_config () {
    cp -f ${STX_METADATA_PATH}/files/syslog.conf ${S}/audisp/plugins/builtins/syslog.conf
}
