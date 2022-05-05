
SUMMARY = "syslog replacement"
DESCRIPTION = " \
	The EventLog library aims to be a replacement of the simple syslog() API \
	provided on UNIX systems. The major difference between EventLog and syslog \
	is that EventLog tries to add structure to messages. \
	 \
	Where you had a simple non-structrured string in syslog() you have a \
	combination of description and tag/value pairs. \
	 \
	EventLog provides an interface to build, format and output an event record. \
	The exact format and output method can be customized by the administrator \
	via a configuration file \
	  "

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=b8ba8e77bcda9a53fac0fe39fe957767"

SRCREV = "a5c19163ba131f79452c6dfe4e31c2b4ce4be741"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "1.0+git${SRCPV}"

SRC_URI = "git://github.com/balabit/eventlog.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"

inherit autotools pkgconfig
