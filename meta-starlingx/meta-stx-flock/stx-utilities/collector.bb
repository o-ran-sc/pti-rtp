DESCRIPTION = " \
This packages scripts that implement data and log collection that field \
support can execute to gather current state and runtime history for off \
platform analysis and debug. \
"

require utilities-common.inc
SUBPATH0 = "tools/collector/scripts"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI += "file://collector-fix-service-name-binary-path.patch;striplevel=4"

RDEPENDS_${PN}_append += " bash"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {

	install -m0755 -d ${D}/${sysconfdir}/collect.d
	install -m0755 -d ${D}/${sysconfdir}/collect
	install -m0755 -d ${D}/${sbindir}
	install -m0755 -d ${D}/${bindir}
	install -m0755 -d ${D}/${sbindir}

	install -m 755 collect ${D}/${sbindir}/collect
	install -m 755 collect_host ${D}/${sbindir}/collect_host
	install -m 755 collect_date ${D}/${sbindir}/collect_date
	install -m 755 collect_utils ${D}/${sbindir}/collect_utils
	install -m 755 collect_parms ${D}/${sbindir}/collect_parms
	install -m 755 collect_mask_passwords ${D}/${sbindir}/collect_mask_passwords
	install -m 755 expect_done ${D}/${sbindir}/expect_done

	install -m 755 collect_sysinv.sh ${D}/${sysconfdir}/collect.d/collect_sysinv
	install -m 755 collect_psqldb.sh ${D}/${sysconfdir}/collect.d/collect_psqldb
	install -m 755 collect_openstack.sh ${D}/${sysconfdir}/collect.d/collect_openstack
	install -m 755 collect_networking.sh ${D}/${sysconfdir}/collect.d/collect_networking
	install -m 755 collect_ceph.sh ${D}/${sysconfdir}/collect.d/collect_ceph
	install -m 755 collect_sm.sh ${D}/${sysconfdir}/collect.d/collect_sm
	install -m 755 collect_tc.sh ${D}/${sysconfdir}/collect.d/collect_tc
	install -m 755 collect_nfv_vim.sh ${D}/${sysconfdir}/collect.d/collect_nfv_vim
	install -m 755 collect_ovs.sh ${D}/${sysconfdir}/collect.d/collect_ovs
	install -m 755 collect_patching.sh ${D}/${sysconfdir}/collect.d/collect_patching
	install -m 755 collect_coredump.sh ${D}/${sysconfdir}/collect.d/collect_coredump
	install -m 755 collect_crash.sh ${D}/${sysconfdir}/collect.d/collect_crash
	install -m 755 collect_ima.sh ${D}/${sysconfdir}/collect.d/collect_ima
	install -m 755 collect_fm.sh ${D}/${sysconfdir}/collect.d/collect_fm
	install -m 755 collect_containerization.sh ${D}/${sysconfdir}/collect.d/collect_containerization

	install -m 755 etc.exclude ${D}/${sysconfdir}/collect/etc.exclude
	install -m 755 run.exclude ${D}/${sysconfdir}/collect/run.exclude

	ln -sf ${sbindir}/collect ${D}/${bindir}/collect
}
