# o-ran repo for Performance Tuned Infrastructure

This includes a Yocto/OpenEmbedded compatible layer meta-oran and wrapper scripts
to pull all required Yocto/OE layers to build out the reference platform.

meta-oran layer depends on many Yocto/OE layers with 'thud' branch (Yocto version 2.6),to have a better user experience, meta-oran depends on WRLinux 1018 open source version and uses wrlinux setup tools to create the build environment.

## About Yocto and Wind River Linux

The Yocto Project is an open source collaboration project that provides templates,
tools and methods to help you create custom Linux-based systems for embedded and
IOT products, regardless of the hardware architecture.

Wind River is a founding member of the Linux Foundation's Yocto Project and continues
to help maintain many Yocto Project components.

Wind River Linux is based on Yocto and is the leading free open-source Linux for the
embedded industry.

## How to use

### Prerequisite:

  * Your host need to meet the requirements for Yocto, please refer to:
    * [Compatible Linux Distribution](https://www.yoctoproject.org/docs/2.6.3/brief-yoctoprojectqs/brief-yoctoprojectqs.html#brief-compatible-distro)
    * [Supported Linux Distributions](https://www.yoctoproject.org/docs/2.6.3/ref-manual/ref-manual.html#detailed-supported-distros)
    * [Required Packages for the Build Host](https://www.yoctoproject.org/docs/2.6.3/ref-manual/ref-manual.html#required-packages-for-the-build-host)

  * The recommended and tested host is Ubuntu 16.04/18.04 and CentOS 7.
    * To install the required packages for Ubuntu 16.04/18.04:
```
$ sudo apt-get install gawk wget git-core diffstat unzip texinfo gcc-multilib \
  build-essential chrpath socat cpio python python3 python3-pip python3-pexpect \
  xz-utils debianutils iputils-ping make xsltproc docbook-utils fop dblatex xmlto \
  python-git
```

    * To install the required packages for CentOS 7:

```
$ sudo yum install -y epel-release
$ sudo yum makecache
$ sudo yum install gawk make wget tar bzip2 gzip python unzip perl patch \
  diffutils diffstat git cpp gcc gcc-c++ glibc-devel texinfo chrpath socat \
  perl-Data-Dumper perl-Text-ParseWords perl-Thread-Queue python34-pip xz \
  which SDL-devel xterm
```
### Use wrapper script build_oran.sh to build the image

```
# Get the wrapper script with either curl or wget
$ curl -o build_oran.sh 'https://gerrit.o-ran-sc.org/r/gitweb?p=pti/rtp.git;a=blob_plain;f=scripts/build_oran.sh;hb=HEAD'
$ wget -O build_oran.sh 'https://gerrit.o-ran-sc.org/r/gitweb?p=pti/rtp.git;a=blob_plain;f=scripts/build_oran.sh;hb=HEAD'

$ chmod +x build_oran.sh
$ WORKSPACE=/path/to/workspace
$ ./build_oran.sh -w ${WORKSPACE}
```

If all go well, you will get the ISO image in:
${WORKSPACE}/prj_wrl1018_oran/tmp-glibc/deploy/images/intel-x86-64/oran-image-inf-host-intel-x86-64.iso

