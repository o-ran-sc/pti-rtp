#
# Copyright (C) 2019 Wind River Systems, Inc.
#

inherit go112

do_compile_append() {
    # build ipam plugins
    cd ${S}/src/import/vendor/github.com/containernetworking/plugins/
    PLUGINS="$(ls -d plugins/ipam/*)"
    mkdir -p ${WORKDIR}/plugins/bin/
    for p in $PLUGINS; do
        plugin="$(basename "$p")"
        echo "building: $p"
        go build -o ${WORKDIR}/plugins/bin/$plugin github.com/containernetworking/plugins/$p
    done
}
