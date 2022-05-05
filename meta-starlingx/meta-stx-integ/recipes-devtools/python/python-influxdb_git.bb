
SUMMARY = "InfluxDB is an open-source distributed time series database, find more about InfluxDB at https://docs.influxdata.com/influxdb/latest"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=046523829184aac3703a4c60c0ae2104"

SRCREV = "dc83fc6576b6463dcc77a0c101475a2a71ed655a"
PROTOCOL = "https"
BRANCH = "master"
S = "${WORKDIR}/git"
PV = "5.2.2+git${SRCPV}"

SRC_URI = "git://github.com/influxdata/influxdb-python.git;protocol=${PROTOCOL};rev=${SRCREV};branch=${BRANCH}"


DEPENDS += " python"
inherit setuptools distutils pkgconfig
