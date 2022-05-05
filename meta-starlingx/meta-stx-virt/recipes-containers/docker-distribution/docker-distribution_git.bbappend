
PKG_NAME = "github.com/docker/distribution"

SYSROOT_PREPROCESS_FUNCS += "docker_distribution_sysroot_preprocess"

docker_distribution_sysroot_preprocess () {
    install -d ${SYSROOT_DESTDIR}${prefix}/local/go/src/${PKG_NAME}
    cp -r ${S} ${SYSROOT_DESTDIR}${prefix}/local/go/src/$(dirname ${PKG_NAME})
}
