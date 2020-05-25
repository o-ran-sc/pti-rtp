#
# Copyright (C) 2019 Wind River Systems, Inc.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.


do_install() {
    kerneldir=${D}${KERNEL_SRC_PATH}
    install -d $kerneldir

    install -d ${D}${KERNEL_BUILD_ROOT}${KERNEL_VERSION}
    (
        cd ${D}${KERNEL_BUILD_ROOT}${KERNEL_VERSION}
        rm -f source build
        ln -s ${KERNEL_SRC_PATH} source
        ln -s ${KERNEL_SRC_PATH} build
    )

    #
    # Copy the staging dir source (and module build support) into the devsrc structure.
    # We can keep this copy simple and take everything, since a we'll clean up any build
    # artifacts afterwards, and the extra i/o is not significant
    #
    cd ${B}
    find . -type d -name '.git*' -prune -o -path '.debug' -prune -o -type f -print0 | cpio --null -pdlu $kerneldir
    cd ${S}
    find . -type d -name '.git*' -prune -o -type d -name '.kernel-meta' -prune -o -type f -print0 | cpio --null -pdlu $kerneldir

    # Explicitly set KBUILD_OUTPUT to ensure that the image directory is cleaned and not
    # The main build artifacts. We clean the directory to avoid QA errors on mismatched
    # architecture (since scripts and helpers are native format).
    KBUILD_OUTPUT="$kerneldir"
    oe_runmake -C $kerneldir CC="${KERNEL_CC}" LD="${KERNEL_LD}" clean _mrproper_scripts

    # make clean generates an absolute path symlink called "source"
    # in $kerneldir points to $kerneldir, which doesn't make any
    # sense, so remove it.
    if [ -L $kerneldir/source ]; then
        bbnote "Removing $kerneldir/source symlink"
        rm -f $kerneldir/source
    fi

    # As of Linux kernel version 3.0.1, the clean target removes
    # arch/powerpc/lib/crtsavres.o which is present in
    # KBUILD_LDFLAGS_MODULE, making it required to build external modules.
    if [ ${ARCH} = "powerpc" ]; then
            mkdir -p $kerneldir/arch/powerpc/lib/
            cp ${B}/arch/powerpc/lib/crtsavres.o $kerneldir/arch/powerpc/lib/crtsavres.o
    fi

    chown -R root:root ${D}
}

INSANE_SKIP_${PN} = "arch"
