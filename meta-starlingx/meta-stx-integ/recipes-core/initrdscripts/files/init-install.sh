#!/bin/sh -e
#
# install.sh [device_name] [rootfs_name] [video_mode] [vga_mode]
#

PATH=/sbin:/bin:/usr/sbin:/usr/bin

# Get a list of hard drives
hdnamelist=""
live_dev_name=`cat /proc/mounts | grep ${1%/} | awk '{print $1}'`
live_dev_name=${live_dev_name#\/dev/}
# Only strip the digit identifier if the device is not an mmc
case $live_dev_name in
    mmcblk*)
    ;;
    nvme*)
    ;;
    *)
        live_dev_name=${live_dev_name%%[0-9]*}
    ;;
esac

echo "Searching for hard drives ..."

# Some eMMC devices have special sub devices such as mmcblk0boot0 etc
# we're currently only interested in the root device so pick them wisely
devices=`ls /sys/block/ | grep -v mmcblk` || true
mmc_devices=`ls /sys/block/ | grep "mmcblk[0-9]\{1,\}$"` || true
devices="$devices $mmc_devices"

for device in $devices; do
    case $device in
        loop*)
            # skip loop device
            ;;
        sr*)
            # skip CDROM device
            ;;
        ram*)
            # skip ram device
            ;;
        *)
            # skip the device LiveOS is on
            # Add valid hard drive name to the list
            case $device in
                $live_dev_name*)
                # skip the device we are running from
                ;;
                *)
                    hdnamelist="$hdnamelist $device"
                ;;
            esac
            ;;
    esac
done

TARGET_DEVICE_NAME=""
for hdname in $hdnamelist; do
    # Display found hard drives and their basic info
    echo "-------------------------------"
    echo /dev/$hdname
    if [ -r /sys/block/$hdname/device/vendor ]; then
        echo -n "VENDOR="
        cat /sys/block/$hdname/device/vendor
    fi
    if [ -r /sys/block/$hdname/device/model ]; then
        echo -n "MODEL="
        cat /sys/block/$hdname/device/model
    fi
    if [ -r /sys/block/$hdname/device/uevent ]; then
        echo -n "UEVENT="
        cat /sys/block/$hdname/device/uevent
    fi
    echo
done

# use the first one found
for hdname in $hdnamelist; do
    TARGET_DEVICE_NAME=$hdname
    break
done

if [ -n "$TARGET_DEVICE_NAME" ]; then
    echo "Installing image on /dev/$TARGET_DEVICE_NAME ..."
else
    echo "No hard drive found. Installation aborted."
    exit 1
fi

device=/dev/$TARGET_DEVICE_NAME

#
# Unmount anything the automounter had mounted
#

for dir in `awk '/\/dev.* \/run\/media/{print $2}' /proc/mounts | grep $TARGET_DEVICE_NAME`; do
	umount $dir
done

if [ ! -b /dev/loop0 ] ; then
    mknod /dev/loop0 b 7 0
fi

mkdir -p /tmp
if [ ! -L /etc/mtab ] && [ -e /proc/mounts ]; then
    ln -sf /proc/mounts /etc/mtab
fi

disk_size=$(parted ${device} unit mb print | grep '^Disk .*: .*MB' | cut -d" " -f 3 | sed -e "s/MB//")

grub_version=$(grub-install -V|sed 's/.* \([0-9]\).*/\1/')

if [ $grub_version -eq 0 ] ; then
    bios_boot_size=0
else
    # For GRUB 2 we need separate parition to store stage2 grub image
    # 2Mb value is chosen to align partition for best performance.
    bios_boot_size=2
fi

boot_size=512
rootfs_size=20000
log_vol_size=8000
scratch_vol_size=8000

data_size=$((disk_size-bios_boot_size-boot_size-rootfs_size))
boot_start=$((bios_boot_size))
rootfs_start=$((bios_boot_size+boot_size))
rootfs_end=$((rootfs_start+rootfs_size))
data_start=$((rootfs_end))

# MMC devices are special in a couple of ways
# 1) they use a partition prefix character 'p'
# 2) they are detected asynchronously (need rootwait)
rootwait=""
part_prefix=""
if [ ! "${device#/dev/mmcblk}" = "${device}" ] || \
   [ ! "${device#/dev/nvme}" = "${device}" ]; then
    part_prefix="p"
    rootwait="rootwait"
