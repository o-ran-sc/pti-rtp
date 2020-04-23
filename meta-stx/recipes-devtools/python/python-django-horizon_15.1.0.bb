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

DESCRIPTION = "Simple database sharding (horizontal partitioning) library for Django applications."
HOMEPAGE = "http://horizon.openstack.org/"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

inherit setuptools 

SRC_URI[md5sum] = "21c32ba58806b351ede4eca6804e6d3e"
SRC_URI[sha256sum] = "25cf663f8f9a0233edbd5ba322acc28805fca684df28290c3e62a79abf6724e9"

S = "${WORKDIR}/horizon-${PV}"

SRC_URI = "https://tarballs.openstack.org/horizon/horizon-${PV}.tar.gz"

DEPENDS += " \
	python-pip \
	python-pbr-native \
	"


RDEPENDS_${PN} = " \
	python-pbr \
	python-babel \
	python-django \
	python-django \
	python-django-babel \
	python-django-compressor \
	python-django-debreach \
	python-django-pyscss \
	python-futurist \
	python-iso8601 \
	python-keystoneauth1 \
	python-netaddr \
	python-oslo.concurrency \
	python-oslo.config \
	python-oslo.i18n \
	python-oslo.policy \
	python-oslo.serialization \
	python-oslo.upgradecheck \
	python-oslo.utils \
	python-osprofiler \
	python-pint \
	python-pymongo \
	python-pyscss \
	python-cinderclient \
	python-glanceclient \
	python-keystoneclient \
	python-neutronclient \
	python-novaclient \
	python-swiftclient \
	python-pytz \
	python-pyyaml \
	python-requests \
	python-semantic-version \
	python-six \
	python-xstatic \
	python-xstatic-angular \
	python-xstatic-angular-bootstrap \
	python-xstatic-angular-fileupload \
	python-xstatic-angular-gettext \
	python-xstatic-angular-lrdragndrop \
	python-xstatic-angular-schema-form \
	python-xstatic-bootstrap-datepicker \
	python-xstatic-bootstrap-scss \
	python-xstatic-bootswatch \
	python-xstatic-d3 \
	python-xstatic-hogan \
	python-xstatic-font-awesome \
	python-xstatic-jasmine \
	python-xstatic-jquery \
	python-xstatic-jquery-migrate \
	python-xstatic-jquery.quicksearch \
	python-xstatic-jquery.tablesorter \
	python-xstatic-jquery-ui \
	python-xstatic-jsencrypt \
	python-xstatic-mdi \
	python-xstatic-objectpath \
	python-xstatic-mdi \
	python-xstatic-objectpath \
	python-xstatic-rickshaw \
	python-xstatic-roboto-fontface \
	python-xstatic-smart-table \
	python-xstatic-spin \
	python-xstatic-term.js \
	python-xstatic-tv4 \
	"
