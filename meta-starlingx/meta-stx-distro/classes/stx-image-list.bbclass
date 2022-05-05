#
## Copyright (C) 2019 Wind River Systems, Inc.
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

IMAGE_LIST = "${IMGDEPLOYDIR}/${IMAGE_NAME}.rootfs.pkglist"

ROOTFS_POSTPROCESS_COMMAND_append = " write_image_list;"

python write_image_list () {
    from oe.rootfs import image_list_installed_packages

    deploy_dir = d.getVar('IMGDEPLOYDIR')
    link_name = d.getVar('IMAGE_LINK_NAME')
    image_list_file = d.getVar('IMAGE_LIST')

    pkg_dict = image_list_installed_packages(d)
    output = []
    for pkg in sorted(pkg_dict):
        output.append(pkg_dict[pkg]["filename"])
    output_str = '\n'.join(output) + '\n'

    with open(image_list_file, 'w+') as image_pkglist:
        image_pkglist.write(output_str)

    if os.path.exists(image_list_file):
        pkglist_link = deploy_dir + "/" + link_name + ".pkglist"
        if os.path.lexists(pkglist_link):
            os.remove(pkglist_link)
        os.symlink(os.path.basename(image_list_file), pkglist_link)
}
