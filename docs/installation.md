#  O-RAN INF image installation and configuration for better real time performance

## 1. O-RAN INF image installation

* Please see the README.md file for how to build the image.
* The Image is a live ISO image with CLI installer: oran-image-inf-host-intel-x86-64.iso

#### 1.1 Burn the image to the USB device

* Assume the the usb device is /dev/sdX here

```
 $ sudo dd if=/path/to/oran-image-inf-host-intel-x86-64.iso of=/dev/sdX bs=1M
```

#### 1.2 Insert the USB device in the target to be booted.

#### 1.3 Reboot the target from the USB device.

#### 1.4 Select "Graphics console install" or "Serial console install" and press ENTER

#### 1.5 Select the hard disk and press ENTER

> Notes: In the CLI installer, you can only select which hard disk to install, the whole disk will be used and partitioned automatically.

* e.g. insert "sda" and press ENTER

#### 1.6 Remove the USB device and press ENTER to reboot

## 2. Configuration for better real time performance

> Some of the tuning options are machine specific or depend on use cases,
> like the hugepages, isolcpus, rcu_nocbs, kthread_cpus, irqaffinity, nohz_full and
> so on, please do not just copy and past.

* Edit the grub.cfg with the following example tuning options

```bash
# Notes: the grub.cfg file path is different for legacy and UEFI mode
#   For legacy mode: /boot/grub/grub.cfg
#   For UEFI mode: /boot/EFI/BOOT/grub.cfg

grub_cfg="/boot/grub/grub.cfg"
#grub_cfg="/boot/EFI/BOOT/grub.cfg"

# In this example, 1-16 cores are isolated for real time processes
root@intel-x86-64:~# rt_tuning="crashkernel=auto biosdevname=0 iommu=pt usbcore.autosuspend=-1 nmi_watchdog=0 softlockup_panic=0 intel_iommu=on cgroup_enable=memory skew_tick=1 hugepagesz=1G hugepages=4 default_hugepagesz=1G isolcpus=1-16 rcu_nocbs=1-16 kthread_cpus=0 irqaffinity=0 nohz=on nohz_full=1-16 intel_idle.max_cstate=0 processor.max_cstate=1 intel_pstate=disable nosoftlockup idle=poll mce=ignore_ce"

# optional to add the console setting
root@intel-x86-64:~# console="console=ttyS0,115200"

root@intel-x86-64:~# sed -i "/linux / s/$/ $console $rt_tuning/" $grub_cfg
```
* Reboot the target

```bash
root@intel-x86-64:~# reboot
```
