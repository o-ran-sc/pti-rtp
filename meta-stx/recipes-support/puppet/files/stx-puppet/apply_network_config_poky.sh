#!/bin/bash

################################################################################
# Copyright (c) 2016 Wind River Systems, Inc.
#
# SPDX-License-Identifier: Apache-2.0
#
################################################################################

#
#  Purpose of this script is to copy the puppet-built
#  network config file from the puppet dir to the /etc/network/interfaces  
#  Only copied when difference detected 
#
#  Please note:  function is_eq_ifcfg() is used to determine if
#                cfg files are different
#

ACQUIRE_LOCK=1
RELEASE_LOCK=0

if [ ! -f /var/run/interfaces.puppet ] ; then
    # No puppet file? Nothing to do!
    exit 1
fi

function log_it {
    logger "${0} ${1}"
}

function do_if_up {
    local iface=$1
    log_it "Bringing $iface up"
    /sbin/ifup $iface
}

function do_if_down {
    local iface=$1
    log_it "Bringing $iface down"
    /sbin/ifdown $iface
}

function do_rm {
    local theFile=$1
    log_it "Removing $theFile"
    /bin/rm  $theFile
}

function do_cp {
    local srcFile=$1
    local dstFile=$2
    log_it "copying network cfg $srcFile to $dstFile"
    cp  $srcFile $dstFile
}

function do_mv {
    local srcFile=$1
    local dstFile=$2
    log_it "Moving network cfg $srcFile to $dstFile"
    mv  $srcFile $dstFile
}

# Return items in list1 that are not in list2
array_diff () {
    list1=${!1}
    list2=${!2}

    result=()
    l2=" ${list2[*]} "
    for item in ${list1[@]}; do
        if [[ ! $l2 =~ " $item " ]] ; then
            result+=($item)
        fi
    done

    echo  ${result[@]}
}

function normalized_cfg_attr_value {
    local cfg=$1
    local attr_name=$2
    local attr_value
    attr_value=$(cat $cfg | grep $attr_name= | awk -F "=" {'print $2'})


    #
    # Special case BONDING_OPTS attribute.
    #
    # The BONDING_OPTS attribute contains '=' characters, so is not correctly
    # parsed by splitting on '=' as done above.  This results in changes to
    # BONDING_OPTS not causing the interface to be restarted, so the old
    # BONDING_OPTS still be used.  Because this is only checking for changes,
    # rather than actually using the returned value, we can return the whole
    # line.
    #
    if [[ "${attr_name}" == "BONDING_OPTS" ]]; then
        echo "$(cat $cfg | grep $attr_name=)"
        return $(true)
    fi

    if [[ "${attr_name}" != "BOOTPROTO" ]]; then
        echo "${attr_value}"
        return $(true)
    fi
    #
    # Special case BOOTPROTO attribute.
    #
    # The BOOTPROTO attribute is not populated consistently by various aspects
    # of the system.  Different values are used to indicate a manually
    # configured interfaces (i.e., one that does not expect to have an IP
    # address) and so to avoid reconfiguring an interface that has different
    # values with the same meaning we normalize them here before making any
    # decisions.
    #
    # From a user perspective the values "manual", "none", and "" all have the
    # same meaning - an interface without an IP address while "dhcp" and
    # "static" are distinct values with a separate meaning.  In practice
    # however, the only value that matters from a ifup/ifdown script point of
    # view is "dhcp".  All other values are ignored.
    #
    # In our system we set BOOTPROTO to "static" to indicate that IP address
    # attributes exist and to "manual"/"none" to indicate that no IP address
    # attributes exist.  These are not needed by ifup/ifdown as it looks for
    # the "IPADDR" attribute whenever BOOTPROTO is set to anything other than
    # "dhcp".
    #
    if [[ "${attr_value}" == "none" ]]; then
        attr_value="none"
    fi
    if [[ "${attr_value}" == "manual" ]]; then
        attr_value="none"
    fi
    if [[ "${attr_value}" == "" ]]; then
        attr_value="none"
    fi
    echo "${attr_value}"
    return $(true)
}

#
# returns $(true) if cfg file ( $1 ) has property propName ( $2 ) with a value of propValue ( $3 )
#
function cfg_has_property_with_value {
    local cfg=$1
    local propname=$2
    local propvalue=$3
    if [ -f $cfg ]; then
        if [[ "$(normalized_cfg_attr_value $cfg $propname)" == "${propvalue}" ]]; then
            return $(true)
        fi
    fi
    return $(false)
}

#
# returns $(true) if cfg file is configured as a slave
#
function is_slave {
    cfg_has_property_with_value $1 "SLAVE" "yes"
    return $?
}

#
# returns $(true) if cfg file is configured for DHCP
#
function is_dhcp {
    cfg_has_property_with_value $1 "BOOTPROTO" "dhcp"
}

