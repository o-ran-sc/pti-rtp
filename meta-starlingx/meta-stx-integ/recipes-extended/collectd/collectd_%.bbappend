
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://collectd-fix-for-LIBPYTHON_LDFLAGS.patch \
    file://default-plugins-cpu.conf \
    file://default-plugins-interface.conf \
    file://default-plugins-load.conf \
    file://default-plugins-memory.conf \
    file://default-plugins-syslog.conf \
    "

PACKAGECONFIG += "python"

PACKAGECONFIG[python] = "--enable-python --with-libpython,--disable-python --with-libpython=no,python"

do_install_append () {
    install -m 755 -d ${D}${sysconfdir}/collectd.d/
    install -m 644 ${WORKDIR}/default-plugins-cpu.conf ${D}${sysconfdir}/collectd.d/90-default-plugins-cpu.conf
    install -m 644 ${WORKDIR}/default-plugins-interface.conf ${D}${sysconfdir}/collectd.d/90-default-plugins-interface.conf
    install -m 644 ${WORKDIR}/default-plugins-load.conf ${D}${sysconfdir}/collectd.d/90-default-plugins-load.conf
    install -m 644 ${WORKDIR}/default-plugins-memory.conf ${D}${sysconfdir}/collectd.d/90-default-plugins-memory.conf
    install -m 644 ${WORKDIR}/default-plugins-syslog.conf ${D}${sysconfdir}/collectd.d/90-default-plugins-syslog.conf
}
