require fault-common.inc

SUBPATH0 = "fm-doc/fm_doc"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

do_configure () {
     :
}

do_compile () {
     :
}

do_install () {
    install -d -m 755 ${D}/${docdir}
    install -d -m 755 ${D}/${sysconfdir}/fm
    install -m 744 events.yaml ${D}/${sysconfdir}/fm/
    install -m 644 events.yaml ${D}/${docdir}
    install -m 755 checkEventYaml ${D}/${docdir}
    install -m 644 parseEventYaml.py ${D}/${docdir}
    install -m 644 check_missing_alarms.py ${D}/${docdir}
}
