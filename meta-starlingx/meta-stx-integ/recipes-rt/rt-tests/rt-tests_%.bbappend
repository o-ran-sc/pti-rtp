PACKAGECONFIG ?= "numa"
PACKAGECONFIG[numa] = ",,numactl,numactl"

EXTRA_OEMAKE = "\
    ${@bb.utils.contains('PACKAGECONFIG', 'numa', 'NUMA=1', 'NUMA=0', d)} \
    PYLIB=${libdir}/python2.7/site-packages \
"

FILES_${PN} += "\
    ${libdir}/python2.7/site-packages \
"

RDEPENDS_${PN} += "python"