#
# returns $(true) if cfg file is configured as a VLAN interface
#
function is_vlan {
    cfg_has_property_with_value $1 "VLAN" "yes"
    return $?
}

#
# returns $(true) if cfg file is configured as an ethernet interface.  For the
# purposes of this script "ethernet" is considered as any interface that is not
# a vlan or a slave.  This includes both regular ethernet interfaces and bonded
# interfaces.
#
function is_ethernet {
    if ! is_vlan $1; then
        if ! is_slave $1; then
            return $(true)
        fi
    fi
    return $(false)
}

#
# returns $(true) if cfg file represents an interface of the specified type.
#
function iftype_filter {
    local iftype=$1

    return $(is_$iftype $2)
}

#
# returns $(true) if ifcfg files have the same number of VFs
#
#
function is_eq_sriov_numvfs {
    local cfg_1=$1
    local cfg_2=$2
    local sriov_numvfs_1
    sriov_numvfs_1=$(grep -o 'echo *[1-9].*sriov_numvfs' $cfg_1 | awk {'print $2'})
    local sriov_numvfs_2
    sriov_numvfs_2=$(grep -o 'echo *[1-9].*sriov_numvfs' $cfg_2 | awk {'print $2'})

    sriov_numvfs_1=${sriov_numvfs_1:-0}
    sriov_numvfs_2=${sriov_numvfs_2:-0}

    if [[ "${sriov_numvfs_1}" != "${sriov_numvfs_2}" ]]; then
        log_it "$cfg_1 and $cfg_2 differ on attribute sriov_numvfs [${sriov_numvfs_1}:${sriov_numvfs_2}]"
        return $(false)
    fi

    return $(true)
}

#
# returns $(true) if ifcfg files are equal
#
# Warning:  Only compares against cfg file attributes:
#            BOOTPROTO DEVICE IPADDR NETMASK GATEWAY MTU BONDING_OPTS SRIOV_NUMVFS
#
function is_eq_ifcfg {
    local cfg_1=$1
    local cfg_2=$2

    for attr in BOOTPROTO DEVICE IPADDR NETMASK GATEWAY MTU BONDING_OPTS; do
        local attr_value1
        attr_value1=$(normalized_cfg_attr_value $cfg_1 $attr)
        local attr_value2
        attr_value2=$(normalized_cfg_attr_value $cfg_2 $attr)
        if [[ "${attr_value1}" != "${attr_value2}"  ]]; then
            log_it "$cfg_1 and $cfg_2 differ on attribute $attr"
            return $(false)
        fi
    done

    is_eq_sriov_numvfs $1 $2
    return $?
}

# Synchronize with sysinv-agent audit (ifup/down to query link speed).
function sysinv_agent_lock {
    case $1 in
    $ACQUIRE_LOCK)
        local lock_file="/var/run/apply_network_config.lock"
        # Lock file should be the same as defined in sysinv agent code
        local lock_timeout=5
        local max=15
        local n=1
        LOCK_FD=0
        exec {LOCK_FD}>$lock_file
        while [[ $n -le $max ]]; do

            flock -w $lock_timeout $LOCK_FD && break
            log_it "Failed to get lock($LOCK_FD) after $lock_timeout seconds ($n/$max), will retry"
            sleep 1
            n=$(($n+1))
        done
        if [[ $n -gt $max ]]; then
            log_it "Failed to acquire lock($LOCK_FD) even after $max retries"
            exit 1
        fi
        ;;
    $RELEASE_LOCK)
        [[ $LOCK_FD -gt 0 ]] && flock -u $LOCK_FD
        ;;
    esac
}


# synchronize with sysinv-agent audit
sysinv_agent_lock $ACQUIRE_LOCK

# check if this is a duplicated configuration
if ! diff -I '^#' "/var/run/interfaces.puppet" "/etc/network/interfaces" > /dev/null; then
    # now copy the puppet changed interfaces to /etc/network/interfaces
    do_mv /var/run/interfaces.puppet /etc/network/interfaces

    # now restart networking service 
    /etc/init.d/networking restart

    sleep 5
else
    # need to remove this file also
    do_rm /var/run/interfaces.puppet
fi


# workaround the loopback label addresses cannot be configured as scope of host
ip addr show lo | egrep "inet.*global.*lo:" > /tmp/loop$$

while read addr_info; do 
	echo $addr_info
	log_it "replace $addr_info with scope host"
	addr=`echo $addr_info | cut -d' ' -f 2`
	ifname=`echo $addr_info | cut -d' ' -f 5`
	ip addr del $addr dev lo label $ifname
	ip addr add $addr dev lo scope host label $ifname
done < /tmp/loop$$


# unlock: synchronize with sysinv-agent audit
sysinv_agent_lock $RELEASE_LOCK
