
inherit useradd

USERADD_PACKAGES = "${PN}"

USERADD_PARAM_${PN} = "-r -g ldap -c 'LDAP Client User' -u 65 -d / -s /sbin/nologin nslcd"
