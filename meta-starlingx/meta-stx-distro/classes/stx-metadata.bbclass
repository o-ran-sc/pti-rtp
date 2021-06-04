# This class is intended to help apply the patches and install
# config files fetched from stx git repo defined in STX_REPO so to
# avoid maintaining a local copy in the recipe's metadata.
#
# This adds dependency on stx-${STX_REPO}-source which
# fetches the stx source code and is used as a shared work
# directory, and the search path of patches and config files
# for the recipe will be added in FILESEXTRAPATHS so the
# patches will be found and applied in do_patch, and STX_METADATA_PATH
# can be used to locate config files to be installed.
#
# Please set the following variables correctly after inherit
# this bbclass:
# - STX_REPO: the StarlingX repo name, default is 'integ'
# - STX_SUBPATH: the subpath for the patches in the work-shard
#                directory of stx-${STX_REPO}-source
# - SRC_URI_STX: the patch list in stx-${STX_REPO}-source
#
# e.g.
# STX_REPO = "integ"
# STX_SUBPATH = "config/puppet-modules/openstack/${BP}/centos/patches"
# SRC_URI_STX = "file://0001-Remove-log_dir-from-conf-files.patch"

STX_REPO ?= "integ"
STX_SUBPATH ?= ""
SRC_URI_STX ?= ""

STX_METADATA_PATH = "${TMPDIR}/work-shared/stx-${STX_REPO}-source/git/${STX_SUBPATH}"
FILESEXTRAPATHS_prepend = "${STX_METADATA_PATH}:"

do_patch[depends] += "stx-${STX_REPO}-source:do_patch"

do_patch_prepend() {
    bb.build.exec_func('add_stx_patch', d)
}

python add_stx_patch() {
    src_uri = d.getVar('SRC_URI', False)
    src_uri_stx = d.getVar('SRC_URI_STX', False)
    d.setVar('SRC_URI', src_uri_stx + " " + src_uri)
}
