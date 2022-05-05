
include haproxy.inc

SRC_URI += "file://haproxy-${PV}-tpm-support.patch"

SRC_URI[md5sum] = "25be5ad717a71da89a65c3c24250e2eb"
SRC_URI[sha256sum] = "d564b8e9429d1e8e13cb648bf4694926b472e36da1079df946bb732927b232ea"
