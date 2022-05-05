#!/bin/bash
# This script serves one purpose, to add a possibly missing attribute
# to a ppolicy schema in a dynamic configuration of OpenLDAP. This
# attribute was introduced in openldap-2.4.43 and slapd will not 
# start without it later on.
#
# The script tries to update in a directory given as first parameter,
# or in /etc/openldap/slapd.d implicitly.
#
# Author: Matus Honek <mhonek@redhat.com>
# Bugzilla: #1487857

function log {
    echo "Update dynamic configuration: " $@
    true
}

function iferr {
    if [ $? -ne 0 ]; then
	log "ERROR: " $@
	true
    else
	false
    fi
}

function update {
    set -u
    shopt -s extglob

    ORIGINAL="${1:-/etc/openldap/slapd.d}"
    ORIGINAL="${ORIGINAL%*(/)}"

    ### check if necessary
    grep -r "pwdMaxRecordedFail" "${ORIGINAL}/cn=config/cn=schema" >/dev/null
    [ $? -eq 0 ] && log "Schemas look up to date. Ok. Quitting." && return 0

    ### prep
    log "Prepare environment."
    
    TEMPDIR=$(mktemp -d)
    iferr "Could not create a temporary directory. Quitting." && return 1
    DBDIR="${TEMPDIR}/db"
    SUBDBDIR="${DBDIR}/cn=temporary"

    mkdir "${DBDIR}"
    iferr "Could not create temporary configuration directory. Quitting." && return 1
    cp -r --no-target-directory "${ORIGINAL}" "${SUBDBDIR}"
    iferr "Could not copy configuration. Quitting." && return 1
    
    pushd "$TEMPDIR" >/dev/null

    cat > temp.conf <<EOF
database ldif
suffix cn=temporary
directory db
access to * by * manage
EOF
    
    SOCKET="$(pwd)/socket"
    LISTENER="ldapi://${SOCKET//\//%2F}"
    CONN_PARAMS=("-Y" "EXTERNAL" "-H" "${LISTENER}")
    
    slapd -f temp.conf -h "$LISTENER" -d 0 >/dev/null 2>&1 &
    SLAPDPID="$!"
    sleep 2

    ldapadd ${CONN_PARAMS[@]} -d 0 >/dev/null 2>&1 <<EOF
dn: cn=temporary
objectClass: olcGlobal
cn: temporary
EOF
    iferr "Could not populate the temporary database. Quitting." && return 1
    
    ### update
    log "Update with new pwdMaxRecordedFailure attribute."
    FILTER="(&"
    FILTER+="(olcObjectClasses=*'pwdPolicy'*)"
    FILTER+="(!(olcObjectClasses=*'pwdPolicy'*'pwdMaxRecordedFailure'*))"
    FILTER+="(!(olcAttributeTypes=*'pwdMaxRecordedFailure'*))"
    FILTER+=")"
    RES=$(ldapsearch ${CONN_PARAMS[@]} \
		     -b cn=schema,cn=config,cn=temporary \
		     -LLL \
		     -o ldif-wrap=no \
		     "$FILTER" \
		     dn olcObjectClasses \
		     2>/dev/null \
	      | sed '/^$/d')
    DN=$(printf "$RES" | grep '^dn:')
    OC=$(printf "$RES" | grep "^olcObjectClasses:.*'pwdPolicy'")
    NEWOC="${OC//$ pwdSafeModify /$ pwdSafeModify $ pwdMaxRecordedFailure }"

    test $(echo "$DN" | wc -l) = 1
    iferr "Received more than one DN. Cannot continue. Quitting." && return 1
    test "$NEWOC" != "$OC"
    iferr "Updating pwdPolicy objectClass definition failed. Quitting." && return 1

    ldapmodify ${CONN_PARAMS[@]} -d 0 >/dev/null 2>&1 <<EOF
$DN
changetype: modify
add: olcAttributeTypes
olcAttributeTypes: ( 1.3.6.1.4.1.42.2.27.8.1.30 NAME 'pwdMaxRecordedFailur
 e' EQUALITY integerMatch ORDERING integerOrderingMatch  SYNTAX 1.3.6.1.4.1.
 1466.115.121.1.27 SINGLE-VALUE )
-
delete: olcObjectClasses
$OC
-
add: olcObjectClasses
$NEWOC
EOF
    iferr "Updating with new attribute failed. Quitting." && return 1

    popd >/dev/null

    ### apply
    log "Apply changes."
    cp -r --no-target-directory "$ORIGINAL" "$ORIGINAL~backup"
    iferr "Backing up old configuration failed. Quitting." && return 1
    cp -r --no-target-directory "$SUBDBDIR" "$ORIGINAL"
    iferr "Applying new configuration failed. Quitting." && return 1
    
    ### clean up
    log "Clean up."
    kill "$SLAPDPID"
    SLAPDPID=
    rm -rf "$TEMPDIR"
    TEMPDIR=
}

SLAPDPID=
TEMPDIR=
update "$1"
if [ $? -ne 0 ]; then
    log "Clean up."
    echo "$SLAPDPID"
    echo "$TEMPDIR"
    kill "$SLAPDPID"
    rm -rf "$TEMPDIR"
fi
log "Finished."