fi

# USB devices also require rootwait
if [ -n `readlink /dev/disk/by-id/usb* | grep $TARGET_DEVICE_NAME` ]; then
    rootwait="rootwait"
fi

if [ $grub_version -eq 0 ] ; then
    bios_boot=''
    bootfs=${device}${part_prefix}1
    rootfs=${device}${part_prefix}2
    data=${device}${part_prefix}3
else
    bios_boot=${device}${part_prefix}1
    bootfs=${device}${part_prefix}2
    rootfs=${device}${part_prefix}3
    data=${device}${part_prefix}4
fi

echo "*********************************************"
[ $grub_version -ne 0 ] && echo "BIOS boot partition size: $bios_boot_size MB ($bios_boot)"
echo "Boot partition size:   $boot_size MB ($bootfs)"
echo "Rootfs partition size: $rootfs_size MB ($rootfs)"
echo "Data partition size:   $data_size MB ($data)"
echo "*********************************************"
echo "Deleting partition table on ${device} ..."
dd if=/dev/zero of=${device} bs=512 count=35

echo "Creating new partition table on ${device} ..."
if [ $grub_version -eq 0 ] ; then
    parted ${device} mktable msdos
    echo "Creating boot partition on $bootfs"
    parted ${device} mkpart primary ext3 0% $boot_size
else
    parted ${device} mktable gpt
    echo "Creating BIOS boot partition on $bios_boot"
    parted ${device} mkpart bios_boot 0% $bios_boot_size
    parted ${device} set 1 bios_grub on
    echo "Creating boot partition on $bootfs"
    parted ${device} mkpart boot ext3 $boot_start $boot_size
fi

echo "Creating rootfs partition on $rootfs"
[ $grub_version -eq 0 ] && pname='primary' || pname='root'
parted ${device} -s mkpart $pname ext4 $rootfs_start $rootfs_end

echo "Creating data partition on $data"
[ $grub_version -eq 0 ] && pname='primary' || pname='data'
parted ${device} -s mkpart $pname $data_start 100%
parted ${device} -s set 4 lvm on

parted ${device} print

echo "Waiting for device nodes..."
C=0
while [ $C -ne 3 ] && [ ! -e $bootfs  -o ! -e $rootfs -o ! -e $data ]; do
    C=$(( C + 1 ))
    sleep 1
done

echo "Formatting $bootfs to ext3..."
mkfs.ext3 -F $bootfs

echo "Formatting $rootfs to ext4..."
mkfs.ext4 -F $rootfs

echo "Create LVM for $data..."
vg_name="cgts-vg"

# Disable udev scan in lvm.conf
sed -i 's/\(md_component_detection =\).*/\1 0/' /etc/lvm/lvm.conf

pvcreate -y -ff $data
vgcreate -y -ff $vg_name $data

udevd -d

lvcreate -y -n log-lv --size $log_vol_size $vg_name
lvcreate -y -n scratch-lv --size  $scratch_vol_size $vg_name

mkfs.ext4 -F /dev/$vg_name/log-lv
mkfs.ext4 -F /dev/$vg_name/scratch-lv

mkdir /tgt_root
mkdir /tgt_log
mkdir /src_root
mkdir -p /boot

if [ ! -f /run/media/$1/$2 ]; then
    mkdir -p /run/media/$1
    mount /dev/$1 /run/media/$1
fi

