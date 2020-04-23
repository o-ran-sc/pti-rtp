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

DESCRIPTION = "Barbican is a ReST API designed for the secure storage, provisioning and management of secrets."
HOMEPAGE = "https://wiki.openstack.org/wiki/Barbican"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fc8be9e7dffe97390d1216b01fd0be01"

PR = "r0"
SRCNAME = "barbican"
BARBICAN_MAX_PACKET_SIZE ?= "65535"

SRC_URI = " \
	git://github.com/openstack/barbican.git;branch=${BRANCH} \
	file://${BPN}/barbican-fix-path-to-find-configuration-files.patch \
	file://${BPN}/openstack-barbican-api.service \
	file://${BPN}/openstack-barbican-worker.service \
	file://${BPN}/openstack-barbican-keystone-listener.service \
	file://${BPN}/gunicorn-config.py \
	"

SRCREV = "4c0ddda941289fba8e5ec4341b5d02d155d46162"
BRANCH = "stable/stein"
PV = "8.0.0+git${SRCPV}"
S = "${WORKDIR}/git"

inherit update-rc.d setuptools identity hosts useradd default_configs monitor systemd

SYSTEMD_SERVICE_${SRCNAME} = " \
	openstack-barbican-api.service \
	openstack-barbican-worker.service \
	openstack-barbican-keystone-listener.service \
	"

SYSTEMD_AUTO_ENABLE_${SRCNAME} = "disable"

SERVICECREATE_PACKAGES = "${SRCNAME}-setup"
KEYSTONE_HOST="${CONTROLLER_IP}"

# USERCREATE_PARAM and SERVICECREATE_PARAM contain the list of parameters to be set.
# If the flag for a parameter in the list is not set here, the default value will be given to that parameter.
# Parameters not in the list will be set to empty.

USERCREATE_PARAM_${SRCNAME}-setup = "name pass tenant role email"
SERVICECREATE_PARAM_${SRCNAME}-setup = "name type description region publicurl adminurl internalurl"
python () {
    flags = {'type':'keystore',\
             'description':'Barbican Key Management Service',\
             'publicurl':"'http://${KEYSTONE_HOST}:9311/v1'",\
             'adminurl':"'http://${KEYSTONE_HOST}:9312/v1'",\
             'internalurl':"'http://${KEYSTONE_HOST}:9313/v1'"}
    d.setVarFlags("SERVICECREATE_PARAM_%s-setup" % d.getVar('SRCNAME',True), flags)
}
SERVICECREATE_PACKAGES[vardeps] += "KEYSTONE_HOST"

do_install_append() {
    TEMPLATE_CONF_DIR=${S}${sysconfdir}/${SRCNAME}
    BARBICAN_CONF_DIR=${D}${sysconfdir}/${SRCNAME}

    install -d ${BARBICAN_CONF_DIR}
    cp -r ${TEMPLATE_CONF_DIR}/* ${BARBICAN_CONF_DIR}

    install -d ${D}${localstatedir}/lib/barbican

    # Install the systemd service files
    install -d ${D}${systemd_system_unitdir}/
    install -m 644 ${WORKDIR}/${BPN}/*.service ${D}${systemd_system_unitdir}

    # python-gunicorn and gunicorn-config.py are required by openstack-barbican-api.service
    install -m 644 ${WORKDIR}/${PN}/gunicorn-config.py ${BARBICAN_CONF_DIR}

    # Modify barbican-api-paste.ini for gunicorn
    echo '[server:main]' >> ${BARBICAN_CONF_DIR}/barbican-api-paste.ini
    echo 'use = egg:gunicorn#main' >> ${BARBICAN_CONF_DIR}/barbican-api-paste.ini

    sed -e "s:%BARBICAN_CONF_DIR%:${sysconfdir}/${SRCNAME}:g" \
        -i ${D}/${PYTHON_SITEPACKAGES_DIR}/${SRCNAME}/tests/api/test_resources_policy.py
}

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system barbican"
USERADD_PARAM_${PN}  = "--system --home /var/lib/barbican -g barbican \
                        --no-create-home --shell /bin/false barbican"

PACKAGES += "${SRCNAME} \
             ${SRCNAME}-setup "

FILES_${PN} = "${libdir}/* \
"
FILES_${SRCNAME} = "${sysconfdir}/${SRCNAME}/* \
                    ${sysconfdir}/init.d/barbican-api \
	            ${bindir} \
	            ${bindir}/* \
                    ${localstatedir}/* \
                    ${systemd_system_unitdir} \
"

ALLOW_EMPTY_${SRCNAME}-setup = "1"
pkg_postinst_${SRCNAME}-setup () {
    if [ -z "$D" ]; then
        chown -R barbican:barbican ${sysconfdir}/${SRCNAME}
        chown -R barbican:barbican ${localstatedir}/lib/barbican
    fi
}

DEPENDS += " \
        python-pip \
        python-pbr-native \
        "
# Stx config files
DEPENDS += " \
	openstack-barbican-api \
	"


RDEPENDS_${SRCNAME} = "${PN} \
                       ${SRCNAME}-setup \
                       uwsgi \
                       python-falcon \
                       python-oslo.messaging"

RDEPENDS_${PN} += " \
        python-pip \
        python-pbr \
        python-alembic \
        python-babel \
        python-eventlet \
        python-falcon \
        python-iso8601 \
        python-jsonschema \
        python-kombu \
        python-netaddr \
        python-pastedeploy \
        python-paste \
        python-pycrypto \
        python-pysqlite \
        python-keystoneclient \
        python-sqlalchemy \
        python-stevedore \
        python-webob \
        python-wsgiref \
        python-barbicanclient \
        python-gunicorn \
	python-castellan \
	python-ldap3 \
        "

INITSCRIPT_PACKAGES = "${SRCNAME}"
INITSCRIPT_NAME_${SRCNAME} = "barbican-api"
INITSCRIPT_PARAMS_${SRCNAME} = "${OS_DEFAULT_INITSCRIPT_PARAMS}"

MONITOR_SERVICE_PACKAGES = "${SRCNAME}"
MONITOR_SERVICE_${SRCNAME} = "barbican"


FILES_${PN}_append = " ${datadir}/"
