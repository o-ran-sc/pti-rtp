FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://${PN}/ibsh-0.3e.patch \
	file://${PN}/ibsh-0.3e-cgcs.patch \
	file://${PN}/ibsh-0.3e-cgcs-copyright.patch \
	file://${PN}/admin.cmds \
	file://${PN}/admin.xtns \
	file://${PN}/LICENSE \
	file://${PN}/operator.cmds \
	file://${PN}/operator.xtns \
	file://${PN}/secadmin.cmds \
	file://${PN}/secadmin.xtns \
	"

do_install_append() {
	cp ${WORKDIR}/${PN}/admin.cmds ${D}/${sysconfdir}/ibsh/cmds/
	cp ${WORKDIR}/${PN}/admin.xtns ${D}/${sysconfdir}/ibsh/xtns/
	cp ${WORKDIR}/${PN}/operator.cmds ${D}/${sysconfdir}/ibsh/cmds/
	cp ${WORKDIR}/${PN}/operator.xtns ${D}/${sysconfdir}/ibsh/xtns/
	cp ${WORKDIR}/${PN}/secadmin.cmds ${D}/${sysconfdir}/ibsh/cmds/
	cp ${WORKDIR}/${PN}/secadmin.xtns ${D}/${sysconfdir}/ibsh/xtns/
}