# Handling of the target root partition
mount $rootfs /tgt_root
mount /dev/$vg_name/log-lv /tgt_log
mount -o rw,loop,noatime,nodiratime /run/media/$1/$2 /src_root
echo "Copying rootfs files..."
cp -a /src_root/* /tgt_root
if [ -d /tgt_root/etc/ ] ; then
    if [ $grub_version -ne 0 ] ; then
        boot_uuid=$(blkid -o value -s UUID ${bootfs})
        bootdev="UUID=$boot_uuid"
    else
        bootdev=${bootfs}
    fi
    sed -i '/vfat/d' /tgt_root/etc/fstab
    echo "$bootdev  /boot  ext3  defaults  1  2" >> /tgt_root/etc/fstab
    echo "/dev/$vg_name/log-lv  /var/log  ext4  defaults  1  2" >> /tgt_root/etc/fstab
    echo "/dev/$vg_name/scratch-lv  /scratch  ext4  defaults  1  2" >> /tgt_root/etc/fstab

    # We dont want udev to mount our root device while we're booting...
    if [ -d /tgt_root/etc/udev/ ] ; then
        echo "${device}" >> /tgt_root/etc/udev/mount.blacklist
    fi
fi

INSTALL_UUID=`uuidgen`
cat << _EOF > /tgt_root/etc/platform/platform.conf
nodetype=controller
subfunction=controller,worker
system_type=All-in-one
security_profile=standard
management_interface=lo
http_port=8080
INSTALL_UUID=${INSTALL_UUID}
_EOF

# Create first_boot flag
touch /tgt_root/etc/platform/.first_boot

# The grub.cfg is created by installer, so the postinsts script is not needed.
rm -f /tgt_root/etc/rpm-postinsts/*-grub

# /var/log will be mounted to the log-lv
# so move the all files to log-lv
cp -rf /tgt_root/var/log/* /tgt_log
rm -rf /tgt_root/var/log

# Fake as anaconda to add info needed by stx 3.0
cat << _EOF > /tgt_root/etc/rpm-postinsts/999-anaconda
# anaconda - postinst
#!/bin/sh
set -e
mkdir -p /var/log/anaconda/
echo "Display mode = t" > /var/log/anaconda/anaconda.log
_EOF
chmod 0755 /tgt_root/etc/rpm-postinsts/999-anaconda

umount /tgt_root
umount /src_root

echo "Looking for kernels to use as boot target.."
# Find kernel to boot to
# Give user options if multiple are found
kernels="$(find /run/media/$1/ -type f  \
           -name bzImage* -o -name zImage* \
           -o -name vmlinux* -o -name vmlinuz* \
           -o -name fitImage* \
           | sed s:.*/::)"
if [ -n "$(echo $kernels)" ]; then
    # only one kernel entry if no space
    if [ -z "$(echo $kernels | grep " ")" ]; then
        kernel=$kernels
        echo "$kernel will be used as the boot target"
    else
        echo "Which kernel do we want to boot by default? The following kernels were found:"
        echo $kernels
        read answer
        kernel=$answer
    fi
else
    echo "No kernels found, exiting..."
    exit 1
fi

# Handling of the target boot partition
mount $bootfs /boot
echo "Preparing boot partition..."

if [ -f /etc/grub.d/00_header -a $grub_version -ne 0 ] ; then
    echo "Preparing custom grub2 menu..."
    root_part_uuid=$(blkid -o value -s PARTUUID ${rootfs})
    boot_uuid=$(blkid -o value -s UUID ${bootfs})
    GRUBCFG="/boot/grub/grub.cfg"
    mkdir -p $(dirname $GRUBCFG)
    cat >$GRUBCFG <<_EOF
timeout=5
default=0
menuentry "Yocto Linux with StarlingX @STX_ID@" {
    search --no-floppy --fs-uuid $boot_uuid --set root
    linux /$kernel root=$rootfs $rootwait rw console=tty0 console=ttyS0,115200 $5 $3 $4
}
_EOF
    chmod 0444 $GRUBCFG
fi
grub-install ${device}

if [ $grub_version -eq 0 ] ; then
    echo "(hd0) ${device}" > /boot/grub/device.map
    echo "Preparing custom grub menu..."
    echo "default 0" > /boot/grub/menu.lst
    echo "timeout 30" >> /boot/grub/menu.lst
    echo "title Live Boot/Install-Image" >> /boot/grub/menu.lst
    echo "root  (hd0,0)" >> /boot/grub/menu.lst
    echo "kernel /$kernel root=$rootfs rw $3 $4 quiet" >> /boot/grub/menu.lst
fi

# Copy kernel artifacts. To add more artifacts just add to types
# For now just support kernel types already being used by something in OE-core
for types in bzImage zImage vmlinux vmlinuz fitImage; do
    for kernel in `find /run/media/$1/ -name $types*`; do
        cp $kernel /boot
    done
done

umount /boot

sync

echo "Remove your installation media, and press ENTER"

read enter

echo "Rebooting..."
reboot -f
