
FILESEXTRAPATHS_append:= ":${THISDIR}/linux:"

SRC_URI_append_poky-stx = " file://stx-kconfig.cfg"
SRC_URI_append_anaconda = " file://stx-anaconda-kconfig.cfg"
SRC_URI_append = " file://Notification-of-Death-of-arbitrary-processes.patch"

KERNEL_EXTRA_FEATURES_append = " \
    features/xfs/xfs.scc \
    features/iommu/iommu.scc \
    features/vfio/vfio.scc \
    features/x2apic \
    cfg/debug/sched/debug-sched.scc \
    "
