DEP_PYTHON = "\
    python \
    python-native \
    python-numpy-native \
    python3 \
    python3-native \
    python3-numpy-native \
"

PACKAGECONFIG = "locale python"
PACKAGECONFIG[python] = ",,${DEP_PYTHON}"

BJAM_OPTS += "${@bb.utils.contains('BOOST_LIBS', 'python', 'python=${PYTHON_BASEVERSION},2.7', '', d)}"

do_configure_append () {
    if ${@bb.utils.contains('BOOST_LIBS', 'python', 'true', 'false', d)}; then
        echo "using python : 2.7 : ${STAGING_BINDIR_NATIVE}/python-native/python : ${STAGING_INCDIR}/python2.7 : ${STAGING_LIBDIR}/python2.7 ;" >> ${WORKDIR}/user-config.jam
        sed -i -e 's|${STAGING_DIR_HOST}${bindir}/python3|${STAGING_BINDIR_NATIVE}/python3-native/python3|' ${WORKDIR}/user-config.jam
    fi
}

PACKAGES += "${PN}-python3"

FILES_${PN}-python = " \
    ${libdir}/libboost_python2*.so.* \
    ${libdir}/libboost_numpy2*.so.* \
"
FILES_${PN}-python3 = " \
    ${libdir}/libboost_python3*.so.* \
    ${libdir}/libboost_numpy3*.so.* \
"
